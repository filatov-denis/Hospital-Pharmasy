// Resolve a (possibly dotted) path against an object: 'product.name' -> obj.product?.name
export const getValue = (obj, path) =>
  path.split('.').reduce((o, k) => (o == null ? o : o[k]), obj);
