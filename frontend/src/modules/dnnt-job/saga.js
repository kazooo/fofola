import {createAction} from '@reduxjs/toolkit';
import {call, put, takeLatest, select} from 'redux-saga/effects';

import {
    createActionType,
    getJob,
    removeJob,
    setJob,
    setJobs,
    toggleIsLoading,
    openUpdateJobForm as openUpdateJobFormSlice,
} from './slice';
import {request, translateServerError} from '../../utils/superagent';
import {snackbar} from '../../utils/snack/saga';

const REQUEST_JOB_PREVIEWS = createActionType('REQUEST_JOB_PREVIEWS');
const SUBMIT_CREATE_JOB_FORM = createActionType('SUBMIT_CREATE_JOB_FORM');
const OPEN_UPDATE_JOB_FORM = createActionType('OPEN_UPDATE_JOB_FORM');
const SUBMIT_UPDATE_JOB_FORM = createActionType('SUBMIT_UPDATE_JOB_FORM');
const TOGGLE_JOB_ACTIVITY = createActionType('TOGGLE_JOB_ACTIVITY');
const DELETE_JOB = createActionType('DELETE_JOB');
const TRIGGER_JOB = createActionType('TRIGGER_JOB');

export const requestJobPreviews = createAction(REQUEST_JOB_PREVIEWS);
export const submitCreateJobForm = createAction(SUBMIT_CREATE_JOB_FORM, (formValues, closeCallback) => ({payload: {formValues, closeCallback}}));
export const openUpdateJobForm = createAction(OPEN_UPDATE_JOB_FORM, (jobId) => ({payload: {jobId}}));
export const submitUpdateJobForm = createAction(SUBMIT_UPDATE_JOB_FORM, (formValues, closeCallback) => ({payload: {formValues, closeCallback}}));
export const toggleJobActivity = createAction(TOGGLE_JOB_ACTIVITY, (jobId) => ({payload: {jobId}}));
export const deleteJob = createAction(DELETE_JOB, (jobId) => ({payload: {jobId}}));
export const triggerJob = createAction(TRIGGER_JOB, (jobId) => ({payload: {jobId}}));

const SUGO_JOB_ENDPOINT = '/sugo/job';

export default function* watcherSaga() {
    yield takeLatest(REQUEST_JOB_PREVIEWS, requestJobPreviewsSaga);
    yield takeLatest(SUBMIT_CREATE_JOB_FORM, submitCreateJobFormSaga);
    yield takeLatest(OPEN_UPDATE_JOB_FORM, openUpdateJobFormSaga);
    yield takeLatest(SUBMIT_UPDATE_JOB_FORM, submitUpdateJobFormSaga);
    yield takeLatest(TOGGLE_JOB_ACTIVITY, toggleJobActivitySaga);
    yield takeLatest(DELETE_JOB, deleteJobSaga);
    yield takeLatest(TRIGGER_JOB, triggerJobSaga);
};

function* requestJobPreviewsSaga(action) {
    yield put(toggleIsLoading());

    try {
        const payload = yield call(() => request.get(SUGO_JOB_ENDPOINT));
        const jobs = payload.body['entities'];
        if (jobs) {
            yield put(setJobs(jobs));
        }
    } catch (e) {
        yield put(snackbar.error('feature.dnntJobs.request.job.loadPreviews.error.cant'));
        console.error(e);
    }

    yield put(toggleIsLoading());
}

function* submitCreateJobFormSaga(action) {
    try {
        const response = yield call(() =>
            request
                .post(SUGO_JOB_ENDPOINT)
                .send(action.payload.formValues)
        );

        if (response.statusCode === 200 || response.statusCode === 201 || response.statusCode === 202) {
            yield put(snackbar.success('feature.dnntJobs.request.job.create.success'));
            /* close job form only if it was created, keep open otherwise */
            yield call(action.payload.closeCallback, true);
            yield call(requestJobPreviewsSaga);
        } else {
            yield put(snackbar.error(translateServerError(response)));
        }
    } catch (e) {
        yield put(snackbar.error('feature.dnntJobs.request.job.create.error.cant'));
        console.error(e);
    }
}

function* openUpdateJobFormSaga(action) {
    try {
        const response = yield call(() => request.get(`${SUGO_JOB_ENDPOINT}/${action.payload.jobId}`));
        if (response.statusCode === 200) {
            const job = response.body;
            yield put(openUpdateJobFormSlice(job));
        } else {
            yield put(snackbar.error(translateServerError(response)));
        }
    } catch (e) {
        yield put(snackbar.error('feature.dnntJobs.request.job.load.error.cant'));
        console.error(e);
    }
}

function* submitUpdateJobFormSaga(action) {
    try {
        const response = yield call(() =>
            request
                .put(SUGO_JOB_ENDPOINT)
                .send(action.payload.formValues)
        );

        if (response.statusCode === 202 || response.statusCode === 200) {
            yield put(snackbar.success('feature.dnntJobs.request.job.update.success'));
            yield put(setJob(response.body));
            /* close job form only if it was updated, keep open otherwise */
            yield call(action.payload.closeCallback, true);
        } else {
            yield put(snackbar.error(translateServerError(response)));
        }
    } catch (e) {
        yield put(snackbar.error('feature.dnntJobs.request.job.update.error.cant'));
        console.error(e);
    }
}

function* toggleJobActivitySaga(action) {
    try {
        const response = yield call(() => request.put(`${SUGO_JOB_ENDPOINT}/${action.payload.jobId}`));

        if (response.statusCode === 200) {
            const job = response.body;
            if (job.active) {
                yield put(snackbar.success('feature.dnntJobs.request.job.toggle.success.activate'));
            } else {
                yield put(snackbar.success('feature.dnntJobs.request.job.toggle.success.deactivate'));
            }
            yield put(setJob(job));
        } else {
            yield put(snackbar.error(translateServerError(response)));
        }
    } catch (e) {
        const active = yield select(getJob(action.payload.jobId));
        if (active) {
            yield put(snackbar.error('feature.dnntJobs.request.job.toggle.error.cantDeactivate'));
        } else {
            yield put(snackbar.error('feature.dnntJobs.request.job.toggle.error.cantActivate'));
        }
        console.error(e);
    }
}

function* deleteJobSaga(action) {
    try {
        const response = yield call(() => request.delete(`${SUGO_JOB_ENDPOINT}/${action.payload.jobId}`));

        if (response.statusCode === 200) {
            yield put(snackbar.success('feature.dnntJobs.request.job.delete.success'));
            yield put(removeJob(action.payload.jobId))
        } else {
            yield put(snackbar.error(translateServerError(response)));
        }
    } catch (e) {
        yield put(snackbar.error('feature.dnntJobs.request.job.delete.error.cant'));
        console.error(e);
    }
}

function* triggerJobSaga(action) {
    try {
        const response = yield call(() => request.put(`${SUGO_JOB_ENDPOINT}/${action.payload.jobId}/trigger`));
        if (response.statusCode === 200 || response.statusCode === 202) {
            yield put(snackbar.success('feature.dnntJobs.request.job.trigger.success'));
        } else {
            yield put(snackbar.error(translateServerError(response)));
        }
    } catch (e) {
        yield put(snackbar.error('feature.dnntJobs.request.job.trigger.error.cant'));
        console.error(e);
    }
}
