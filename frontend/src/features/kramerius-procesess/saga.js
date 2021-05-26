import {createAction} from "@reduxjs/toolkit";
import {takeEvery, select, call, put} from "redux-saga/effects";
import {getCurrentPage, removeProcessInfo, setProcessesInfo} from "./slice";
import {request} from "../../redux/superagent";

const REQUEST_PROCESSES_INFO = "REQUEST_PROCESSES_INFO";
const REMOVE_PROCESS = "REMOVE_PROCESS";
const STOP_PROCESS = "STOP_PROCESS";

export const requestProcessesInfo = createAction(REQUEST_PROCESSES_INFO);
export const removeProcess = createAction(REMOVE_PROCESS);
export const stopProcess = createAction(STOP_PROCESS);

export default function* watcherSaga() {
    yield takeEvery(REQUEST_PROCESSES_INFO, requestProcessesInfoSaga);
    yield takeEvery(REMOVE_PROCESS, removeProcessSaga);
    yield takeEvery(STOP_PROCESS, stopProcessSaga);
}

function* requestProcessesInfoSaga(action) {
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
    } catch (e) {
        console.error(e);
    }
}
