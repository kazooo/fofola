import {createAction} from "@reduxjs/toolkit";
import {call, put, takeEvery} from "redux-saga/effects";

import {baseUrl, request} from "../../redux/superagent";
import {setOutputFiles, removeOutputFile, createActionType, setVcs} from "./slice";

const LOAD_VCS = createActionType('LOAD_VCS');
const CHECK_DONATOR = createActionType("CHECK_DONATOR");
const REQUEST_CHECK_DONATOR_OUTPUTS = createActionType("REQUEST_CHECK_DONATOR_OUTPUTS");
const REMOVE_OUTPUT_FILE = createActionType("REMOVE_OUTPUT_FIlE");
const DOWNLOAD_OUTPUT_FILE = createActionType("DOWNLOAD_OUTPUT_FILE");

export const checkDonator = createAction(CHECK_DONATOR);
export const removeFile = createAction(REMOVE_OUTPUT_FILE);
export const loadVirtualCollections = createAction(LOAD_VCS);
export const downloadOutputFile = createAction(DOWNLOAD_OUTPUT_FILE);
export const requestCheckDonatorOutputs = createAction(REQUEST_CHECK_DONATOR_OUTPUTS);

export default function* watcherSaga() {
    yield takeEvery(CHECK_DONATOR, checkDonatorSaga);
    yield takeEvery(REMOVE_OUTPUT_FILE, removeFileSaga);
    yield takeEvery(LOAD_VCS, loadVirtualCollectionsSaga);
    yield takeEvery(DOWNLOAD_OUTPUT_FILE, downloadFileSaga);
    yield takeEvery(REQUEST_CHECK_DONATOR_OUTPUTS, requestFilesSaga);
}

function* checkDonatorSaga(action) {
    try {
        yield call(() => request
            .post("/internal-processes/new/donator_check")
            .send(action.payload)
        );
        yield call(requestFilesSaga);
    } catch (e) {
        console.error(e);
    }
}

function* requestFilesSaga(action) {
    try {
        const payload = yield call(() => request
            .get("/check-donator/all")
        );
        yield put(setOutputFiles(payload.body));
    } catch (e) {
        console.error(e);
    }
}

function* removeFileSaga(action) {
    try {
        yield call(() => request
            .delete("/check-donator/remove/" + action.payload)
        );
        yield put(removeOutputFile(action.payload));
    } catch (e) {
        console.error(e);
    }
}

function* downloadFileSaga(action) {
    const url = baseUrl + '/check-donator/download/' + action.payload;
    const win = window.open(url, '_self');
    win.focus();
}

function* loadVirtualCollectionsSaga() {
    try {
        const response = yield call(() => request
            .get('/vc/all')
        );
        yield put(setVcs(response.body))
    } catch (e) {
        console.error(e);
        yield put(setVcs([]));
    }
}
