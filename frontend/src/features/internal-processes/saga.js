import {call, put, select, takeEvery} from "redux-saga/effects";
import {createAction} from "@reduxjs/toolkit";

import {baseUrl, request} from "../../redux/superagent";
import {createActionType, getCurrentPage, removeProcessInfo, setProcessesInfo, toggleIsLoading} from "./slice";
import {snackbar} from "../../utils/snack/saga";
import {
    cantLoadNextPage,
    cantRemoveProcessMsg,
    cantStopProcessMsg,
    successRemoveProcessMsg,
    successStopProcessMsg
} from "../../utils/constants/messages";

const REQUEST_NEW_PAGE_INTERNAL_PROCESS = createActionType("REQUEST_NEW_PAGE_INTERNAL_PROCESS");
const REQUEST_CURRENT_PAGE_INTERNAL_PROCESS = createActionType("REQUEST_CURRENT_PAGE_INTERNAL_PROCESS");
const STOP_INTERNAL_PROCESS = createActionType("STOP_INTERNAL_PROCESS");
const REMOVE_INTERNAL_PROCESS = createActionType("REMOVE_INTERNAL_PROCESS");
const OPEN_INTERNAL_PROCESS_LOGS = createActionType("OPEN_INTERNAL_PROCESS_LOGS");

export const stopInternalProcess = createAction(STOP_INTERNAL_PROCESS);
export const removeInternalProcess = createAction(REMOVE_INTERNAL_PROCESS);
export const openInternalProcessLogs = createAction(OPEN_INTERNAL_PROCESS_LOGS);
export const requestNewPageInternalProcesses = createAction(REQUEST_NEW_PAGE_INTERNAL_PROCESS);
export const requestInternalProcesses = createAction(REQUEST_CURRENT_PAGE_INTERNAL_PROCESS);

export default function* watcherSaga() {
    yield takeEvery(STOP_INTERNAL_PROCESS, stopProcessSaga);
    yield takeEvery(OPEN_INTERNAL_PROCESS_LOGS, openLogsSaga);
    yield takeEvery(REMOVE_INTERNAL_PROCESS, removeProcessSaga);
    yield takeEvery(REQUEST_NEW_PAGE_INTERNAL_PROCESS, requestProcessesInfoLoadingSaga);
    yield takeEvery(REQUEST_CURRENT_PAGE_INTERNAL_PROCESS, requestCurrentPageProcessSaga);
}

function* requestCurrentPageProcessSaga(action) {
    try {
        const page = yield select(getCurrentPage);
        /* TODO create pagination for internal processes */
        const payload = yield call(() => request
            .get("/internal-processes/all")
        );
        yield put(setProcessesInfo(payload.body));
    } catch (e) {
        yield put(snackbar.error(cantLoadNextPage));
        console.error(e);
    }
}

function* requestProcessesInfoLoadingSaga(action) {
    yield put(toggleIsLoading());
    yield call(requestCurrentPageProcessSaga, action);
    yield put(toggleIsLoading());
}

function* stopProcessSaga(action) {
    try {
        const pid = action.payload;
        yield call(() => request
            .put("/internal-processes/stop/" + pid)
        );
        yield put(snackbar.success(successStopProcessMsg));
    } catch (e) {
        yield put(snackbar.error(cantStopProcessMsg));
        console.error(e);
    }
}

function* removeProcessSaga(action) {
    try {
        const pid = action.payload;
        yield call(() => request
            .delete("/internal-processes/remove/" + pid)
        );
        yield put(removeProcessInfo(pid));
        yield put(snackbar.success(successRemoveProcessMsg));
    } catch (e) {
        yield put(snackbar.success(cantRemoveProcessMsg));
        console.error(e);
    }
}

function* openLogsSaga(action) {
    const url = baseUrl + '/internal-processes/logs/' + action.payload + '.log';
    const win = window.open(url, '_blank');
    win.focus();
}
