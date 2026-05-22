import React from 'react';

// Stylised confirmation dialog. Same chrome as FormPopup (scrim + card + actions),
// but with no inputs — just a message and Cancel / Confirm buttons.
export default function ConfirmPopup({
  title, message, confirmLabel, cancelLabel, onConfirm, onCancel, danger = false,
}) {
  const [busy, setBusy] = React.useState(false);

  const confirm = async () => {
    if (busy) return;
    setBusy(true);
    try { await onConfirm(); }
    finally { setBusy(false); }
  };

  return (
    <div className="popup-scrim" onClick={onCancel}>
      <div className="popup" onClick={e => e.stopPropagation()}>
        <h2>{title}</h2>
        <p className="popup-message">{message}</p>
        <div className="popup-actions">
          <button type="button" className="btn btn-ghost" onClick={onCancel} disabled={busy}>{cancelLabel}</button>
          <button
            type="button"
            className={`btn ${danger ? 'btn-danger' : 'btn-primary'}`}
            onClick={confirm}
            disabled={busy}
          >
            {busy ? '…' : confirmLabel}
          </button>
        </div>
      </div>
    </div>
  );
}
