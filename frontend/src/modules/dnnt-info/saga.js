import {createAction} from '@reduxjs/toolkit';
import {call, put, takeEvery} from 'redux-saga/effects';

import {cantLoadNextPage, getCantLinkDnntLabelMsg, getLinkDnntLabelMsg} from 'utils/constants/messages';
import {snackbar} from 'utils/snack/saga';
import {request} from 'utils/superagent';

import {DnntLinkingMode} from '../constants';

import {
    clearInfo,
    createActionType,
    setInfo,
    toggleIsLoading
} from './slice';

const REQUEST_CURRENT_PAGE = createActionType('REQUEST_CURRENT_PAGE');
const SYNCHRONIZE_UUIDS = createActionType('SYNCHRONIZE_UUIDS');

export const requestInfoPage = createAction(REQUEST_CURRENT_PAGE);
export const synchronizeUuids = createAction(SYNCHRONIZE_UUIDS);

export default function* watcherSaga() {
    yield takeEvery(REQUEST_CURRENT_PAGE, requestWithLoading);
    yield takeEvery(SYNCHRONIZE_UUIDS, synchronize);
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
        yield put(clearInfo());
        console.error(e);
    }

    yield put(toggleIsLoading());
}

function* synchronize(action) {
    yield put(toggleIsLoading());

    const body = {
        uuids: action.payload,
        mode: DnntLinkingMode.Synchronize,
        processRecursive: true,
    };

    try {
        yield call(() => request
            .post('/internal-processes/new/dnnt_link')
            .send(body)
        );
        yield put(snackbar.success(getLinkDnntLabelMsg(body.uuids.length)));
    } catch (e) {
        yield put(snackbar.error(getCantLinkDnntLabelMsg(body.uuids.length)));
        console.error(e);
    }

    yield put(toggleIsLoading());
}
