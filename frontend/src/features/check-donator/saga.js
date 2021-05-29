import {createAction} from "@reduxjs/toolkit";
import {call, put, takeEvery} from "redux-saga/effects";
import {request} from "../../redux/superagent";
import {setOutputFiles, removeOutputFile} from "./slice";

const CHECK_DONATOR = "CHECK_DONATOR";
const REQUEST_CHECK_DONATOR_OUTPUTS = "REQUEST_CHECK_DONATOR_OUTPUTS";
const REMOVE_OUTPUT_FILE = "REMOVE_OUTPUT_FIlE";
const DOWNLOAD_OUTPUT_FILE = "DOWNLOAD_OUTPUT_FILE";

export const checkDonator = createAction(CHECK_DONATOR);
export const removeFile = createAction(REMOVE_OUTPUT_FILE);
export const downloadOutputFile = createAction(DOWNLOAD_OUTPUT_FILE);
export const requestCheckDonatorOutputs = createAction(REQUEST_CHECK_DONATOR_OUTPUTS);

export default function* watcherSaga() {
    yield takeEvery(CHECK_DONATOR, checkDonatorSaga);
    yield takeEvery(REMOVE_OUTPUT_FILE, removeFileSaga);
    yield takeEvery(DOWNLOAD_OUTPUT_FILE, downloadFileSaga);
    yield takeEvery(REQUEST_CHECK_DONATOR_OUTPUTS, requestFilesSaga);
}

function* checkDonatorSaga(action) {
    try {
        yield call(() => request
            .post("/internal-processes/new/donator_check")
            .send(action.payload)
        );
    } catch (e) {
        console.error(e);
    }
}

function* requestFilesSaga(action) {
    try {
        const payload = yield call(() => request
            .get("/check-donator/all")
        );
        yield put(setOutputFiles(payload));
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
    const form = document.createElement("form");
    form.method = "GET";
    form.action = "/check-donator/download/" + action.payload;
    document.body.appendChild(form);
    form.submit();
    document.body.removeChild(form);
}
