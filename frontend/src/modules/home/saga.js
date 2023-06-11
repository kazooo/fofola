import {createAction} from '@reduxjs/toolkit';
import {call, put, takeEvery} from 'redux-saga/effects';
import moment from 'moment';

import {cantLoadEnvInfo} from 'utils/constants/messages';
import {snackbar} from 'utils/snack/saga';
import {request} from 'utils/superagent';

import {createActionType, setAppInfo} from './slice';
import {SUGO_ALERT_ENDPOINT} from '../dnnt-alert/saga';

export const REQUEST_ENV_INFO = createActionType('REQUEST_ENV_INFO');
export const REQUEST_ALERT_STATS = createActionType('REQUEST_ALERT_STATS');

export const requestEnvInfo = createAction(REQUEST_ENV_INFO);
export const requestAlertStats = createAction(REQUEST_ALERT_STATS);

const dateTimeFormat = 'HH:mm:ss DD/MM/YYYY';
const MANAGEMENT_API = '/management';

export default function* watcherSaga() {
    yield takeEvery(REQUEST_ENV_INFO, requestEnvInfoSaga);
    yield takeEvery(REQUEST_ALERT_STATS, requestAlertStatsSaga);
};

function* requestEnvInfoSaga() {
    try {
        const startupPayload = yield call(() => request.get(`${MANAGEMENT_API}/startup`));
        const infoPayload = yield call(() => request.get(`${MANAGEMENT_API}/info`));

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

function* requestAlertStatsSaga() {
    try {
        const response = yield call(() => request.get(`${SUGO_ALERT_ENDPOINT}/stats`));
        if (response.statusCode !== 200) {
            yield put(snackbar.error('feature.dnntAlerts.request.stats.load.error'));
            return;
        }
        const stats = response.body;
        const total = stats.warnings + stats.errors;
        if (total > 0) {
            const url = '/dnnt-alert';
            yield put(snackbar.warning('feature.dnntAlerts.request.stats.alert', {total, url}));
        }
    } catch (e) {
        yield put(snackbar.error('feature.dnntAlerts.request.stats.load.cant'));
        console.error(e);
    }
}
