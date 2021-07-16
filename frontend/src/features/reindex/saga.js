import {takeEvery, call, put} from "redux-saga/effects";
import {createAction} from "@reduxjs/toolkit";

import {request} from "../../redux/superagent";
import {clearUuids, createActionType} from "./slice";

const REINDEX_UUID = createActionType("REINDEX_UUID");

export const reindexUuids = createAction(REINDEX_UUID);

export default function* watcherSaga() {
    yield takeEvery(REINDEX_UUID, reindexSaga);
}

function* reindexSaga(action) {
    try {
        yield call(() =>
            request.post("/reindex")
                .send(action.payload)
        );
    } catch (e) {
        console.error(e);
    } finally {
        yield put(clearUuids());
    }
}
