import {createAction} from "@reduxjs/toolkit";
import {call, put, takeEvery} from "redux-saga/effects";

import {request} from "../../redux/superagent";
import {clearUuidInfo, addUuidInfo, createActionType} from "./slice";

const REINDEX_UUID = createActionType("REINDEX_UUID");
const PUBLIC_UUIDS = createActionType("PUBLIC_UUIDS");
const PRIVATE_UUIDS = createActionType("PRIVATE_UUIDS");
const GET_UUID_INFO = createActionType("GET_UUID_INFO");

export const getUuidInfo = createAction(GET_UUID_INFO);
export const reindexUuids = createAction(REINDEX_UUID);
export const publicUuids = createAction(PUBLIC_UUIDS);
export const privateUuids = createAction(PRIVATE_UUIDS);

export default function* watcherSaga() {
    yield takeEvery(GET_UUID_INFO, getUuidInfoSaga);
    yield takeEvery(REINDEX_UUID, reindexSaga);
    yield takeEvery(PUBLIC_UUIDS, publicSaga);
    yield takeEvery(PRIVATE_UUIDS, privateSaga);
}

function* getUuidInfoSaga(action) {
    try {
        const payload = yield call(() => request
            .post("/uuid-info")
            .send(action.payload)
        );
        yield put(addUuidInfo(payload.body));
    } catch (e) {
        console.error(e);
    }
}

function* reindexSaga(action) {
    try {
        yield call(() => request
            .post("/reindex")
            .send(action.payload)
        );
        yield put(clearUuidInfo())
    } catch (e) {
        console.error(e);
    }
}

function* publicSaga(action) {
    try {
        yield call(() => request
            .post('/access/public')
            .send(action.payload)
        );
        yield put(clearUuidInfo());
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
        yield put(clearUuidInfo());
    } catch (e) {
        console.log(e);
    }
}
