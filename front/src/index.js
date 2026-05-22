import React from 'react';
import ReactDOM from 'react-dom/client';
import reportWebVitals from './reportWebVitals';
import LoginScreen from './components/login';
import MainScreen from './components/main';
import { LANG_CONST } from './values/languageConstants';
import { authenticate, getCreds, setCreds, clearCreds, setToken, clearToken } from './api';

const t = LANG_CONST.ru;

// Build the user object stored in state: server-provided fields + token + the
// login we used (handy for displaying the username in the sidebar).
const buildUser = (token, userData, login) => ({ ...userData, token, login });

function App() {
  const [user, setUser]       = React.useState(null);
  const [booting, setBooting] = React.useState(true);

  // Auto-login from saved creds (if any).
  React.useEffect(() => {
    const c = getCreds();
    if (!c) { setBooting(false); return; }
    authenticate(c.login, c.password, c.role)
      .then(({ token: tok, userData }) => {
        setToken(tok);
        setUser(buildUser(tok, userData, c.login));
      })
      .catch(() => clearCreds())
      .finally(() => setBooting(false));
  }, []);

  const handleLogin = ({ token: tok, userData, role, login, password, remember }) => {
    setToken(tok);
    if (remember) setCreds({ login, password, role });
    else clearCreds();
    setUser(buildUser(tok, userData, login));
  };

  const handleLogout = () => {
    clearToken();
    clearCreds();
    setUser(null);
  };

  // Merge server-returned user fields into the current user state.
  const handleUserUpdate = (updated) =>
    setUser(prev => prev ? { ...prev, ...updated } : prev);

  if (booting) return null;

  return user
    ? <MainScreen t={t} user={user} onLogout={handleLogout} onUserUpdate={handleUserUpdate} />
    : <LoginScreen t={t} onLogin={handleLogin} />;
}

ReactDOM.createRoot(document.getElementById('root')).render(
  <React.StrictMode><App /></React.StrictMode>
);

reportWebVitals();
