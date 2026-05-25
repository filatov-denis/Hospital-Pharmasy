import { flatten } from '../../utils/flatten';

test('shallow object passes through', () => {
  expect(flatten({ a: 1, b: 'x' })).toEqual({ a: 1, b: 'x' });
});

test('flattens nested object with dotted keys', () => {
  expect(flatten({ a: 1, b: { c: 2, d: { e: 3 } } }))
    .toEqual({ a: 1, 'b.c': 2, 'b.d.e': 3 });
});

test('arrays are kept as-is, not recursed into', () => {
  const out = flatten({ xs: [1, 2, 3], nested: { ys: [4, 5] } });
  expect(out).toEqual({ xs: [1, 2, 3], 'nested.ys': [4, 5] });
});

test('handles null / undefined input', () => {
  expect(flatten(null)).toEqual({});
  expect(flatten(undefined)).toEqual({});
});

test('keeps null leaf values', () => {
  expect(flatten({ a: null, b: { c: null } })).toEqual({ a: null, 'b.c': null });
});

test('typical batch response shape', () => {
  const input = {
    id: 21,
    count: 13,
    manufactureDate: '2026-05-19',
    expirationDate: '2026-05-31',
    product: { id: 1, name: 'Glicerol', countryName: 'Андорра' },
  };
  expect(flatten(input)).toEqual({
    id: 21,
    count: 13,
    manufactureDate: '2026-05-19',
    expirationDate: '2026-05-31',
    'product.id': 1,
    'product.name': 'Glicerol',
    'product.countryName': 'Андорра',
  });
});
