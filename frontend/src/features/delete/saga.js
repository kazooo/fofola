import {createAction} from "@reduxjs/toolkit";
import {call, put, takeEvery} from "redux-saga/effects";
import {clearUuids, createActionType} from "./slice";
import {request} from "../../redux/superagent";

const DELETE_UUIDS = createActionType("DELETE_UUIDS");

export const deleteUuids = createAction(DELETE_UUIDS);

export default function* watcherSaga() {
    yield takeEvery(DELETE_UUIDS, deleteSaga);
}

function* deleteSaga(action) {
    try {
        yield call(() => request
            .delete("/delete")
            .send(action.payload)
        );
    } catch (e) {
        console.error(e);
    } finally {
        yield put(clearUuids());
    }
}
