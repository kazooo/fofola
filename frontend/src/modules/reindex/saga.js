import {takeEvery, call, put} from "redux-saga/effects";
import {createAction} from "@reduxjs/toolkit";

import {request} from "../../utils/superagent";
import {clearUuids, createActionType} from "./slice";
import {snackbar} from "../../utils/snack/saga";
import {getCantReindexMsg, getReindexMsg} from "../../utils/constants/messages";

const REINDEX_UUID = createActionType("REINDEX_UUID");

export const reindexUuids = createAction(REINDEX_UUID);

export default function* watcherSaga() {
    yield takeEvery(REINDEX_UUID, reindexSaga);
}

function* reindexSaga(action) {
    const uuids = action.payload;
    try {
        yield call(() =>
            request.post("/reindex")
                .send(uuids)
        );
        yield put(snackbar.success(getReindexMsg(uuids.length)));
    } catch (e) {
        yield put(snackbar.error(getCantReindexMsg(uuids.length)));
        console.error(e);
    } finally {
        yield put(clearUuids());
    }
}
