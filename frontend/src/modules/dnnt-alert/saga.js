import {createAction} from '@reduxjs/toolkit';
import {call, put, takeLatest} from 'redux-saga/effects';

import {request, translateServerError} from 'utils/superagent';
import {snackbar} from 'utils/snack/saga';

import {
    createActionType,
    setAlerts,
    toggleIsLoading,
    openAlertWindow as openAlertWindowSlice,
} from './slice';

const REQUEST_PAGE = createActionType('REQUEST_PAGE');
const OPEN_ALERT_WINDOW = createActionType('OPEN_ALERT_WINDOW');
const SOLVE_ALERT = createActionType('SOLVE_ALERT');

export const requestPage = createAction(REQUEST_PAGE, (page, size) => ({payload: {page, size}}));
export const openAlertWindow = createAction(OPEN_ALERT_WINDOW, (id) => ({payload: {id}}));
export const solveAlert  = createAction(SOLVE_ALERT, (id) => ({payload: {id}}));

export const SUGO_ALERT_ENDPOINT = '/sugo/alert';

export default function* watcherSaga() {
    yield takeLatest(REQUEST_PAGE, requestPageSaga);
    yield takeLatest(OPEN_ALERT_WINDOW, openAlertWindowSaga);
    yield takeLatest(SOLVE_ALERT, solveAlertSaga);
};

function* requestPageSaga(action) {
    yield put(toggleIsLoading());

    try {
        const payload = yield call(() => request.get(`${SUGO_ALERT_ENDPOINT}/unsolved`));
        const alerts = payload.body['entities'];
        if (alerts) {
            yield put(setAlerts(alerts));
        }
    } catch (e) {
        yield put(snackbar.error('feature.dnntAlerts.request.alert.loadPreviews.error.cant'));
        console.error(e);
    }

    yield put(toggleIsLoading());
}

function* openAlertWindowSaga(action) {
    try {
        const response = yield call(() => request.get(`${SUGO_ALERT_ENDPOINT}/${action.payload.id}`));
        if (response.statusCode === 200) {
            yield put(openAlertWindowSlice(response.body));
        } else {
            yield put(snackbar.error(translateServerError(response)));
        }
    } catch (e) {
        yield put(snackbar.error('feature.dnntAlerts.request.alert.load.error.cant'));
        console.error(e);
    }
}

function* solveAlertSaga(action) {
    try {
        const response = yield call(() => request.put(`${SUGO_ALERT_ENDPOINT}/${action.payload.id}/solve`));
        if (response.statusCode === 200) {
            yield put(snackbar.success('feature.dnntAlerts.request.alert.solve.success'));
            yield put(requestPage(0, 20));
            yield put(openAlertWindowSlice(response.body));
        } else {
            yield put(snackbar.error('feature.dnntAlerts.request.alert.solve.error'));
        }
    } catch (e) {
        yield put(snackbar.error('feature.dnntAlerts.request.alert.solve.error'));
        console.error(e);
    }
}
