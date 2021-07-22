import {clearUuids, createActionType} from "./slice";
import {createAction} from "@reduxjs/toolkit";
import {call, put, takeEvery} from "redux-saga/effects";
import {request} from "../../redux/superagent";

const CHANGE_LABEL = createActionType('CHANGE_LABEL');

export const changeLabel = createAction(CHANGE_LABEL);

export default function* watcherSaga() {
    yield takeEvery(CHANGE_LABEL, changeLabelSaga);
}

function* changeLabelSaga(action) {
    try {
        yield call(() => request
            .post("/internal-processes/new/dnnt_link")
            .send(action.payload)
        );
    } catch (e) {
        console.error(e);
    } finally {
        yield put(clearUuids());
    }
}
