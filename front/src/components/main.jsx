import React from 'react';
import '../static/styles.css';
import Topbar from './topbar';
import { SECTIONS_BY_ROLE, SECTION_ENTITY, ENTITY_FIELDS, ENTITY_FILTERS, ENTITY_CREATE_FIELDS, ENTITY_EDIT_FIELDS, ENTITY_DELETABLE, ENTITY_LABEL, FIELD_CONFIG, canDo } from '../values/sections';
import { getAll, getOne, send } from '../api';
import FormPopup from './formPopup';
import ConfirmPopup from './confirmPopup';
import SpendPopup from './spendPopup';
import Histogram from './histogram';
import ComboBox from './comboBox';
import { getValue } from '../utils/getValue';
import { flatten } from '../utils/flatten';
import { nextStates } from '../utils/transitions';

// View-popup filter: drop image ids, nested *.id pks (e.g. product.id, requestBatch.product.id),
// and any *.batches / batches collection (storage view dumps its full batch list otherwise).
const VIEW_SKIP = /(^|\.)(image_?id|batches)$|\.id$/i;

const PROFILE_FIELDS = ['name', 'middlename', 'lastname', 'password'];
const PROFILE_CONFIG = { ...FIELD_CONFIG, password: { type: 'password' } };
// Same widget config as filters, plus password masking for the create-user form.
const CREATE_CONFIG  = { ...FIELD_CONFIG, password: { type: 'password' } };

// For sections that don't list under the default /<entity> URL.
//   deptStorages     -> GET /storage/{user.linkedStorageId}   (single object, rows come from res.batches)
//   batchesByStorage -> 'main' -> GET /batch/main (paged), else GET /storage/{id} (rows from res.batches)
const listPathFor = (activeId, entity, user, selectedStorageId) => {
  if (activeId === 'deptStorages') return `storage/${user.linkedStorageId ?? ''}`;
  if (activeId === 'batchesByStorage') {
    return selectedStorageId === 'main' ? 'batch/main' : `storage/${selectedStorageId ?? ''}`;
  }
  return entity;
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
  const [spendBatch, setSpendBatch] = React.useState(null);
  const [histogram, setHistogram] = React.useState(null);
  const [selectedStorageId, setSelectedStorageId] = React.useState(null);
  const [linkedStorageName, setLinkedStorageName] = React.useState(null);
  const [nurseBatchOpts, setNurseBatchOpts] = React.useState(null);
  const [refresh, setRefresh] = React.useState(0);

  const isNurse = user.role === 'ROLE_NURSE';
  const isNurseRequestAdd = addOpen && entity === 'request' && isNurse;

  // Nurse-only request creation: source = /batch/main, target = batches inside her own storage.
  React.useEffect(() => {
    if (!isNurseRequestAdd) { setNurseBatchOpts(null); return; }
    let alive = true;
    const fmt = (arr) => (arr || []).map(b => ({ id: b.id, name: ENTITY_LABEL.batch(b) }));
    Promise.all([
      getAll('batch/main', { page: 0, size: 100 }).catch(() => ({})),
      user.linkedStorageId ? getOne('storage', user.linkedStorageId).catch(() => ({})) : Promise.resolve({}),
    ]).then(([mainRes, ownRes]) => {
      if (!alive) return;
      const mainList = mainRes.content || (Array.isArray(mainRes) ? mainRes : []);
      setNurseBatchOpts({ source: fmt(mainList), target: fmt(ownRes.batches) });
    });
    return () => { alive = false; };
  }, [isNurseRequestAdd, user.linkedStorageId]);

  // Resolve the user's linked storage name once (used as a label in the dropdown).
  React.useEffect(() => {
    if (!user.linkedStorageId) { setLinkedStorageName(null); return; }
    getOne('storage', user.linkedStorageId)
      .then(s => setLinkedStorageName(s && s.name))
      .catch(() => setLinkedStorageName(null));
  }, [user.linkedStorageId]);

  // Two-item dropdown: main storage + the user's own (when present).
  const storageOptions = React.useMemo(() => {
    const opts = [{ id: 'main', name: t.mainStorageOpt }];
    if (user.linkedStorageId) {
      opts.push({
        id: user.linkedStorageId,
        name: linkedStorageName || `${t.storage} #${user.linkedStorageId}`,
      });
    }
    return opts;
  }, [user.linkedStorageId, linkedStorageName, t]);

  // Default: own storage if present, otherwise main.
  React.useEffect(() => {
    if (active === 'batchesByStorage') {
      setSelectedStorageId(user.linkedStorageId ?? 'main');
    } else {
      setSelectedStorageId(null);
    }
  }, [active, user.linkedStorageId]);

  const saveProfile = async (values) => {
    const updated = await send('user', 'PUT', { id: user.id, ...values });
    if (updated && onUserUpdate) onUserUpdate(updated);
    setProfileOpen(false);
    if (entity === 'user') setRefresh(r => r + 1);
  };

  const addEntity = async (values) => {
    const payload = isNurseRequestAdd && user.linkedStorageId
      ? { ...values, targetStorageId: user.linkedStorageId }
      : values;
    await send(entity, 'POST', payload);
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

  const submitSpend = async (amount) => {
    await send('batch', 'PUT', { id: spendBatch.id, count: spendBatch.count - amount });
    setSpendBatch(null);
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
    if (active === 'batchesByStorage' && !selectedStorageId) {
      setRows([]); setHistogram(null);
      setStatus(t.pickStorage);
      return;
    }
    let alive = true;
    setStatus('loading');
    getAll(listPathFor(active, entity, user, selectedStorageId), { page: 0, size: 50, ...filters })
      .then(res => {
        if (!alive) return;
        let arr;
        if (!res) arr = [];
        else if (Array.isArray(res)) arr = res;
        else if (res.content) arr = res.content;
        else if (res.lines) arr = res.lines;
        else if (active === 'batchesByStorage' && Array.isArray(res.batches)) arr = res.batches;
        else if (typeof res === 'object') arr = [res];   // single object (e.g. GET /storage/{id})
        else arr = [];
        setRows(arr);
        setHistogram(res && !Array.isArray(res) ? (res.histogram || null) : null);
        setStatus('');
      })
      .catch(err => { if (alive) { setRows([]); setHistogram(null); setStatus(err.message || 'Ошибка'); } });
    return () => { alive = false; };
  }, [active, entity, filters, refresh, user, t, selectedStorageId]);

  const cols = ENTITY_FIELDS[entity] || [];
  // Drop filter fields the current role shouldn't see.
  // Nurse only ever sees her own requests, so the "creator" filter is meaningless for her.
  const filterFields = (ENTITY_FILTERS[entity] || []).filter(f =>
    !(entity === 'request' && f === 'creatorId' && user.role === 'ROLE_NURSE')
  );
  const createFieldsRaw = ENTITY_CREATE_FIELDS[entity] || [];
  // Nurse-only: hide targetStorageId (auto-filled to linkedStorageId on submit) and swap
  // both batch fields to static-option ComboBoxes backed by /batch/main + /storage/{id}.
  const createFields = isNurseRequestAdd
    ? createFieldsRaw.filter(f => f !== 'targetStorageId')
    : createFieldsRaw;
  const createConfig = isNurseRequestAdd && nurseBatchOpts
    ? { ...CREATE_CONFIG,
        sourceBatchId: { options: nurseBatchOpts.source },
        targetBatchId: { options: nurseBatchOpts.target } }
    : CREATE_CONFIG;
  const editFields   = ENTITY_EDIT_FIELDS[entity] || [];
  const canCreate    = createFields.length > 0  && canDo(entity, 'create', user.role);
  const editable     = editFields.length > 0    && canDo(entity, 'edit',   user.role);
  const deletable    = ENTITY_DELETABLE.has(entity) && canDo(entity, 'delete', user.role);
  const isAnalytics      = entity === 'request/analytics';
  const isBatchByStorage = active === 'batchesByStorage';
  // Spend is only meaningful for batches inside a real department storage (not the main pharmacy view).
  const canSpend         = isBatchByStorage && selectedStorageId != null && selectedStorageId !== 'main';
  const hasActions       = !isAnalytics;   // analytics has no per-row actions
  // batchesByStorage is read-only at section level: no filter, no add. Edit/delete per row stay.
  const showFilter       = !isBatchByStorage && filterFields.length > 0;
  const showAdd          = !isBatchByStorage && canCreate;
  const colSpan      = Math.max(cols.length + (hasActions ? 1 : 0), 1);
  const actionsWidth = 36 + (canSpend ? 36 : 0) + (editable ? 36 : 0) + (deletable ? 36 : 0);

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
          {(showFilter || showAdd || isAnalytics || isBatchByStorage) && (
            <div style={{ display: 'flex', gap: 8, alignItems: 'center' }}>
              {showFilter && (
                <button type="button" className="btn btn-ghost" onClick={() => setFilterOpen(true)}>{t.filters}</button>
              )}
              {showAdd && (
                <button type="button" className="btn btn-primary" onClick={() => setAddOpen(true)}>{t.add}</button>
              )}
              {isAnalytics && (
                <button type="button" className="btn btn-primary" onClick={() => window.print()}>{t.print}</button>
              )}
              {isBatchByStorage && (
                <div style={{ minWidth: 260, flex: '0 0 auto' }}>
                  <ComboBox
                    options={storageOptions}
                    value={selectedStorageId}
                    onChange={setSelectedStorageId}
                    t={t}
                  />
                </div>
              )}
            </div>
          )}
          <br/>
          <div className="print-area">
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
                        {canSpend && (
                          <button type="button" className="icon-btn" title={t.spend} onClick={() => setSpendBatch(row)}>
                            <svg viewBox="0 0 16 16" width="14" height="14">
                              <path d="M3 8.5l3 3 7-7" stroke="currentColor" strokeWidth="2" fill="none" strokeLinecap="round" strokeLinejoin="round"/>
                            </svg>
                          </button>
                        )}
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

      {addOpen && (!isNurseRequestAdd || nurseBatchOpts) && (
        <FormPopup
          t={t}
          title={t.add}
          fields={createFields}
          initial={{}}
          config={createConfig}
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
              ? { ...CREATE_CONFIG, status: { options: nextStates(editValues.status) } }
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
          fields={Object.keys(viewValues).filter(k => !VIEW_SKIP.test(k))}
          initial={viewValues}
          readOnly
          onCancel={() => setViewValues(null)}
        />
      )}

      {spendBatch && (
        <SpendPopup
          t={t}
          max={spendBatch.count}
          onCancel={() => setSpendBatch(null)}
          onApply={submitSpend}
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
