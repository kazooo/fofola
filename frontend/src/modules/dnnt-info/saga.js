import {createAction} from '@reduxjs/toolkit';
import {call, put, takeEvery} from 'redux-saga/effects';

import {cantLoadNextPage} from '../../utils/constants/messages';
import {snackbar} from '../../utils/snack/saga';
import {request} from '../../utils/superagent';

import {
    createActionType,
    setInfo,
    toggleIsLoading
} from './slice';

const REQUEST_CURRENT_PAGE = createActionType('REQUEST_CURRENT_PAGE');

export const requestInfoPage = createAction(REQUEST_CURRENT_PAGE);

export default function* watcherSaga() {
    yield takeEvery(REQUEST_CURRENT_PAGE, requestWithLoading);
};

function* requestWithLoading(action) {
    yield put(toggleIsLoading());

    const body = Object.entries(
        {
            internalUuids: action.payload.internalUuids,
            cnb: action.payload.cnb,
            sourceUuid: action.payload.sourceUuid,
        }
    ).filter(([_, value]) => value !== '' && value !== null);

    try {
        const payload = yield call(() => request
            .post('/sugo/info')
            .send(Object.fromEntries(body))
        );

        const info = payload.body['entities'];

        if (info) {
            yield put(setInfo(info));
        } else {
            yield put(setInfo([]));
        }
    } catch (e) {
        yield put(snackbar.error(cantLoadNextPage));
        console.error(e);
    }

    yield put(toggleIsLoading());
}
