const DEV_BASE_URL = 'http://localhost:8082/api';
const PROD_BASE_URL = window.location.origin + '/api';

export const BASE_URL = process.env.NODE_ENV === 'production' ? PROD_BASE_URL : DEV_BASE_URL;
