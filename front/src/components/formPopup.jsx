import React from 'react';
import ComboBox from './comboBox';

// Reusable popup with a flat list of fields and Cancel / Apply buttons.
// Labels resolve from t.fields. Per-field widget via the `config` map:
//   { combo: '<entity>' }     -> ComboBox backed by GET /{entity}
//   { options: ['A', 'B'] }   -> ComboBox with hardcoded values
//   { type: '<html-type>' }   -> <input type="...">
//   undefined                 -> plain text input
export default function FormPopup({
  t, title, fields, initial, submitLabel, config = {}, onApply, onCancel,
}) {
  const [values, setValues] = React.useState(initial || {});
  const [error, setError]   = React.useState('');
  const [busy, setBusy]     = React.useState(false);
  const set = (k, v) => setValues(prev => ({ ...prev, [k]: v }));

  const submit = async () => {
    if (busy) return;
    setBusy(true); setError('');
    try {
      await onApply(values);
    } catch (e) {
      setError(e.message || t.error || 'Ошибка');
    } finally {
      setBusy(false);
    }
  };

  const renderField = (k) => {
    const cfg = config[k] || {};
    if (cfg.combo || cfg.options) {
      return (
        <ComboBox
          entity={cfg.combo}
          options={cfg.options}
          value={values[k]}
          onChange={v => set(k, v)}
          t={t}
        />
      );
    }
    return (
      <input
        id={`f-${k}`}
        type={cfg.type || 'text'}
        value={values[k] || ''}
        onChange={e => set(k, e.target.value)}
      />
    );
  };

  return (
    <div className="popup-scrim" onClick={onCancel}>
      <div className="popup" onClick={e => e.stopPropagation()}>
        <h2>{title}</h2>
        {fields.map(k => (
          <div key={k} className="field">
            <label htmlFor={`f-${k}`}>{t.fields[k] || k}</label>
            {renderField(k)}
          </div>
        ))}
        {error && <div className="popup-error" role="alert">{error}</div>}
        <div className="popup-actions">
          <button type="button" className="btn btn-ghost" onClick={onCancel} disabled={busy}>{t.cancel}</button>
          <button type="button" className="btn btn-primary" onClick={submit} disabled={busy}>
            {busy ? '…' : submitLabel}
          </button>
        </div>
      </div>
    </div>
  );
}
