import {createAction} from '@reduxjs/toolkit';
import {call, put, select, takeEvery} from 'redux-saga/effects';
import moment from 'moment';

import {request} from '../../utils/superagent';
import {snackbar} from '../../utils/snack/saga';
import {cantLoadNextPage} from '../../utils/constants/messages';

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

const DATE_PATTERN = 'hh:mm DD/MM/YYYY';
const OUTPUT_DATE_PATTERN = 'YYYY-MM-DDTHH:mm:ss.sss';

const REQUEST_CURRENT_PAGE = createActionType('REQUEST_CURRENT_PAGE');

export const requestSessionPage = createAction(REQUEST_CURRENT_PAGE);

export default function* watcherSaga() {
    yield takeEvery(REQUEST_CURRENT_PAGE, requestWithLoading);
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
            .get('/sugo/sessions')
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
