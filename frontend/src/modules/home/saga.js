import {createAction} from "@reduxjs/toolkit";
import {call, put, takeEvery} from "redux-saga/effects";
import moment from 'moment';

import {createActionType, setBuildTime, setCommitId, setGitBranch, setStartupTime, setVersion} from "./slice";
import {snackbar} from "../../utils/snack/saga";
import {cantLoadEnvInfo} from "../../utils/constants/messages";
import {request} from "../../utils/superagent";

export const REQUEST_ENV_INFO = createActionType("REQUEST_ENV_INFO");

export const requestEnvInfo = createAction(REQUEST_ENV_INFO);

export default function* watcherSaga() {
    yield takeEvery(REQUEST_ENV_INFO, requestEnvInfoSaga);
};

function* requestEnvInfoSaga() {
    const dateTimeFormat = "HH:MM:SS DD/MM/YYYY";
    try {
        const startupPayload = yield call(() => request.get("/management/startup"));
        const infoPayload = yield call(() => request.get("/management/info"));

        const startupTime = moment(startupPayload.body?.timeline.startTime).format(dateTimeFormat);
        const buildTime = moment(infoPayload.body?.build.time).format(dateTimeFormat);
        const version = infoPayload.body?.build.version;
        const gitBranch = infoPayload.body?.git.branch;
        const commitId = infoPayload.body?.git.commit.id;

        yield put(setStartupTime(startupTime));
        yield put(setBuildTime(buildTime));
        yield put(setVersion(version));
        yield put(setGitBranch(gitBranch));
        yield put(setCommitId(commitId));
    } catch (e) {
        yield put(snackbar.error(cantLoadEnvInfo));
        console.error(e);
    }
}
