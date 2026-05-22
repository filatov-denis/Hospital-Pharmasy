import React from 'react';
import '../static/styles.css';
import Topbar from './topbar';
import { authenticate } from '../api';

function LoginScreen({ t, onLogin }) {
  const [role, setRole] = React.useState(localStorage.getItem("lastRole") || "ROLE_PHARMACIST");
  const [login, setLoginVal] = React.useState("");
  const [password, setPassword] = React.useState("");
  const [remember, setRemember] = React.useState(true);
  const [showPwd, setShowPwd] = React.useState(false);
  const [busy, setBusy] = React.useState(false);
  const [error, setError] = React.useState("");

  const roles = [
    { id: "ROLE_ADMIN", title: t.roleAdmin, desc: t.roleAdminDesc, glyph: t.roleAdminGlyph},
    { id: "ROLE_PHARMACIST", title: t.rolePharm, desc: t.rolePharmDesc, glyph: t.rolePharmGlyph},
    { id: "ROLE_NURSE", title: t.roleNurse, desc: t.roleNurseDesc, glyph: t.roleNurseGlyph},
  ];

  const handleSubmit = async (e) => {
    e.preventDefault();
    if (busy) return;
    setBusy(true); setError("");
    try {
      const { token, userData } = await authenticate(login.trim(), password, role);
      localStorage.setItem("lastRole", role);
      onLogin({ token, userData, role, login: login.trim(), password, remember });
    } catch (err) {
      setError(err.status === 401 || err.status === 403 ? "Неверный логин или пароль" : (err.message || "Ошибка входа"));
    } finally {
      setBusy(false);
    }
  };

  return (
    <div className="login-shell">
      <div className="login-bg" aria-hidden="true">
        <div className="login-bg-grid"></div>
        <div className="login-bg-glow"></div>
      </div>

      <Topbar t={t} />

      <main className="login-main">
        <section className="login-card" aria-labelledby="signin-title">
          <div className="login-card-head">
            <h1 id="signin-title">{t.signIn}</h1>
            <p>{t.signInHint}</p>
          </div>

          <form className="login-form" onSubmit={handleSubmit}>
            <fieldset className="role-group">
              <legend>{t.role}</legend>
              <div className="role-list">
                {roles.map((r) => (
                  <label key={r.id} className={`role-card ${role === r.id ? "selected" : ""}`}>
                    <input
                      type="radio"
                      name="role"
                      value={r.id}
                      checked={role === r.id}
                      onChange={() => setRole(r.id)}
                    />
                    <div className="role-glyph" aria-hidden="true">
                      {r.glyph}
                    </div>
                    <div className="role-text">
                      <div className="role-title">{r.title}</div>
                      <div className="role-desc">{r.desc}</div>
                    </div>
                    <div className="role-check" aria-hidden="true">
                      <svg viewBox="0 0 16 16" width="14" height="14">
                        <path d="M3 8.5l3 3 7-7" stroke="currentColor" strokeWidth="2" fill="none" strokeLinecap="round" strokeLinejoin="round"/>
                      </svg>
                    </div>
                  </label>
                ))}
              </div>
            </fieldset>

            <div className="field">
              <label htmlFor="login">{t.login}</label>
              <input
                id="login"
                type="text"
                value={login}
                onChange={(e) => setLoginVal(e.target.value)}
                placeholder={t.loginPh}
                autoComplete="username"
              />
            </div>

            <div className="field">
              <label htmlFor="pwd">{t.password}</label>
              <div className="pwd-wrap">
                <input
                  id="pwd"
                  type={showPwd ? "text" : "password"}
                  value={password}
                  onChange={(e) => setPassword(e.target.value)}
                  placeholder={t.pwdPh}
                  autoComplete="current-password"
                />
                <button type="button" className="pwd-toggle" onClick={() => setShowPwd(s => !s)} aria-label="toggle">
                  {showPwd ? (
                    <svg viewBox="0 0 20 20" width="16" height="16" fill="none" stroke="currentColor" strokeWidth="1.6">
                      <path d="M3 10s2.5-5 7-5 7 5 7 5-2.5 5-7 5-7-5-7-5z"/>
                      <circle cx="10" cy="10" r="2"/>
                      <path d="M3 3l14 14" strokeLinecap="round"/>
                    </svg>
                  ) : (
                    <svg viewBox="0 0 20 20" width="16" height="16" fill="none" stroke="currentColor" strokeWidth="1.6">
                      <path d="M3 10s2.5-5 7-5 7 5 7 5-2.5 5-7 5-7-5-7-5z"/>
                      <circle cx="10" cy="10" r="2.5"/>
                    </svg>
                  )}
                </button>
              </div>
            </div>

            <div className="field-row">
              <label className="check">
                <input type="checkbox" checked={remember} onChange={(e) => setRemember(e.target.checked)} />
                <span className="check-box" aria-hidden="true">
                  <svg viewBox="0 0 16 16" width="11" height="11">
                    <path d="M3 8.5l3 3 7-7" stroke="currentColor" strokeWidth="2.4" fill="none" strokeLinecap="round" strokeLinejoin="round"/>
                  </svg>
                </span>
                <span>{t.remember}</span>
              </label>
              <a href="#" className="link" onClick={(e) => e.preventDefault()}>{t.forgot}</a>
            </div>

            {error && <div className="popup-error" role="alert">{error}</div>}

            <button type="submit" className="btn btn-primary btn-block" disabled={busy}>
              {busy ? '…' : t.submit}
            </button>
          </form>

          <footer className="login-card-foot">
            <span>{t.needHelp}</span>
            <a href="#" className="link" onClick={(e) => e.preventDefault()}>{t.contactAdmin}</a>
          </footer>
        </section>
      </main>

      <footer className="login-foot">
        <span>© 2026 · {t.appName}</span>
      </footer>
    </div>
  );
}

export default LoginScreen
