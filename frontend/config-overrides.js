/**
 * Also look at jsconfig.js for custom module configuration.
 */
const path = require('path');
module.exports = function override(config) {
    config.resolve.alias = {
        ...config.resolve.alias,
        tools: path.resolve(__dirname, 'src/utils/tools.js'),
        components: path.resolve(__dirname, 'src/components'),
    };
    return config;
};
