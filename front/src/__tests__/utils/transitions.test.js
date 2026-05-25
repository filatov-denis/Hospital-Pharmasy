import { nextStates } from '../../utils/transitions';

test.each([
  ['CREATED',    ['CONFIRMED', 'CANCELLED']],
  ['CONFIRMED',  ['DELIVERING', 'CANCELLED']],
  ['DELIVERING', ['COMPLETED', 'CANCELLED']],
])('non-terminal: %s -> %p', (from, expected) => {
  expect(nextStates(from)).toEqual(expected);
});

test.each([
  'COMPLETED',
  'CANCELLED',
  'UNKNOWN',
  undefined,
  null,
])('terminal/unknown returns []: %p', (from) => {
  expect(nextStates(from)).toEqual([]);
});
