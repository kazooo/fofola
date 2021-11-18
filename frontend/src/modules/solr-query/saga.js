import {createAction} from "@reduxjs/toolkit";
import {call, put, takeEvery} from "redux-saga/effects";

import {request} from "../../utils/superagent";
import {createActionType, setOutputFiles, removeOutputFile as removeOutputFileFromSlice} from "./slice";
import {snackbar} from "../../utils/snack/saga";
import {
    cantLoadFilesMsg,
    cantRemoveFileMsg,
    cantSolrQueryMsg,
    successRemoveFileMsg,
    successSolrQueryMsg
} from "../../utils/constants/messages";
import {BASE_URL} from "../../utils/environment";

const SEND_SOLR_QUERY = createActionType("SEND_SOLR_QUERY");
const REMOVE_OUTPUT_FILE = createActionType("REMOVE_OUTPUT_FILE");
const REQUEST_OUTPUT_FILES = createActionType("REQUEST_OUTPUT_FILES");
const DOWNLOAD_OUTPUT_FILE = createActionType("DOWNLOAD_OUTPUT_FILE");

export const sendSolrQuery = createAction(SEND_SOLR_QUERY);
export const removeOutputFile = createAction(REMOVE_OUTPUT_FILE);
export const requestOutputFiles = createAction(REQUEST_OUTPUT_FILES);
export const downloadOutputFile = createAction(DOWNLOAD_OUTPUT_FILE);

export default function* watcherSaga() {
    yield takeEvery(SEND_SOLR_QUERY, sendSolrQuerySaga);
    yield takeEvery(REMOVE_OUTPUT_FILE, removeOutputFileSaga);
    yield takeEvery(REQUEST_OUTPUT_FILES, requestOutputFilesSaga);
    yield takeEvery(DOWNLOAD_OUTPUT_FILE, downloadOutputFileSaga);
}

function* requestOutputFilesSaga(action) {
    try {
        const payload = yield call(() => request
            .get("/solr-response/all")
        );
        yield put(setOutputFiles(payload.body));
    } catch (e) {
        yield put(snackbar.error(cantLoadFilesMsg));
        console.error(e);
    }
}

function* sendSolrQuerySaga(action) {
    try {
        yield call(() => request
            .post("/internal-processes/new/solr-response")
            .send(action.payload)
        );
        yield put(snackbar.success(successSolrQueryMsg));
        yield call(requestOutputFilesSaga);
    } catch (e) {
        yield put(snackbar.error(cantSolrQueryMsg));
        console.error(e);
    }
}

function* removeOutputFileSaga(action) {
    try {
        const fileName = action.payload;
        yield call(() => request
            .delete("/solr-response/remove/" + fileName)
        );
        yield put(removeOutputFileFromSlice(fileName));
        yield put(snackbar.success(successRemoveFileMsg));
    } catch (e) {
        yield put(snackbar.error(cantRemoveFileMsg));
        console.error(e);
    }
}

// eslint-disable-next-line require-yield
function* downloadOutputFileSaga(action) {
    const fileName = action.payload;
    const url = BASE_URL + '/solr-response/download/' + fileName;
    const win = window.open(url, '_self');
    win.focus();
}
