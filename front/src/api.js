// Tiny API + auth layer.
// Token lives in memory only — never persisted.
// Credentials are persisted only when "remember" is on, and only to auto-login next visit.
const CREDS_KEY = 'creds';
let token = null;

export const getToken   = () => token;
export const setToken   = (t) => { token = t; };
export const clearToken = () => { token = null; };

export const setCreds = (creds) => localStorage.setItem(CREDS_KEY, JSON.stringify(creds));
export const clearCreds = () => localStorage.removeItem(CREDS_KEY);
export function getCreds() {
  try {
    const raw = localStorage.getItem(CREDS_KEY);
    return raw ? JSON.parse(raw) : null;
  } catch { return null; }
}

async function apiFetch(path, opts = {}) {
  const token = getToken();
  const res = await fetch(path, {
    ...opts,
    headers: {
      Accept: 'application/json',
      ...(opts.body ? { 'Content-Type': 'application/json' } : null),
      ...(token ? { Authorization: `Bearer ${token}` } : null),
      ...opts.headers,
    },
  });
  if (!res.ok) {
    let msg = '';
    try {
      const body = await res.json();
      msg = body.message || body.detail || '';
    } catch { /* body wasn't JSON */ }
    const err = new Error(msg || `HTTP ${res.status}`);
    err.status = res.status;
    throw err;
  }
  return res.status === 204 ? null : res.json();
}

// Flat object -> ?a=1&b=2 (skips null/empty, supports arrays).
const qs = (params) => {
  const u = new URLSearchParams();
  Object.entries(params || {}).forEach(([k, v]) => {
    if (v == null || v === '') return;
    Array.isArray(v) ? v.forEach(x => u.append(k, x)) : u.append(k, v);
  });
  const s = u.toString();
  return s ? `?${s}` : '';
};

// Internal glue used by the helpers below.
const request = (path, { method = 'GET', query, body } = {}) =>
  apiFetch(`${path}${qs(query)}`, {
    method,
    body: body === undefined ? undefined : JSON.stringify(body),
  });

// GET list — with paging/filter.   getAll('user', { page: 0, size: 10, username: 'john' })
export const getAll = (entity, query) => request(`/${entity}`, { query });

// GET one — by id.                  getOne('user', 5)
export const getOne = (entity, id) => request(`/${entity}/${id}`);

// POST / PUT / DELETE — create, update, delete.
//   send('user', 'POST',   { username, password })
//   send('user', 'PUT',    { id, username })
//   send('user', 'DELETE', null, 5)
export const send = (entity, method, body, id) =>
  request(id != null ? `/${entity}/${id}` : `/${entity}`, { method, body });

export const authenticate = (username, password, role) =>
  request('/auth/authenticate', { method: 'POST', body: { username, password, role } });
