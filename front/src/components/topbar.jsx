import React from 'react';

export default function Topbar({ t }) {
  return (
    <header className="login-topbar">
      <div className="brand">
        <div className="brand-mark" aria-hidden="true">
          <svg viewBox="0 0 32 32" width="22" height="22">
            <circle cx="16" cy="16" r="13" fill="none" stroke="currentColor" strokeWidth="1.5" opacity=".35"/>
            <path d="M16 8v16M8 16h16" stroke="currentColor" strokeWidth="3" strokeLinecap="round"/>
          </svg>
        </div>
        <div className="brand-text">
          <div className="brand-name">{t.appName}</div>
          <div className="brand-hint">{t.appHint}</div>
        </div>
      </div>
    </header>
  );
}
