import { getValue } from '../../utils/getValue';

test('top-level key', () => {
  expect(getValue({ a: 1 }, 'a')).toBe(1);
});

test('nested dotted path', () => {
  expect(getValue({ a: { b: { c: 42 } } }, 'a.b.c')).toBe(42);
});

test('missing leaf returns undefined', () => {
  expect(getValue({ a: { b: 1 } }, 'a.c')).toBeUndefined();
});

test('null along the path is safe (no throw)', () => {
  expect(getValue({ a: null }, 'a.b.c')).toBeNull();
  expect(getValue(null, 'a.b')).toBeNull();
});

test('keeps falsy leaf values', () => {
  expect(getValue({ a: 0 }, 'a')).toBe(0);
  expect(getValue({ a: false }, 'a')).toBe(false);
  expect(getValue({ a: '' }, 'a')).toBe('');
});
