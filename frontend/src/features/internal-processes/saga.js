import {call, put, select, takeEvery} from "redux-saga/effects";
import {request} from "../../redux/superagent";
import {getCurrentPage, setProcessesInfo} from "./slice";
import {createAction} from "@reduxjs/toolkit";

const STOP_INTERNAL_PROCESS = "STOP_INTERNAL_PROCESS";
const REMOVE_INTERNAL_PROCESS = "REMOVE_INTERNAL_PROCESS";
const REQUEST_INTERNAL_PROCESSES = "REQUEST_INTERNAL_PROCESSES";
const OPEN_INTERNAL_PROCESS_LOGS = "OPEN_INTERNAL_PROCESS_LOGS";

export const stopInternalProcess = createAction(STOP_INTERNAL_PROCESS);
export const removeInternalProcess = createAction(REMOVE_INTERNAL_PROCESS);
export const openInternalProcessLogs = createAction(OPEN_INTERNAL_PROCESS_LOGS);
export const requestInternalProcesses = createAction(REQUEST_INTERNAL_PROCESSES);

export default function* watcherSaga() {
    yield takeEvery(STOP_INTERNAL_PROCESS, stopProcessSaga);
    yield takeEvery(OPEN_INTERNAL_PROCESS_LOGS, openLogsSaga);
    yield takeEvery(REMOVE_INTERNAL_PROCESS, removeProcessSaga);
    yield takeEvery(REQUEST_INTERNAL_PROCESSES, requestProcessSaga);
}

function* requestProcessSaga(action) {
    try {
        const payload = yield call(request
            .get("/internal-processes/all")
        );
        yield put(setProcessesInfo(payload.body));
    } catch (e) {
        console.error(e);
    }
}

function* stopProcessSaga(action) {
    try {
        const pid = action.payload;
        yield call(request
            .put("/internal-processes/stop/" + pid)
        );
    } catch (e) {
        console.error(e);
    }
}

function* removeProcessSaga(action) {
    try {
        const pid = action.payload;
        yield call(request
            .delete("/internal-processes/remove/" + pid)
        );
    } catch (e) {
        console.error(e);
    }
}

function* openLogsSaga(action) {
    const pid = action.payload;
    const url = '/internal-processes/logs/' + pid + '.log';
    const win = window.open(url, '_blank');
    win.focus();
}
