import {call, put, select, takeEvery} from 'redux-saga/effects';
import {createAction} from '@reduxjs/toolkit';
import moment from 'moment';

import {cantLoadNextPage} from '../../utils/constants/messages';
import {snackbar} from '../../utils/snack/saga';
import {request} from '../../utils/superagent';

import {
    createActionType,
    getAccess,
    getCnb,
    getCurrentPage,
    getFromDateTime,
    getInternalUuid,
    getModel,
    getSourceIdentifier,
    getSourceUuid,
    getToDateTime, setNumFound,
    setTransitions,
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
    const fromDateTime = yield select(getFromDateTime);
    const toDateTime = yield select(getToDateTime);
    const internalUuid = yield select(getInternalUuid);
    const model = yield select(getModel);
    const access = yield select(getAccess);
    const cnb = yield select(getCnb);
    const sourceIdentifier = yield select(getSourceIdentifier);
    const sourceUuid = yield select(getSourceUuid);

    /* filter 'any' values */
    const queryParams = Object.entries(
        {
            page,
            internalUuid,
            model,
            access,
            cnb,
            sourceIdentifier,
            sourceUuid,
            from: fromDateTime && moment(fromDateTime, DATE_PATTERN).format(OUTPUT_DATE_PATTERN),
            to: toDateTime && moment(toDateTime, DATE_PATTERN).format(OUTPUT_DATE_PATTERN)
        }
    ).filter(([_, value]) => value !== 'any' && value !== '' && value !== null);

    try {
        const payload = yield call(() => request
            .get('/sugo/transitions')
            .query(Object.fromEntries(queryParams))
        );

        const transitions = payload.body['entities'];
        const numFound = payload.body['numFound'];

        yield put(setNumFound(numFound));
        if (transitions) {
            yield put(setTransitions(transitions));
        }
    } catch (e) {
        yield put(snackbar.error(cantLoadNextPage));
        console.error(e);
    }

    yield put(toggleIsLoading());
}
