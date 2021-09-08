import {createAction} from "@reduxjs/toolkit";
import {put, takeEvery} from "redux-saga/effects";
import {v4} from 'uuid';

import {addNotification, createActionType} from "./slice";

const INFO_SNACKBAR = createActionType('INFO_SNACKBAR');
const SUCCESS_SNACKBAR = createActionType('SUCCESS_SNACKBAR');
const WARNING_SNACKBAR =createActionType('WARNING_SNACKBAR');
const ERROR_SNACKBAR = createActionType('ERROR_SNACKBAR');

const info = createAction(INFO_SNACKBAR);
const success = createAction(SUCCESS_SNACKBAR);
const warning = createAction(WARNING_SNACKBAR);
const error = createAction(ERROR_SNACKBAR);

export const snackbar = {
    info,
    success,
    warning,
    error,
}

export default function* watcherSaga() {
    yield takeEvery(INFO_SNACKBAR, infoSnackbarSaga);
    yield takeEvery(SUCCESS_SNACKBAR, successSnackbarSaga);
    yield takeEvery(WARNING_SNACKBAR, warningSnackbarSaga);
    yield takeEvery(ERROR_SNACKBAR, errorSnackbarSaga);
}

function* infoSnackbarSaga(action) {
    yield put(addNotification(createSnackbar(action.payload, 'info')));
}

function* successSnackbarSaga(action) {
    yield put(addNotification(createSnackbar(action.payload, 'success')));
}

function* warningSnackbarSaga(action) {
    yield put(addNotification(createSnackbar(action.payload, 'warning')));
}

function* errorSnackbarSaga(action) {
    yield put(addNotification(createSnackbar(action.payload, 'error')));
}

const createSnackbar = (message, variant) => (
    {
        key: v4(),
        variant,
        message,
    }
);
