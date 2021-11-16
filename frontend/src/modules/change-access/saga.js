import {call, put, takeEvery} from "redux-saga/effects";
import {createAction} from "@reduxjs/toolkit";

import {request} from "../../utils/superagent";
import {snackbar} from "../../utils/snack/saga";
import {clearUuids, createActionType} from "./slice";
import {
    getCantMakePublicMsg,
    getMakePrivateMsg,
    getMakePublicMsg
} from "../../utils/constants/messages";

const PUBLIC_UUIDS = createActionType("PUBLIC_UUIDS");
const PRIVATE_UUIDS = createActionType("PRIVATE_UUIDS");

export const publicUuids = createAction(PUBLIC_UUIDS);
export const privateUuids = createAction(PRIVATE_UUIDS);

export default function* watcherSaga() {
    yield takeEvery(PUBLIC_UUIDS, publicSaga);
    yield takeEvery(PRIVATE_UUIDS, privateSaga);
}

function* publicSaga(action) {
    const uuids = action.payload;
    try {
        yield call(() => request
            .post('/access/public')
            .send(uuids)
        );
        yield put(clearUuids());
        yield put(snackbar.success(getMakePublicMsg(uuids.length)));
    } catch (e) {
        yield put(snackbar.error(getCantMakePublicMsg(uuids.length)));
        console.error(e);
    }
}

function* privateSaga(action) {
    const uuids = action.payload;
    try {
        yield call(() => request
            .post('/access/private')
            .send(uuids)
        );
        yield put(clearUuids());
        yield put(snackbar.success(getMakePrivateMsg(uuids.length)));
    } catch (e) {
        yield put(snackbar.error(getCantMakePublicMsg(uuids.length)));
        console.error(e);
    }
}
