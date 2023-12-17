import {createAction} from '@reduxjs/toolkit';
import {call, put, takeEvery} from 'redux-saga/effects';

import {getCantDeleteUuidsMsg, getDeleteUuidsMsg} from 'utils/constants/messages';
import {snackbar} from 'utils/snack/saga';
import {request} from 'utils/superagent';

import {clearUuids, createActionType} from './slice';

const DELETE_UUIDS = createActionType('DELETE_UUIDS');

export const deleteUuids = createAction(DELETE_UUIDS);

export default function* watcherSaga() {
    yield takeEvery(DELETE_UUIDS, deleteSaga);
}

function* deleteSaga(action) {
    const { uuids } = action.payload;
    try {
        yield call(() => request
            .delete('/delete')
            .send(uuids)
        );
        yield put(snackbar.success(getDeleteUuidsMsg(uuids.length)));
    } catch (e) {
        yield put(snackbar.error(getCantDeleteUuidsMsg(uuids.length)));
        console.error(e);
    } finally {
        yield put(clearUuids());
    }
}
