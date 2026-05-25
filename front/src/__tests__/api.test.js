import { getAll, getOne, send, authenticate, setToken, clearToken, setCreds, getCreds, clearCreds } from '../api';

const okJson = (data = {}, status = 200) => ({
  ok: status >= 200 && status < 300,
  status,
  text: () => Promise.resolve(JSON.stringify(data)),
});
const okEmpty = (status = 200) => ({
  ok: status >= 200 && status < 300,
  status,
  text: () => Promise.resolve(''),
});

beforeEach(() => {
  global.fetch = jest.fn(() => Promise.resolve(okJson({ id: 1 })));
  clearToken();
  clearCreds();
});

describe('getAll', () => {
  test('builds /entity URL with query string', async () => {
    await getAll('user', { page: 0, size: 10, username: 'john' });
    const [url] = global.fetch.mock.calls[0];
    expect(url).toBe('/user?page=0&size=10&username=john');
  });

  test('no query -> bare /entity', async () => {
    await getAll('country');
    expect(global.fetch.mock.calls[0][0]).toBe('/country');
  });

  test('attaches Bearer token when set', async () => {
    setToken('abc');
    await getAll('user');
    const opts = global.fetch.mock.calls[0][1];
    expect(opts.headers.Authorization).toBe('Bearer abc');
  });

  test('omits Authorization when token is null', async () => {
    await getAll('user');
    const opts = global.fetch.mock.calls[0][1];
    expect(opts.headers.Authorization).toBeUndefined();
  });
});

describe('getOne', () => {
  test('builds /entity/{id}', async () => {
    await getOne('user', 5);
    expect(global.fetch.mock.calls[0][0]).toBe('/user/5');
  });
});

describe('send', () => {
  test('POST without id -> /entity', async () => {
    await send('user', 'POST', { username: 'x' });
    const [url, opts] = global.fetch.mock.calls[0];
    expect(url).toBe('/user');
    expect(opts.method).toBe('POST');
    expect(JSON.parse(opts.body)).toEqual({ username: 'x' });
    expect(opts.headers['Content-Type']).toBe('application/json');
  });

  test('PUT serialises body', async () => {
    await send('user', 'PUT', { id: 5, name: 'X' });
    const opts = global.fetch.mock.calls[0][1];
    expect(opts.method).toBe('PUT');
    expect(JSON.parse(opts.body)).toEqual({ id: 5, name: 'X' });
  });

  test('DELETE with id -> /entity/{id}, no body', async () => {
    await send('user', 'DELETE', null, 5);
    const [url, opts] = global.fetch.mock.calls[0];
    expect(url).toBe('/user/5');
    expect(opts.method).toBe('DELETE');
  });
});

describe('apiFetch error handling', () => {
  test('empty 200 body resolves to null (not a JSON parse error)', async () => {
    global.fetch = jest.fn(() => Promise.resolve(okEmpty(200)));
    await expect(send('user', 'DELETE', null, 5)).resolves.toBeNull();
  });

  test('non-2xx with JSON {message} throws with that text and .status', async () => {
    global.fetch = jest.fn(() => Promise.resolve(okJson({ message: 'boom' }, 422)));
    try {
      await getOne('user', 5);
      throw new Error('should have thrown');
    } catch (err) {
      expect(err.message).toBe('boom');
      expect(err.status).toBe(422);
    }
  });

  test('non-2xx with empty body falls back to "HTTP {status}"', async () => {
    global.fetch = jest.fn(() => Promise.resolve(okEmpty(500)));
    await expect(getOne('user', 5)).rejects.toMatchObject({ status: 500, message: 'HTTP 500' });
  });
});

describe('authenticate', () => {
  test('POSTs to /auth/authenticate with username/password/role', async () => {
    await authenticate('admin', 'pwd', 'ROLE_ADMIN');
    const [url, opts] = global.fetch.mock.calls[0];
    expect(url).toBe('/auth/authenticate');
    expect(opts.method).toBe('POST');
    expect(JSON.parse(opts.body)).toEqual({ username: 'admin', password: 'pwd', role: 'ROLE_ADMIN' });
  });
});

describe('creds storage', () => {
  test('setCreds + getCreds round-trip', () => {
    setCreds({ login: 'a', password: 'b', role: 'ROLE_ADMIN' });
    expect(getCreds()).toEqual({ login: 'a', password: 'b', role: 'ROLE_ADMIN' });
  });

  test('clearCreds wipes', () => {
    setCreds({ login: 'a' });
    clearCreds();
    expect(getCreds()).toBeNull();
  });

  test('getCreds returns null if storage is empty', () => {
    expect(getCreds()).toBeNull();
  });
});
