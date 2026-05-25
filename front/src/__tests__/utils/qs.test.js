import { qs } from '../../utils/qs';

test('empty / null input returns empty string', () => {
  expect(qs()).toBe('');
  expect(qs(null)).toBe('');
  expect(qs({})).toBe('');
});

test('skips null, undefined, and empty-string values', () => {
  expect(qs({ a: null, b: undefined, c: '' })).toBe('');
});

test('builds standard query string', () => {
  expect(qs({ page: 0, size: 10, name: 'john' })).toBe('?page=0&size=10&name=john');
});

test('keeps falsy values that are not null/empty (0, false)', () => {
  expect(qs({ page: 0, active: false })).toBe('?page=0&active=false');
});

test('repeats key for array values', () => {
  expect(qs({ ids: [1, 2, 3] })).toBe('?ids=1&ids=2&ids=3');
});

test('encodes special characters', () => {
  expect(qs({ q: 'hello world' })).toBe('?q=hello+world');
  expect(qs({ q: 'a&b' })).toBe('?q=a%26b');
});
