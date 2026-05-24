import React from 'react';
import '../static/styles.css';
import Topbar from './topbar';
import { SECTIONS_BY_ROLE, SECTION_ENTITY, ENTITY_FIELDS, ENTITY_FILTERS, ENTITY_CREATE_FIELDS, ENTITY_EDIT_FIELDS, ENTITY_DELETABLE, FIELD_CONFIG, canDo } from '../values/sections';
import { getAll, getOne, send } from '../api';
import FormPopup from './formPopup';
import ConfirmPopup from './confirmPopup';
import Histogram from './histogram';

const PROFILE_FIELDS = ['name', 'middlename', 'lastname', 'password'];
const PROFILE_CONFIG = { ...FIELD_CONFIG, password: { type: 'password' } };
// Same widget config as filters, plus password masking for the create-user form.
const CREATE_CONFIG  = { ...FIELD_CONFIG, password: { type: 'password' } };

// Allowed next-states per current request status. Terminal states (COMPLETED, CANCELLED)
// are intentionally absent — no further transitions.
const STATUS_TRANSITIONS = {
  CREATED:   ['CONFIRMED', 'CANCELLED'],
  CONFIRMED: ['DELIVERING', 'CANCELLED'],
  DELIVERING: ['COMPLETED', 'CANCELLED'],
};

// Resolve a (possibly dotted) path against an object: 'product.name' -> obj.product?.name
const getValue = (obj, path) =>
  path.split('.').reduce((o, k) => (o == null ? o : o[k]), obj);

// For sections that don't list under the default /<entity> URL.
// deptStorages -> GET /storage/{user.linkedStorageId}  (single-object response, wrapped to 1 row)
const listPathFor = (activeId, entity, user) => {
  if (activeId === 'deptStorages') return `storage/${user.linkedStorageId ?? ''}`;
  return entity;
};

// Flatten nested objects into a single-level map keyed by dotted paths.
// { count: 13, product: { name: 'X' } } -> { count: 13, 'product.name': 'X' }
// Arrays and primitives are kept as-is.
const flatten = (obj, prefix = '') => {
  const out = {};
  for (const [k, v] of Object.entries(obj || {})) {
    const key = prefix ? `${prefix}.${k}` : k;
    if (key === 'product.id')
      continue;
    if (v && typeof v === 'object' && !Array.isArray(v)) Object.assign(out, flatten(v, key));
    else out[key] = v;
  }
  return out;
};

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
  const [viewValues, setViewValues] = React.useState(null);
  const [pendingDelete, setPendingDelete] = React.useState(null);
  const [histogram, setHistogram] = React.useState(null);
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
      const full = await getOne(entity, row.id || row.name);
      setEditValues({ ...row, ...full });
    } catch (e) {
      alert(e.message || 'Ошибка');         // not a popup-driven action; alert is fine here
    }
  };

  const startView = async (row) => {
    try {
      const full = await getOne(entity, row.id);
      setViewValues(flatten({ ...row, ...full }));
    } catch (e) {
      alert(e.message || 'Ошибка');
    }
  };

  const confirmDelete = async () => {
    if (!pendingDelete) return;
    try {
      await send(entity, 'DELETE', null, pendingDelete.id);
      setPendingDelete(null);
      setRefresh(r => r + 1);
    } catch (e) {
      setPendingDelete(null);
      alert(e.message || 'Ошибка');
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
    if (active === 'deptStorages' && !user.linkedStorageId) {
      setRows([]); setHistogram(null);
      setStatus(t.noLinkedStorage);
      return;
    }
    let alive = true;
    setStatus('loading');
    getAll(listPathFor(active, entity, user), { page: 0, size: 50, ...filters })
      .then(res => {
        if (!alive) return;
        let arr;
        if (!res) arr = [];
        else if (Array.isArray(res)) arr = res;
        else if (res.content) arr = res.content;
        else if (res.lines) arr = res.lines;
        else if (typeof res === 'object') arr = [res];   // single object (e.g. GET /storage/{id})
        else arr = [];
        setRows(arr);
        setHistogram(res && !Array.isArray(res) ? (res.histogram || null) : null);
        setStatus('');
      })
      .catch(err => { if (alive) { setRows([]); setHistogram(null); setStatus(err.message || 'Ошибка'); } });
    return () => { alive = false; };
  }, [active, entity, filters, refresh, user, t]);

  const cols = ENTITY_FIELDS[entity] || [];
  const filterFields = ENTITY_FILTERS[entity] || [];
  const createFields = ENTITY_CREATE_FIELDS[entity] || [];
  const editFields   = ENTITY_EDIT_FIELDS[entity] || [];
  const canCreate    = createFields.length > 0  && canDo(entity, 'create', user.role);
  const editable     = editFields.length > 0    && canDo(entity, 'edit',   user.role);
  const deletable    = ENTITY_DELETABLE.has(entity) && canDo(entity, 'delete', user.role);
  const isAnalytics  = entity === 'request/analytics';
  const hasActions   = !isAnalytics;   // analytics has no per-row actions
  const colSpan      = Math.max(cols.length + (hasActions ? 1 : 0), 1);
  const actionsWidth = 36 + (editable ? 36 : 0) + (deletable ? 36 : 0);

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
              <div className="user-role">{t.values[user.role] || user.role}</div>
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
          {(filterFields.length > 0 || canCreate || isAnalytics) && (
            <div style={{ display: 'flex', gap: 8 }}>
              {filterFields.length > 0 && (
                <button type="button" className="btn btn-ghost" onClick={() => setFilterOpen(true)}>{t.filters}</button>
              )}
              {canCreate && (
                <button type="button" className="btn btn-primary" onClick={() => setAddOpen(true)}>{t.add}</button>
              )}
              {isAnalytics && (
                <button type="button" className="btn btn-primary" onClick={() => { /* TODO: print */ }}>{t.print}</button>
              )}
            </div>
          )}
          <br/>
          <div className="table-wrap">
            <table className="data-table">
              <thead>
                <tr>
                  {cols.map(c => <th key={c}>{t.fields[c] || c}</th>)}
                  {hasActions && <th style={{ width: actionsWidth }}></th>}
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
                    {cols.map(c => <td key={c}>{cell(getValue(row, c), t)}</td>)}
                    {hasActions && (
                      <td style={{ textAlign: 'right', whiteSpace: 'nowrap' }}>
                        <button type="button" className="icon-btn" title={t.view} onClick={() => startView(row)}>
                          <svg viewBox="0 0 16 16" width="14" height="14" fill="none" stroke="currentColor" strokeWidth="1.6" strokeLinecap="round" strokeLinejoin="round">
                            <circle cx="7" cy="7" r="5"/>
                            <path d="M11 11l3.5 3.5"/>
                          </svg>
                        </button>
                        {editable && (
                          <button type="button" className="icon-btn" title={t.edit} onClick={() => startEdit(row)}>
                            <svg viewBox="0 0 16 16" width="14" height="14" fill="none" stroke="currentColor" strokeWidth="1.6" strokeLinecap="round" strokeLinejoin="round">
                              <path d="M11.5 1.5l3 3-9 9H2.5v-3l9-9z"/>
                              <path d="M9.5 3.5l3 3"/>
                            </svg>
                          </button>
                        )}
                        {deletable && (
                          <button type="button" className="icon-btn icon-btn-danger" title={t.delete} onClick={() => setPendingDelete(row)}>
                            <svg viewBox="0 0 16 16" width="14" height="14" fill="none" stroke="currentColor" strokeWidth="1.6" strokeLinecap="round" strokeLinejoin="round">
                              <path d="M3 4h10"/>
                              <path d="M6 4V2.5h4V4"/>
                              <path d="M4.5 4l.7 9.5a1 1 0 0 0 1 .9h3.6a1 1 0 0 0 1-.9L11.5 4"/>
                            </svg>
                          </button>
                        )}
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

          {isAnalytics && histogram && (
            <Histogram data={histogram} title={t.histogram} />
          )}
        </div>
      </div>

      {filterOpen && (
        <FormPopup
          t={t}
          title={t.filters}
          fields={filterFields}
          initial={{}}
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
          config={
            entity === 'request'
              ? { ...CREATE_CONFIG, status: { options: STATUS_TRANSITIONS[editValues.status] || [] } }
              : CREATE_CONFIG
          }
          submitLabel={t.save}
          onCancel={() => setEditValues(null)}
          onApply={saveEdit}
        />
      )}

      {viewValues && (
        <FormPopup
          t={t}
          title={t.view}
          fields={Object.keys(viewValues).filter(k => !/(^|\.)(image_?id)$/i.test(k))}
          initial={viewValues}
          readOnly
          onCancel={() => setViewValues(null)}
        />
      )}

      {pendingDelete && (
        <ConfirmPopup
          title={t.delete}
          message={t.confirmDelete}
          confirmLabel={t.delete}
          cancelLabel={t.cancel}
          danger
          onConfirm={confirmDelete}
          onCancel={() => setPendingDelete(null)}
        />
      )}
    </div>
  );
}
