import superagent from 'superagent';
import superagentAbsolute from 'superagent-absolute';
import {BASE_URL} from './environment';
import i18n from './i18n';

const agent = superagent.agent();

export const request = superagentAbsolute(agent)(BASE_URL);

export const translateServerError = (response) => {
    const errorMessageKey = response.body['errorMessageKey'];
    const errorParameters = response.body['errorParameters'];
    let errorMessage;

    if (errorMessageKey && i18n.exists(errorMessageKey)) {
        errorMessage = i18n.t(errorMessageKey, errorParameters);
    } else {
        errorMessage = response.body['defaultErrorMessage'];
    }

    return errorMessage;
};
