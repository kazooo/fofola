import {createAction} from "@reduxjs/toolkit";
import {call, put, takeEvery} from "redux-saga/effects";

import {request} from "../../utils/superagent";
import {clearUuidInfo, addUuidInfo, createActionType} from "./slice";
import {snackbar} from "../../utils/snack/saga";
import {
    cantGetUuidInfo,
    getCantMakePrivateMsg,
    getCantMakePublicMsg,
    getCantReindexMsg, getMakePrivateMsg,
    getMakePublicMsg,
    getReindexMsg
} from "../../utils/constants/messages";

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
        yield put(snackbar.error(cantGetUuidInfo));
        console.error(e);
    }
}

function* reindexSaga(action) {
    const uuids = action.payload;
    try {
        yield call(() => request
            .post("/reindex")
            .send(uuids)
        );
        yield put(clearUuidInfo())
        yield put(snackbar.success(getReindexMsg(uuids.length)));
    } catch (e) {
        yield put(snackbar.error(getCantReindexMsg(uuids.length)));
        console.error(e);
    }
}

function* publicSaga(action) {
    const uuids = action.payload;
    try {
        yield call(() => request
            .post('/access/public')
            .send(uuids)
        );
        yield put(clearUuidInfo());
        yield put(snackbar.success(getMakePublicMsg(uuids.length)));
    } catch (e) {
        yield put(snackbar.error(getCantMakePublicMsg(uuids.length)));
        console.log(e);
    }
}

function* privateSaga(action) {
    const uuids = action.payload;
    try {
        yield call(() => request
            .post('/access/private')
            .send(uuids)
        );
        yield put(clearUuidInfo());
        yield put(snackbar.success(getMakePrivateMsg(uuids.length)));
    } catch (e) {
        yield put(snackbar.error(getCantMakePrivateMsg(uuids.length)));
        console.log(e);
    }
}
