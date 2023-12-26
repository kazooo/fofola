import {createAction} from "@reduxjs/toolkit";
import {takeEvery, select, call, put} from "redux-saga/effects";

import {request} from "utils/superagent";
import {snackbar} from "utils/snack/saga";
import {
    cantLoadNextPage,
    cantRemoveProcessMsg,
    cantStopProcessMsg,
    successRemoveProcessMsg,
    successStopProcessMsg
} from "utils/constants/messages";
import {createActionType, getCurrentPage, removeProcessInfo, setProcessesInfo, toggleIsLoading} from "./slice";

const REQUEST_NEW_PAGE_PROCESSES_INFO = createActionType("REQUEST_NEW_PAGE_PROCESSES_INFO");
const REQUEST_CURRENT_PAGE_PROCESSES_INFO = createActionType("REQUEST_CURRENT_PAGE_PROCESSES_INFO");
const REMOVE_PROCESS = createActionType("REMOVE_PROCESS");
const STOP_PROCESS = createActionType("STOP_PROCESS");

export const requestNewPageProcessesInfo = createAction(REQUEST_NEW_PAGE_PROCESSES_INFO);
export const requestProcessesInfo = createAction(REQUEST_CURRENT_PAGE_PROCESSES_INFO);
export const removeProcess = createAction(REMOVE_PROCESS);
export const stopProcess = createAction(STOP_PROCESS);

export default function* watcherSaga() {
    yield takeEvery(REQUEST_NEW_PAGE_PROCESSES_INFO, requestProcessesInfoLoadingSaga);
    yield takeEvery(REQUEST_CURRENT_PAGE_PROCESSES_INFO, requestCurrentPageProcessesInfoSaga);
    yield takeEvery(REMOVE_PROCESS, removeProcessSaga);
    yield takeEvery(STOP_PROCESS, stopProcessSaga);
}

function* requestCurrentPageProcessesInfoSaga(action) {
    try {
        const page = yield select(getCurrentPage);
        const payload = yield call(() => request
            .get("/k-processes/page/" + page)
        );
        yield put(setProcessesInfo(payload.body));
    } catch (e) {
        yield put(snackbar.error(cantLoadNextPage));
        console.error(e);
    }
}

function* requestProcessesInfoLoadingSaga(action) {
    yield put(toggleIsLoading());
    yield call(requestCurrentPageProcessesInfoSaga, action);
    yield put(toggleIsLoading());
}

function* removeProcessSaga(action) {
    try {
        const response = yield call(() => request
            .delete("/k-processes/" + action.payload)
        );
        yield put(removeProcessInfo(response ?? action.payload));
        yield put(snackbar.success(successRemoveProcessMsg));
    } catch (e) {
        yield put(snackbar.error(cantRemoveProcessMsg));
        console.error(e);
    }
}

function* stopProcessSaga(action) {
    try {
        yield call(() => request
            .put("/k-processes/" + action.payload + "/stop")
        );
        yield put(snackbar.success(successStopProcessMsg));
        yield call(requestCurrentPageProcessesInfoSaga, action);
    } catch (e) {
        yield put(snackbar.error(cantStopProcessMsg));
        console.error(e);
    }
}
