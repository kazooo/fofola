const DEFAULT_BASE_URL = 'http://localhost:3000/api';
const ENV_BASE_URL = window.location.origin + '/api';

export const BASE_URL = ENV_BASE_URL ? ENV_BASE_URL : DEFAULT_BASE_URL;
