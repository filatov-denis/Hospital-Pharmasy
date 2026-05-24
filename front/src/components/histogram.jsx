import React from 'react';

// Simple horizontal-bar histogram.
// `data` is an object: { label: count, ... }.
// Bars scale relative to the max value in the set.
export default function Histogram({ data, title }) {
  const entries = Object.entries(data || {});
  if (entries.length === 0) return null;
  const max = Math.max(...entries.map(([, v]) => v), 1);
  return (
    <div className="histogram">
      {title && <div className="hist-title">{title}</div>}
      {entries.map(([label, value]) => (
        <div className="hist-row" key={label}>
          <div className="hist-label" title={label}>{label}</div>
          <div className="hist-bar">
            <div className="hist-fill" style={{ width: `${(value / max) * 100}%` }} />
          </div>
          <div className="hist-value">{value}</div>
        </div>
      ))}
    </div>
  );
}
