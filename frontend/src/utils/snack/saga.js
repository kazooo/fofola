import {createAction} from "@reduxjs/toolkit";
import {put, takeEvery, call} from "redux-saga/effects";
import i18n from "i18next";
import {v4} from 'uuid';

import {i18LoadedPromise} from "../i18n";

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
    yield call(() => i18LoadedPromise);
    const t = i18n.getFixedT(action.payload.language);
    yield put(addNotification(createSnackbar(t(action.payload), 'info')));
}

function* successSnackbarSaga(action) {
    yield call(() => i18LoadedPromise);
    const t = i18n.getFixedT(action.payload.language);
    yield put(addNotification(createSnackbar(t(action.payload), 'success')));
}

function* warningSnackbarSaga(action) {
    yield call(() => i18LoadedPromise);
    const t = i18n.getFixedT(action.payload.language);
    yield put(addNotification(createSnackbar(t(action.payload), 'warning')));
}

function* errorSnackbarSaga(action) {
    yield call(() => i18LoadedPromise);
    const t = i18n.getFixedT(action.payload.language);
    yield put(addNotification(createSnackbar(t(action.payload), 'error')));
}

const createSnackbar = (message, variant) => (
    {
        key: v4(),
        variant,
        message,
    }
);
