// Sidebar sections per role. Edit the lists to change navigation.
// Labels resolve from `t.sections[id]` in languageConstants.

export const SECTIONS_BY_ROLE = {
  ROLE_ADMIN: ['users', 'storages', 'medications', 'batches', 'requests', 'analytics'],
  ROLE_PHARMACIST: ['mainStorage', 'deptStorages', 'batches', 'requests', 'analytics'],
  ROLE_NURSE: ['mainStorage', 'deptStorages', 'batches', 'myRequests'],
};

// Section -> backend entity name (used with getAll). Sections with no entity
// (e.g. 'reports') are intentionally absent.
export const SECTION_ENTITY = {
  users:        'user',
  storages:     'storage',
  medications:  'product',
  requests:     'request',
  mainStorage:  'storage',
  deptStorages: 'storage',
  myRequests:   'request',
  batches:      'batch',
  analytics:    'request/analytics',
};

// Columns per entity (id and image fields excluded). Used to render headers
// even when the request fails or returns zero rows.
export const ENTITY_FIELDS = {
  user:    ['username', 'name', 'middlename', 'lastname', 'role', 'linkedStorageId'],
  storage: ['name', 'isPharmacyStorage'],
  product: ['name', 'description', 'isRequiredRecipe', 'manufacturer', 'countryName'],
  request: ['number', 'creatorName', 'handlerName', 'status', 'creationDate', 'productName', 'productCount'],
  country: ['name'],
  batch:   ['product.name', 'count', 'manufactureDate', 'expirationDate'],
  'request/analytics': ['number', 'creatorName', 'handlerName', 'status', 'creationDate', 'productName', 'productCount'],
};

// Available filter params per entity (matches *Filter DTOs in the backend).
export const ENTITY_FILTERS = {
  user:    ['username', 'name'],
  storage: ['name'],
  product: ['name', 'productType', 'countryOfOriginId', 'manufacturer'],
  request: ['creatorId', 'status', 'creationDateFrom', 'creationDateTo', 'productName'],
};

// Fields shown in the "Add" popup per entity (matches *CreateDto DTOs).
export const ENTITY_CREATE_FIELDS = {
  user:    ['username', 'password', 'name', 'middlename', 'lastname', 'role', 'linkedStorageId'],
  storage: ['name'],
  product: ['name', 'productType', 'description', 'isRequiredRecipe', 'manufacturer', 'countryId'],
  request: ['sourceBatchId', 'targetBatchId', 'targetStorageId', 'count'],
  batch:   ['productId', 'count', 'manufactureDate', 'expirationDate'],
};

// Entities that support DELETE /<entity>/{id}. /request and /country are read-only.
export const ENTITY_DELETABLE = new Set(['user', 'storage', 'product', 'batch']);

// Fields shown in the "Edit" popup per entity (matches *UpdateDto DTOs, minus id).
export const ENTITY_EDIT_FIELDS = {
  user:    ['name', 'middlename', 'lastname', 'password', 'linkedStorageId'],
  storage: ['name'],
  product: ['name', 'description', 'isRequiredRecipe', 'manufacturer', 'countryId'],
  request: ['status'],
  batch:   ['count', 'manufactureDate', 'expirationDate'],
};

// Hardcoded enum values reused below.
const PRODUCT_TYPES = [
  'TABLETS', 'POWDER', 'GRANULES', 'CAPSULES', 'PILLS',
  'OINTMENT', 'GEL', 'SUPPOSITORIES', 'PLASTER', 'SYRUP',
  'SOLUTION', 'DROPS', 'MIXTURE', 'EXTRACT', 'AEROSOL', 'SPRAY',
];

const YES_NO = [
  true, false
];

// Per-field widget config. Used by both filter and edit popups.
//   { combo: 'country' }       -> ComboBox loading from GET /country?name=...
//   { options: [...] }         -> ComboBox with hardcoded values
//   { type: 'password' }       -> <input type="password">
//   { type: 'date' }           -> <input type="date">
// Fields without an entry fall back to a plain text input.
export const FIELD_CONFIG = {
  countryOfOriginId: { combo: 'country' },
  countryId:         { combo: 'country' },
  linkedStorageId:   { combo: 'storage' },
  targetStorageId:   { combo: 'storage' },
  creatorId:         { combo: 'user' },
  productId:         { combo: 'product' },
  sourceBatchId:     { combo: 'batch' },
  targetBatchId:     { combo: 'batch' },
  creationDateFrom:  { type: 'date' },
  creationDateTo:    { type: 'date' },
  manufactureDate:   { type: 'date' },
  expirationDate:    { type: 'date' },
  count:             { type: 'number' },
  productType:       { options: PRODUCT_TYPES },
  isRequiredRecipe:  { options: YES_NO },
  isPharmacyStorage: { options: YES_NO },
};
