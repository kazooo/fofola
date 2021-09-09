const DEFAULT_BASE_URL = 'http://localhost:3000';
const ENV_BASE_URL = window.location.origin;

export const BASE_URL = ENV_BASE_URL ? ENV_BASE_URL : DEFAULT_BASE_URL;
