// Request status finite-state machine. Terminal states have no entry.
export const STATUS_TRANSITIONS = {
  CREATED:    ['CONFIRMED', 'CANCELLED'],
  CONFIRMED:  ['DELIVERING', 'CANCELLED'],
  DELIVERING: ['COMPLETED', 'CANCELLED'],
};

// Allowed next states for a given current state. Returns [] for terminal/unknown.
export const nextStates = (current) => STATUS_TRANSITIONS[current] || [];
