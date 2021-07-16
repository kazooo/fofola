import {createAction} from "@reduxjs/toolkit";
import {takeEvery, select, call, put} from "redux-saga/effects";

import {createActionType, getCurrentPage, removeProcessInfo, setProcessesInfo, toggleIsLoading} from "./slice";
import {request} from "../../redux/superagent";

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
        yield call(() => request
            .delete("/k-processes/" + action.payload)
        );
        yield put(removeProcessInfo(action.payload));
    } catch (e) {
        console.error(e);
    }
}

function* stopProcessSaga(action) {
    try {
        yield call(() => request
            .put("/k-processes/" + action.payload + "/stop")
        );
        yield call(requestCurrentPageProcessesInfoSaga, action);
    } catch (e) {
        console.error(e);
    }
}
