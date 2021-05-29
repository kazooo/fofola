import {createAction} from "@reduxjs/toolkit";
import {call, put, select, takeEvery, all} from "redux-saga/effects";
import {request} from "../../redux/superagent";
import {setOutputFiles, removeOutputFile, getUuids} from "./slice";

const GENERATE_PDF = "GENERATE_PDF";
const REQUEST_PDF_FILES = "REQUEST_PDF_FILES";
const REMOVE_PDF_FILE = "REMOVE_PDF_FILE";
const DOWNLOAD_PDF_FILE = "DOWNLOAD_PDF_FILE";

export const generatePdf = createAction(GENERATE_PDF);
export const removePdfFile = createAction(REMOVE_PDF_FILE);
export const downloadPdfFile = createAction(DOWNLOAD_PDF_FILE);
export const requestPdfFiles = createAction(REQUEST_PDF_FILES);

export default function* watcherSaga() {
    yield takeEvery(GENERATE_PDF, generatePdfSaga);
    yield takeEvery(REMOVE_PDF_FILE, removeFileSaga);
    yield takeEvery(DOWNLOAD_PDF_FILE, downloadFileSaga);
    yield takeEvery(REQUEST_PDF_FILES, requestFilesSaga);
}

function* generatePdfSaga(action) {
    try {
        const uuids = yield select(getUuids);
        yield all(uuids.map(uuid =>
            request.post("/pdf/generate/" + uuid)
        ));
    } catch (e) {
        console.error(e);
    }
}

function* requestFilesSaga(action) {
    try {
        const payload = yield call(() => request
            .get("/pdf/get")
        );
        yield put(setOutputFiles(payload.body));
    } catch (e) {
        console.error(e);
    }
}

function* removeFileSaga(action) {
    try {
        yield call(() => request
            .delete("/pdf/remove/" + action.payload)
        );
        yield put(removeOutputFile(action.payload));
    } catch (e) {
        console.error(e);
    }
}

function* downloadFileSaga(action) {
    const win = window.open("/pdf/get/" + action.payload, '_blank');
    win.focus();
}
