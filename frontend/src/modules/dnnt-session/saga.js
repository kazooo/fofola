import {createAction} from '@reduxjs/toolkit';
import {call, put, select, takeEvery} from 'redux-saga/effects';
import moment from 'moment';

import {request, translateServerError} from 'utils/superagent';
import {snackbar} from 'utils/snack/saga';
import {cantLoadNextPage} from 'utils/constants/messages';

import {
    createActionType,
    getCurrentPage,
    getDirection,
    getFromDateTime,
    getOperation,
    getRequestor,
    getStatus,
    getToDateTime,
    setNumFound,
    setSessions,
    toggleIsLoading
} from './slice';

const SUGO_SESSIONS_URL = '/sugo/session';
const DATE_PATTERN = 'hh:mm DD/MM/YYYY';
const OUTPUT_DATE_PATTERN = 'YYYY-MM-DDTHH:mm:ss.sss';

const REQUEST_CURRENT_PAGE = createActionType('REQUEST_CURRENT_PAGE');
const PAUSE_SESSION = createActionType('PAUSE_SESSION');
const LAUNCH_SESSION = createActionType('LAUNCH_SESSION');
const TERMINATE_SESSION = createActionType('TERMINATE_SESSION');

export const requestSessionPage = createAction(REQUEST_CURRENT_PAGE);
export const pauseSession = createAction(PAUSE_SESSION, (sessionId) => ({payload: {sessionId}}));
export const launchSession = createAction(LAUNCH_SESSION, (sessionId) => ({payload: {sessionId}}));
export const terminateSession = createAction(TERMINATE_SESSION, (sessionId) => ({payload: {sessionId}}));

export default function* watcherSaga() {
    yield takeEvery(REQUEST_CURRENT_PAGE, requestWithLoading);
    yield takeEvery(PAUSE_SESSION, pauseSessionSaga);
    yield takeEvery(LAUNCH_SESSION, launchSessionSaga);
    yield takeEvery(TERMINATE_SESSION, terminateSessionSaga);
};

function* requestWithLoading(action) {
    yield put(toggleIsLoading());

    const page = yield select(getCurrentPage);
    const requestor = yield select(getRequestor);
    const direction = yield select(getDirection);
    const operation = yield select(getOperation);
    const status = yield select(getStatus);
    const fromDateTime = yield select(getFromDateTime);
    const toDateTime = yield select(getToDateTime);

    /* filter 'any' values */
    const queryParams = Object.entries(
        {
            page,
            requestor,
            direction,
            operation,
            status,
            from: fromDateTime && moment(fromDateTime, DATE_PATTERN).format(OUTPUT_DATE_PATTERN),
            to: toDateTime && moment(toDateTime, DATE_PATTERN).format(OUTPUT_DATE_PATTERN)
        }
    ).filter(([_, value]) => value !== 'any' && value !== '' && value !== null);

    try {
        const payload = yield call(() => request
            .get(SUGO_SESSIONS_URL)
            .query(Object.fromEntries(queryParams))
        );
        
        const sessions = payload.body['entities'];
        const numFound = payload.body['numFound'];

        yield put(setNumFound(numFound));
        if (sessions) {
            yield put(setSessions(sessions));
        }
    } catch (e) {
        yield put(snackbar.error(cantLoadNextPage));
        console.error(e);
    }

    yield put(toggleIsLoading());
}

function* pauseSessionSaga(action) {
    const sessionId = action.payload.sessionId;
    try {
        const response = yield call(() => request.put(`${SUGO_SESSIONS_URL}/${sessionId}/pause`));
        if (response.statusCode === 200 || response.statusCode === 202) {
            yield put(snackbar.success('feature.dnntSessions.request.session.pause.success'));
            yield put(requestSessionPage());
        } else {
            yield put(snackbar.error(translateServerError(response)));
        }
    } catch (e) {
        yield put(snackbar.error('feature.dnntSessions.request.session.pause.error.cant'));
        console.error(e);
    }
}

function* launchSessionSaga(action) {
    const sessionId = action.payload.sessionId;
    try {
        const response = yield call(() => request.put(`${SUGO_SESSIONS_URL}/${sessionId}/launch`));
        if (response.statusCode === 200 || response.statusCode === 202) {
            yield put(snackbar.success('feature.dnntSessions.request.session.launch.success'));
            yield put(requestSessionPage());
        } else {
            yield put(snackbar.error(translateServerError(response)));
        }
    } catch (e) {
        yield put(snackbar.error('feature.dnntSessions.request.session.launch.error.cant'));
        console.error(e);
    }
}

function* terminateSessionSaga(action) {
    const sessionId = action.payload.sessionId;
    try {
        const response = yield call(() => request.put(`${SUGO_SESSIONS_URL}/${sessionId}/terminate`));
        if (response.statusCode === 200 || response.statusCode === 202) {
            yield put(snackbar.success('feature.dnntSessions.request.session.terminate.success'));
            yield put(requestSessionPage());
        } else {
            yield put(snackbar.error(translateServerError(response)));
        }
    } catch (e) {
        yield put(snackbar.error('feature.dnntSessions.request.session.terminate.error.cant'));
        console.error(e);
    }
}
