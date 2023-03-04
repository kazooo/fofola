import {createAction} from "@reduxjs/toolkit";
import {put, takeEvery, call} from "redux-saga/effects";
import i18n from "i18next";
import {v4} from 'uuid';

import {i18nLoadedPromise} from "../i18n";

import {addNotification, createActionType} from "./slice";

const INFO_SNACKBAR = createActionType('INFO_SNACKBAR');
const SUCCESS_SNACKBAR = createActionType('SUCCESS_SNACKBAR');
const WARNING_SNACKBAR =createActionType('WARNING_SNACKBAR');
const ERROR_SNACKBAR = createActionType('ERROR_SNACKBAR');

const info = createAction(INFO_SNACKBAR, (message) => ({payload: {message}}));
const success = createAction(SUCCESS_SNACKBAR, (message) => ({payload: {message}}));
const warning = createAction(WARNING_SNACKBAR, (message) => ({payload: {message}}));
const error = createAction(ERROR_SNACKBAR, (message) => ({payload: {message}}));

export const snackbar = {
    info: (message) => info(message),
    success: (message) => success(message),
    warning: (message) => warning(message),
    error: (message) => error(message),
}

export default function* watcherSaga() {
    yield takeEvery(INFO_SNACKBAR, infoSnackbarSaga);
    yield takeEvery(SUCCESS_SNACKBAR, successSnackbarSaga);
    yield takeEvery(WARNING_SNACKBAR, warningSnackbarSaga);
    yield takeEvery(ERROR_SNACKBAR, errorSnackbarSaga);
}

function* infoSnackbarSaga(action) {
    yield call(() => i18nLoadedPromise);
    yield put(addNotification(createSnackbar(i18n.t(action.payload.message), 'info')));
}

function* successSnackbarSaga(action) {
    yield call(() => i18nLoadedPromise);
    yield put(addNotification(createSnackbar(i18n.t(action.payload.message), 'success')));
}

function* warningSnackbarSaga(action) {
    yield call(() => i18nLoadedPromise);
    yield put(addNotification(createSnackbar(i18n.t(action.payload.message), 'warning')));
}

function* errorSnackbarSaga(action) {
    yield call(() => i18nLoadedPromise);
    yield put(addNotification(createSnackbar(i18n.t(action.payload.message), 'error')));
}

const createSnackbar = (message, variant) => (
    {
        key: v4(),
        variant,
        message,
    }
);
