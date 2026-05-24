import React from 'react';
import { getAll } from '../api';

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
        const sel = (res.content || res || []).find(i => i.id === value);
        if (sel) setQuery(sel.name);
      })
      .catch(() => {});
  }, [value, entity, staticItems, isStatic]);

  // Update items while open. Static mode shows the full list (no filter).
  // Entity mode hits the API debounced.
  React.useEffect(() => {
    if (!open) return;
    if (isStatic) { setItems(staticItems); return; }
    const id = setTimeout(() => {
      getAll(entity, { name: query, page: 0, size: 20 })
        .then(res => setItems(res.content || res || []))
        .catch(() => setItems([]));
    }, 200);
    return () => clearTimeout(id);
  }, [entity, query, open, staticItems, isStatic]);

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
        readOnly={isStatic}
        onFocus={() => setOpen(true)}
        onClick={() => isStatic && setOpen(o => !o)}
        onChange={isStatic ? undefined : (e => { setQuery(e.target.value); setOpen(true); })}
        placeholder={isStatic ? '' : (t.search || '')}
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
