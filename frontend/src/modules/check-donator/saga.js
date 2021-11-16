import {createAction} from "@reduxjs/toolkit";
import {call, put, takeEvery} from "redux-saga/effects";

import {request} from "../../utils/superagent";
import {setOutputFiles, removeOutputFile, createActionType, setVcs, setIsLoading, setIsLoadingError} from "./slice";
import {snackbar} from "../../utils/snack/saga";
import {
    cantLoadFilesMsg,
    cantLoadVcMsg,
    cantRemoveFileMsg,
    getCantCheckDonatorMsg,
    getCheckDonatorMsg,
    successRemoveFileMsg,
} from "../../utils/constants/messages";
import {BASE_URL} from "../../utils/environment";

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
    const uuids = action.payload;
    try {
        yield call(() => request
            .post("/internal-processes/new/donator_check")
            .send(action.payload)
        );
        yield call(requestFilesSaga);
        yield put(snackbar.success(getCheckDonatorMsg(uuids.length)));
    } catch (e) {
        yield put(snackbar.error(getCantCheckDonatorMsg(uuids.length)));
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
        yield put(snackbar.error(cantLoadFilesMsg));
        console.error(e);
    }
}

function* removeFileSaga(action) {
    try {
        yield call(() => request
            .delete("/check-donator/remove/" + action.payload)
        );
        yield put(removeOutputFile(action.payload));
        yield put(snackbar.success(successRemoveFileMsg));
    } catch (e) {
        yield put(snackbar.error(cantRemoveFileMsg));
        console.error(e);
    }
}

function* downloadFileSaga(action) {
    const url = BASE_URL + '/check-donator/download/' + action.payload;
    const win = window.open(url, '_self');
    win.focus();
}

function* loadVirtualCollectionsSaga() {
    try {
        yield put(setIsLoading(true));
        const response = yield call(() => request
            .get('/vc/all')
        );
        yield put(setVcs(response.body));
        yield put(setIsLoadingError(false));
    } catch (e) {
        yield put(snackbar.error(cantLoadVcMsg));
        console.error(e);
        yield put(setVcs([]));
        yield put(setIsLoadingError(true));
    } finally {
        yield put(setIsLoading(false));
    }
}
