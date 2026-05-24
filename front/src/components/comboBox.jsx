import React from 'react';
import { getAll } from '../api';
import { ENTITY_LABEL, ENTITY_DROPDOWN } from '../values/sections';

// Two modes:
//   entity:  loads items from GET /{entity}?name=<query>  (server-filtered typeahead)
//   options: ['VALUE_A', 'VALUE_B', ...]                  (static dropdown, not searchable)
// In both cases the selected id is emitted via onChange. For options mode the id is
// the raw value; the display label resolves via t.values[value] when available.
export default function ComboBox({ entity, options, value, onChange, t }) {
  const [open, setOpen]   = React.useState(false);
  const [query, setQuery] = React.useState('');
  const [items, setItems] = React.useState([]);
  const ref = React.useRef(null);
  const resolved = React.useRef(false);

  // For options mode, pre-build the {id, name} list once.
  const staticItems = React.useMemo(
    () => options ? options.map(v => ({ id: v, name: (t.values && t.values[String(v)]) || String(v) })) : null,
    [options, t]
  );
  const isStatic = !!staticItems;
  // Entity-backed but should behave like a static dropdown (no search, always full list).
  const isDropdown = !!(entity && ENTITY_DROPDOWN.has(entity));
  const noSearch = isStatic || isDropdown;

  // Optionally override how an entity-mode item is displayed
  // (e.g. batch: "Glicerol • 2026-05-19 • 13" instead of the raw id).
  const formatItem = React.useCallback(
    (item) => (entity && ENTITY_LABEL[entity]) ? { ...item, name: ENTITY_LABEL[entity](item) } : item,
    [entity]
  );

  // Resolve display name for a pre-set value (once).
  React.useEffect(() => {
    if (resolved.current || value == null) return;
    resolved.current = true;
    if (isStatic) {
      const sel = staticItems.find(i => i.id === value);
      if (sel) setQuery(sel.name);
      return;
    }
    getAll(entity, { page: 0, size: 50 })
      .then(res => {
        const sel = (res.content || res || []).map(formatItem).find(i => i.id === value);
        if (sel) setQuery(sel.name);
      })
      .catch(() => {});
  }, [value, entity, staticItems, isStatic, formatItem]);

  // Update items while open. Static mode shows the full list (no filter).
  // Entity mode hits the API debounced.
  React.useEffect(() => {
    if (!open) return;
    if (isStatic) { setItems(staticItems); return; }
    const params = isDropdown ? { page: 0, size: 50 } : { name: query, page: 0, size: 20 };
    const id = setTimeout(() => {
      getAll(entity, params)
        .then(res => setItems((res.content || res || []).map(formatItem)))
        .catch(() => setItems([]));
    }, 200);
    return () => clearTimeout(id);
  }, [entity, query, open, staticItems, isStatic, isDropdown, formatItem]);

  // Close on outside click.
  React.useEffect(() => {
    if (!open) return;
    const close = (e) => { if (ref.current && !ref.current.contains(e.target)) setOpen(false); };
    document.addEventListener('mousedown', close);
    return () => document.removeEventListener('mousedown', close);
  }, [open]);

  const pick = (item) => {
    onChange(item.id ?? item.name);
    setQuery(item.name);
    setOpen(false);
  };

  return (
    <div className="combo" ref={ref}>
      <input
        type="text"
        className="combo-input"
        value={query}
        readOnly={noSearch}
        onFocus={() => setOpen(true)}
        onClick={() => noSearch && setOpen(o => !o)}
        onChange={noSearch ? undefined : (e => { setQuery(e.target.value); setOpen(true); })}
        placeholder={noSearch ? '' : (t.search || '')}
      />
      <svg className="combo-arrow" aria-hidden="true" viewBox="0 0 10 6" width="10" height="6">
        <path d="M1 1l4 4 4-4" fill="none" stroke="currentColor" strokeWidth="1.5" strokeLinecap="round" strokeLinejoin="round"/>
      </svg>
      {open && (
        <ul className="combo-panel">
          {items.length === 0 && <li className="combo-empty">{t.noData}</li>}
          {items.map(item => (
            <li
              key={String(item.id)}
              onMouseDown={(e) => { e.preventDefault(); pick(item); }}
            >
              {item.name ?? String(item.id)}
            </li>
          ))}
        </ul>
      )}
    </div>
  );
}
