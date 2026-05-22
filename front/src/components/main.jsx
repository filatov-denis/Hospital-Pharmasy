import React from 'react';
import '../static/styles.css';
import Topbar from './topbar';
import { SECTIONS_BY_ROLE, SECTION_ENTITY, ENTITY_FIELDS, ENTITY_FILTERS, ENTITY_CREATE_FIELDS, ENTITY_EDIT_FIELDS, FIELD_CONFIG } from '../values/sections';
import { getAll, getOne, send } from '../api';
import FormPopup from './formPopup';

const PROFILE_FIELDS = ['name', 'middlename', 'lastname', 'password'];
const PROFILE_CONFIG = { ...FIELD_CONFIG, password: { type: 'password' } };
// Same widget config as filters, plus password masking for the create-user form.
const CREATE_CONFIG  = { ...FIELD_CONFIG, password: { type: 'password' } };

const cell = (v, t) => {
  if (v == null) return '—';
  if (typeof v === 'object') return JSON.stringify(v);
  return t.values[String(v)] || String(v);
};

export default function MainScreen({ t, user, onLogout, onUserUpdate }) {
  const sections = SECTIONS_BY_ROLE[user.role] || [];
  const [active, setActive] = React.useState(sections[0]);
  const label = (id) => t.sections[id] || id;

  const entity = SECTION_ENTITY[active];
  const [rows, setRows] = React.useState([]);
  const [status, setStatus] = React.useState('');   // '', 'loading', error message
  const [filters, setFilters] = React.useState({});
  const [filterOpen, setFilterOpen] = React.useState(false);
  const [profileOpen, setProfileOpen] = React.useState(false);
  const [addOpen, setAddOpen] = React.useState(false);
  const [editValues, setEditValues] = React.useState(null);
  const [refresh, setRefresh] = React.useState(0);

  const saveProfile = async (values) => {
    const updated = await send('user', 'PUT', { id: user.id, ...values });
    if (updated && onUserUpdate) onUserUpdate(updated);
    setProfileOpen(false);
    if (entity === 'user') setRefresh(r => r + 1);
  };

  const addEntity = async (values) => {
    await send(entity, 'POST', values);
    setAddOpen(false);
    setRefresh(r => r + 1);
  };

  const startEdit = async (row) => {
    try {
      const full = await getOne(entity, row.id);
      setEditValues({ ...row, ...full });
    } catch (e) {
      alert(e.message || 'Ошибка');         // not a popup-driven action; alert is fine here
    }
  };

  const saveEdit = async (values) => {
    await send(entity, 'PUT', { id: editValues.id, ...values });
    setEditValues(null);
    setRefresh(r => r + 1);
  };

  React.useEffect(() => { setFilters({}); }, [entity]);   // reset filters on section change

  React.useEffect(() => {
    if (!entity) { setRows([]); setStatus(''); return; }
    let alive = true;
    setStatus('loading');
    getAll(entity, { page: 0, size: 50, ...filters })
      .then(res => { if (alive) { setRows(res.content || res || []); setStatus(''); } })
      .catch(err => { if (alive) { setRows([]); setStatus(err.message || 'Ошибка'); } });
    return () => { alive = false; };
  }, [entity, filters, refresh]);

  const cols = ENTITY_FIELDS[entity] || [];
  const filterFields = ENTITY_FILTERS[entity] || [];
  const createFields = ENTITY_CREATE_FIELDS[entity] || [];
  const editFields   = ENTITY_EDIT_FIELDS[entity] || [];
  const editable     = editFields.length > 0;
  const colSpan      = Math.max(cols.length + (editable ? 1 : 0), 1);

  const fullName = [user.middlename, user.name, user.lastname].filter(Boolean).join(' ');
  const initial = (user.name || user.middlename || '?').charAt(0).toUpperCase();

  return (
    <div className="app-shell">
      <aside className="sidebar">
        <Topbar t={t} />
        <nav className="sidebar-nav">
          {sections.map(id => (
            <button
              key={id}
              type="button"
              className={`nav-item ${active === id ? 'active' : ''}`}
              onClick={() => setActive(id)}
            >
              <span>{label(id)}</span>
            </button>
          ))}
        </nav>
        <div className="sidebar-foot">
          <button type="button" className="user-chip" onClick={() => setProfileOpen(true)}>
            <div className="avatar" aria-hidden="true">{initial}</div>
            <div className="user-text">
              <div className="user-name">{fullName}</div>
              <div className="user-role">{user.role}</div>
            </div>
          </button>
        </div>
      </aside>

      <div className="main-col">
        <header className="topbar">
          <div className="page-title"><h1>{active ? label(active) : ''}</h1></div>
          <div className="topbar-actions">
            <button type="button" className="btn btn-ghost" onClick={onLogout}>{t.logout}</button>
          </div>
        </header>

        <div className="content">
          {(filterFields.length > 0 || createFields.length > 0) && (
            <div style={{ display: 'flex', gap: 8 }}>
              {filterFields.length > 0 && (
                <button type="button" className="btn btn-ghost" onClick={() => setFilterOpen(true)}>{t.filters}</button>
              )}
              {createFields.length > 0 && (
                <button type="button" className="btn btn-primary" onClick={() => setAddOpen(true)}>{t.add}</button>
              )}
            </div>
          )}
          <br/>
          <div className="table-wrap">
            <table className="data-table">
              <thead>
                <tr>
                  {cols.map(c => <th key={c}>{t.fields[c] || c}</th>)}
                  {editable && <th style={{ width: 36 }} aria-label={t.edit}></th>}
                </tr>
              </thead>
              <tbody>
                {status && (
                  <tr><td colSpan={colSpan} style={{ textAlign: 'center', color: 'var(--ink-3)' }}>
                    {status === 'loading' ? t.loading : status}
                  </td></tr>
                )}
                {!status && rows.map((row, i) => (
                  <tr key={row.id ?? i}>
                    {cols.map(c => <td key={c}>{cell(row[c], t)}</td>)}
                    {editable && (
                      <td style={{ textAlign: 'right' }}>
                        <button type="button" className="icon-btn" title={t.edit} onClick={() => startEdit(row)}>
                          <svg viewBox="0 0 16 16" width="14" height="14" fill="none" stroke="currentColor" strokeWidth="1.6" strokeLinecap="round" strokeLinejoin="round">
                            <path d="M11.5 1.5l3 3-9 9H2.5v-3l9-9z"/>
                            <path d="M9.5 3.5l3 3"/>
                          </svg>
                        </button>
                      </td>
                    )}
                  </tr>
                ))}
                {!status && !rows.length && (
                  <tr><td colSpan={colSpan} style={{ textAlign: 'center', color: 'var(--ink-3)' }}>{t.noData}</td></tr>
                )}
              </tbody>
            </table>
          </div>
        </div>
      </div>

      {filterOpen && (
        <FormPopup
          t={t}
          title={t.filters}
          fields={filterFields}
          initial={filters}
          config={FIELD_CONFIG}
          submitLabel={t.apply}
          onCancel={() => setFilterOpen(false)}
          onApply={(v) => { setFilters(v); setFilterOpen(false); }}
        />
      )}

      {profileOpen && (
        <FormPopup
          t={t}
          title={t.profile}
          fields={PROFILE_FIELDS}
          initial={{ name: user.name, middlename: user.middlename, lastname: user.lastname }}
          config={PROFILE_CONFIG}
          submitLabel={t.save}
          onCancel={() => setProfileOpen(false)}
          onApply={saveProfile}
        />
      )}

      {addOpen && (
        <FormPopup
          t={t}
          title={t.add}
          fields={createFields}
          initial={{}}
          config={CREATE_CONFIG}
          submitLabel={t.add}
          onCancel={() => setAddOpen(false)}
          onApply={addEntity}
        />
      )}

      {editValues && (
        <FormPopup
          t={t}
          title={t.edit}
          fields={editFields}
          initial={editValues}
          config={CREATE_CONFIG}
          submitLabel={t.save}
          onCancel={() => setEditValues(null)}
          onApply={saveEdit}
        />
      )}
    </div>
  );
}
