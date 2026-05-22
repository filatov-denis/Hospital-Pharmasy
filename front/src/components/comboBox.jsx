import React from 'react';
import { getAll } from '../api';

// Reusable typeahead. Two modes:
//   entity:  loads items from GET /{entity}?name=<query>  (server-filtered)
//   options: ['VALUE_A', 'VALUE_B', ...]                  (static, client-filtered)
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
    () => options ? options.map(v => ({ id: v, name: (t.values && t.values[v]) || v })) : null,
    [options, t]
  );

  // Resolve display name for a pre-set value (once).
  React.useEffect(() => {
    if (resolved.current || value == null) return;
    resolved.current = true;
    if (staticItems) {
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
  }, [value, entity, staticItems]);

  // Update items while open. Static mode filters locally; entity mode hits the API (debounced).
  React.useEffect(() => {
    if (!open) return;
    if (staticItems) {
      const q = query.toLowerCase();
      setItems(staticItems.filter(i => i.name.toLowerCase().includes(q)));
      return;
    }
    const id = setTimeout(() => {
      getAll(entity, { name: query, page: 0, size: 20 })
        .then(res => setItems(res.content || res || []))
        .catch(() => setItems([]));
    }, 200);
    return () => clearTimeout(id);
  }, [entity, query, open, staticItems]);

  // Close on outside click.
  React.useEffect(() => {
    if (!open) return;
    const close = (e) => { if (ref.current && !ref.current.contains(e.target)) setOpen(false); };
    document.addEventListener('mousedown', close);
    return () => document.removeEventListener('mousedown', close);
  }, [open]);

  const pick = (item) => {
    onChange(item.id);
    setQuery(item.name);
    setOpen(false);
  };

  return (
    <div className="combo" ref={ref}>
      <input
        type="text"
        className="combo-input"
        value={query}
        onFocus={() => setOpen(true)}
        onChange={e => { setQuery(e.target.value); setOpen(true); }}
        placeholder={t.search || ''}
      />
      <span className="combo-arrow" aria-hidden="true">▾</span>
      {open && (
        <ul className="combo-panel">
          {items.length === 0 && <li className="combo-empty">{t.noData}</li>}
          {items.map(item => (
            <li
              key={item.id}
              className={item.id === value ? 'active' : ''}
              onMouseDown={(e) => { e.preventDefault(); pick(item); }}
            >
              {item.name}
            </li>
          ))}
        </ul>
      )}
    </div>
  );
}
