import {call, put, takeEvery} from "redux-saga/effects";
import {createAction} from "@reduxjs/toolkit";
import {request} from "../../redux/superagent";
import {clearUuids, createActionType} from "./slice";

const PUBLIC_UUIDS = createActionType("PUBLIC_UUIDS");
const PRIVATE_UUIDS = createActionType("PRIVATE_UUIDS");

export const publicUuids = createAction(PUBLIC_UUIDS);
export const privateUuids = createAction(PRIVATE_UUIDS);

export default function* watcherSaga() {
    yield takeEvery(PUBLIC_UUIDS, publicSaga);
    yield takeEvery(PRIVATE_UUIDS, privateSaga);
}

function* publicSaga(action) {
    try {
        yield call(() => request
            .post('/access/public')
            .send(action.payload)
        );
        yield put(clearUuids());
    } catch (e) {
        console.log(e);
    }
}

function* privateSaga(action) {
    try {
        yield call(() => request
            .post('/access/private')
            .send(action.payload)
        );
        yield put(clearUuids());
    } catch (e) {
        console.log(e);
    }
}
