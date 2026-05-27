import React from 'react';

// Single-field popup for spending product from a batch.
// User enters the amount to subtract; submit is blocked when value is
// missing, non-positive, or greater than `max` (the batch's current count).
export default function SpendPopup({ t, max, onCancel, onApply }) {
  const [value, setValue] = React.useState('');
  const [busy, setBusy] = React.useState(false);
  const num = Number(value);
  const valid = value !== '' && Number.isFinite(num) && num > 0 && num <= max;

  const submit = async () => {
    if (!valid || busy) return;
    setBusy(true);
    try { await onApply(num); }
    finally { setBusy(false); }
  };

  return (
    <div className="popup-scrim" onClick={onCancel}>
      <div className="popup" onClick={e => e.stopPropagation()}>
        <h2>{t.spend}</h2>
        <p className="popup-message">{t.spendHint} {max}</p>
        <div className="popup-fields">
          <div className="field">
            <label htmlFor="spend-count">{t.fields.count}</label>
            <input
              id="spend-count"
              type="number"
              min={1}
              max={max}
              value={value}
              autoFocus
              onChange={e => setValue(e.target.value)}
            />
          </div>
        </div>
        <div className="popup-actions">
          <button type="button" className="btn btn-ghost" onClick={onCancel} disabled={busy}>{t.cancel}</button>
          <button type="button" className="btn btn-primary" onClick={submit} disabled={!valid || busy}>
            {busy ? '…' : t.spend}
          </button>
        </div>
      </div>
    </div>
  );
}
