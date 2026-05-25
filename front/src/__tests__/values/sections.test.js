import { canDo, ENTITY_LABEL, ENTITY_DROPDOWN } from '../../values/sections';

describe('canDo', () => {
  test('admin can delete users', () => {
    expect(canDo('user', 'delete', 'ROLE_ADMIN')).toBe(true);
  });

  test('nurse cannot delete users', () => {
    expect(canDo('user', 'delete', 'ROLE_NURSE')).toBe(false);
  });

  test('nurse can create requests', () => {
    expect(canDo('request', 'create', 'ROLE_NURSE')).toBe(true);
  });

  test('pharmacist can edit batches', () => {
    expect(canDo('batch', 'edit', 'ROLE_PHARMACIST')).toBe(true);
  });

  test('unknown entity returns false', () => {
    expect(canDo('totally-fake', 'create', 'ROLE_ADMIN')).toBe(false);
  });

  test('unknown action returns false', () => {
    expect(canDo('user', 'merge', 'ROLE_ADMIN')).toBe(false);
  });
});

describe('ENTITY_LABEL.batch', () => {
  test('formats with product name • date • count', () => {
    const item = {
      id: 21,
      count: 13,
      manufactureDate: '2026-05-19',
      product: { name: 'Glicerol' },
    };
    expect(ENTITY_LABEL.batch(item)).toBe('21 • Glicerol • 2026-05-19 • 13');
  });

  test('omits missing fields', () => {
    const item = { id: 7, count: 5, manufactureDate: null, product: { name: 'X' } };
    expect(ENTITY_LABEL.batch(item)).toBe('7 • X • 5');
  });
});

describe('ENTITY_DROPDOWN', () => {
  test('batch is configured as dropdown', () => {
    expect(ENTITY_DROPDOWN.has('batch')).toBe(true);
  });

  test('country is not (still typeahead)', () => {
    expect(ENTITY_DROPDOWN.has('country')).toBe(false);
  });
});
