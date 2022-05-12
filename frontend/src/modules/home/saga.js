import {createAction} from '@reduxjs/toolkit';
import {call, put, takeEvery} from 'redux-saga/effects';
import moment from 'moment';

import {cantLoadEnvInfo} from '../../utils/constants/messages';
import {snackbar} from '../../utils/snack/saga';
import {request} from '../../utils/superagent';

import {createActionType, setAppInfo} from './slice';

export const REQUEST_ENV_INFO = createActionType('REQUEST_ENV_INFO');

export const requestEnvInfo = createAction(REQUEST_ENV_INFO);

const dateTimeFormat = 'HH:mm:ss DD/MM/YYYY';

export default function* watcherSaga() {
    yield takeEvery(REQUEST_ENV_INFO, requestEnvInfoSaga);
};

function* requestEnvInfoSaga() {
    try {
        const startupPayload = yield call(() => request.get('/management/startup'));
        const infoPayload = yield call(() => request.get('/management/info'));

        const startupTimeRaw = startupPayload.body?.timeline?.startTime;
        const buildTimeRaw = infoPayload.body?.build?.time;

        const appInfo = {
            startupTime: startupTimeRaw ? moment(startupTimeRaw).format(dateTimeFormat) : null,
            buildTime: buildTimeRaw ? moment(buildTimeRaw).format(dateTimeFormat) : null,
            version: infoPayload.body?.build?.version,
            gitBranch: infoPayload.body?.git?.branch,
            commitId: infoPayload.body?.git?.commit?.id,
        }

        yield put(setAppInfo(appInfo));
    } catch (e) {
        yield put(snackbar.error(cantLoadEnvInfo));
        console.error(e);
    }
}
