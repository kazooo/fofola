import {createAction} from "@reduxjs/toolkit";
import {call, put, takeEvery} from "redux-saga/effects";
import moment from 'moment';

import {createActionType, setBuildTime, setCommitId, setGitBranch, setStartupTime, setVersion} from "./slice";
import {snackbar} from "../../utils/snack/saga";
import {cantLoadEnvInfo} from "../../utils/constants/messages";
import {request} from "../../utils/superagent";

export const REQUEST_ENV_INFO = createActionType("REQUEST_ENV_INFO");

export const requestEnvInfo = createAction(REQUEST_ENV_INFO);

const UNDEFINED_VALUE = null;
const dateTimeFormat = 'HH:MM:SS DD/MM/YYYY';

export default function* watcherSaga() {
    yield takeEvery(REQUEST_ENV_INFO, requestEnvInfoSaga);
};

function* requestEnvInfoSaga() {
    try {
        const startupPayload = yield call(() => request.get("/management/startup"));
        const infoPayload = yield call(() => request.get("/management/info"));

        const startupTimeRaw = startupPayload.body?.timeline.startTime;
        const buildTimeRaw = infoPayload.body?.build.time;

        const startupTime = startupTimeRaw ? moment(startupTimeRaw).format(dateTimeFormat) : UNDEFINED_VALUE;
        yield put(setStartupTime(startupTime));

        const buildTime = buildTimeRaw ? moment(buildTimeRaw).format(dateTimeFormat) : UNDEFINED_VALUE;
        yield put(setBuildTime(buildTime));

        const version = infoPayload.body?.build.version ?? UNDEFINED_VALUE;
        yield put(setVersion(version));

        const gitBranch = infoPayload.body?.git.branch ?? UNDEFINED_VALUE;
        yield put(setGitBranch(gitBranch));

        const commitId = infoPayload.body?.git.commit.id ?? UNDEFINED_VALUE;
        yield put(setCommitId(commitId));
    } catch (e) {
        yield put(snackbar.error(cantLoadEnvInfo));
        console.error(e);
    }
}
