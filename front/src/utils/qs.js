// Flat object -> ?a=1&b=2 (skips null/empty, supports arrays).
export const qs = (params) => {
  const u = new URLSearchParams();
  Object.entries(params || {}).forEach(([k, v]) => {
    if (v == null || v === '') return;
    if (Array.isArray(v)) v.forEach(x => u.append(k, x));
    else u.append(k, v);
  });
  const s = u.toString();
  return s ? `?${s}` : '';
};
