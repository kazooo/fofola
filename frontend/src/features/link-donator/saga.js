import {takeEvery, call, put} from "redux-saga/effects";
import {createAction} from "@reduxjs/toolkit";
import {request} from "../../redux/superagent";
import {clearUuids, createActionType} from "./slice";

const CHANGE_DONATOR = createActionType("CHANGE_DONATOR");

export const changeDonator = createAction(CHANGE_DONATOR);

export default function* watcherSaga() {
    yield takeEvery(CHANGE_DONATOR, changeDonatorSaga);
}

function* changeDonatorSaga(action) {
    try {
        yield call(() => request
            .post("/internal-processes/new/donator_link")
            .send(action.payload)
        );
    } catch (e) {
        console.error(e);
    } finally {
        yield put(clearUuids())
    }
}
