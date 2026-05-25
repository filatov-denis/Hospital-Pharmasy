// Flatten nested objects into a single-level map keyed by dotted paths.
// { count: 13, product: { name: 'X' } } -> { count: 13, 'product.name': 'X' }
// Arrays and primitives are kept as-is.
export const flatten = (obj, prefix = '') => {
  const out = {};
  for (const [k, v] of Object.entries(obj || {})) {
    const key = prefix ? `${prefix}.${k}` : k;
    if (v && typeof v === 'object' && !Array.isArray(v)) {
      Object.assign(out, flatten(v, key));
    } else {
      out[key] = v;
    }
  }
  return out;
};
