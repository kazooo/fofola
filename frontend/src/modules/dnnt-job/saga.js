import {createAction} from "@reduxjs/toolkit";
import {call, put, takeLatest} from 'redux-saga/effects';

import {closeJobForm, createActionType, removeJob, setJobs, toggleIsLoading} from "./slice";
import {request} from "../../utils/superagent";
import {snackbar} from "../../utils/snack/saga";

const REQUEST_JOB_PREVIEWS = createActionType('REQUEST_JOB_PREVIEWS');
const SUBMIT_CREATE_JOB_FORM = createActionType('SUBMIT_CREATE_JOB_FORM');
const SUBMIT_UPDATE_JOB_FORM = createActionType('SUBMIT_UPDATE_JOB_FORM');
const TOGGLE_JOB_ACTIVITY = createActionType('TOGGLE_JOB_ACTIVITY');
const DELETE_JOB = createActionType('DELETE_JOB');

export const requestJobPreviews = createAction(REQUEST_JOB_PREVIEWS);
export const submitCreateJobForm = createAction(SUBMIT_CREATE_JOB_FORM, (formValues) => ({payload: {formValues}}));
export const submitUpdateJobForm = createAction(SUBMIT_UPDATE_JOB_FORM, (formValues) => ({payload: {formValues}}));
export const toggleJobActivity = createAction(TOGGLE_JOB_ACTIVITY, (jobId) => ({payload: {jobId}}));
export const deleteJob = createAction(DELETE_JOB, (jobId) => ({payload: {jobId}}));

export default function* watcherSaga() {
    yield takeLatest(REQUEST_JOB_PREVIEWS, requestJobPreviewsSaga);
    yield takeLatest(SUBMIT_CREATE_JOB_FORM, submitCreateJobFormSaga);
    yield takeLatest(SUBMIT_UPDATE_JOB_FORM, submitUpdateJobFormSaga);
    yield takeLatest(TOGGLE_JOB_ACTIVITY, toggleJobActivitySaga);
    yield takeLatest(DELETE_JOB, deleteJobSaga);
};

function* requestJobPreviewsSaga(action) {
    yield put(toggleIsLoading());

    try {
        const payload = yield call(() => request.get('/sugo/job'));
        const jobs = payload.body['entities'];
        if (jobs) {
            yield put(setJobs(jobs));
        }
    } catch (e) {
        yield put(snackbar.error('feature.dnntJobs.snacks.cantLoadJobPreviews'));
        console.error(e);
    }

    yield put(toggleIsLoading());
}

function* submitCreateJobFormSaga(action) {
    console.log('submit create', action.payload.formValues)
    yield put(closeJobForm())
}

function* submitUpdateJobFormSaga(action) {
    console.log('submit update', action.payload.formValues)
    yield put(closeJobForm())
}

function* toggleJobActivitySaga(action) {
    console.log('toggle job activity', action.payload.jobId)
}

function* deleteJobSaga(action) {
    console.log('delete job', action.payload.jobId)
    yield put(removeJob(action.payload.jobId))
}
