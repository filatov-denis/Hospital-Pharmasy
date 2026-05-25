import React from 'react';
import { render, screen, fireEvent } from '@testing-library/react';
import ComboBox from '../../components/comboBox';

const t = {
  values: { true: 'Да', false: 'Нет' },
  noData: 'Нет данных',
  search: '',
};

describe('ComboBox — static options (YES_NO)', () => {
  test('opens on click and lists localized options', () => {
    render(<ComboBox options={[true, false]} value={null} onChange={() => {}} t={t} />);
    fireEvent.click(screen.getByRole('textbox'));
    expect(screen.getByText('Да')).toBeInTheDocument();
    expect(screen.getByText('Нет')).toBeInTheDocument();
  });

  test('emits boolean false when "Нет" is picked (regression for ?? vs ||)', () => {
    const onChange = jest.fn();
    render(<ComboBox options={[true, false]} value={null} onChange={onChange} t={t} />);
    fireEvent.click(screen.getByRole('textbox'));
    fireEvent.mouseDown(screen.getByText('Нет'));
    expect(onChange).toHaveBeenCalledWith(false);
  });

  test('input is readOnly in static mode (no free typing)', () => {
    render(<ComboBox options={[true, false]} value={null} onChange={() => {}} t={t} />);
    expect(screen.getByRole('textbox')).toHaveAttribute('readonly');
  });

  test('pre-set value shows its localized label', () => {
    render(<ComboBox options={[true, false]} value={true} onChange={() => {}} t={t} />);
    expect(screen.getByRole('textbox')).toHaveValue('Да');
  });
});

describe('ComboBox — entity mode (country)', () => {
  beforeEach(() => {
    global.fetch = jest.fn(() => Promise.resolve({
      ok: true,
      status: 200,
      text: () => Promise.resolve(JSON.stringify({ content: [
        { id: 1, name: 'Андорра' },
        { id: 2, name: 'Антигуа' },
      ] })),
    }));
  });

  test('input is editable (typeahead mode)', () => {
    render(<ComboBox entity="country" value={null} onChange={() => {}} t={t} />);
    expect(screen.getByRole('textbox')).not.toHaveAttribute('readonly');
  });
});
