import {createAction} from '@reduxjs/toolkit';
import {put, takeEvery} from 'redux-saga/effects';
import {v4} from 'uuid';

import {addNotification, createActionType} from './slice';

const INFO_SNACKBAR = createActionType('INFO_SNACKBAR');
const SUCCESS_SNACKBAR = createActionType('SUCCESS_SNACKBAR');
const WARNING_SNACKBAR =createActionType('WARNING_SNACKBAR');
const ERROR_SNACKBAR = createActionType('ERROR_SNACKBAR');

const info = createAction(INFO_SNACKBAR, (message, props) => ({payload: {message, props}}));
const success = createAction(SUCCESS_SNACKBAR, (message, props) => ({payload: {message, props}}));
const warning = createAction(WARNING_SNACKBAR, (message, props) => ({payload: {message, props}}));
const error = createAction(ERROR_SNACKBAR, (message, props) => ({payload: {message, props}}));

export const snackbar = {
    info: (message, props) => info(message, props),
    success: (message, props) => success(message, props),
    warning: (message, props) => warning(message, props),
    error: (message, props) => error(message, props),
}

export default function* watcherSaga() {
    yield takeEvery(INFO_SNACKBAR, infoSnackbarSaga);
    yield takeEvery(SUCCESS_SNACKBAR, successSnackbarSaga);
    yield takeEvery(WARNING_SNACKBAR, warningSnackbarSaga);
    yield takeEvery(ERROR_SNACKBAR, errorSnackbarSaga);
}

function* infoSnackbarSaga(action) {
    yield put(addNotification(createSnackbar(action.payload.message, action.payload.props, 'info')));
}

function* successSnackbarSaga(action) {
    yield put(addNotification(createSnackbar(action.payload.message, action.payload.props, 'success')));
}

function* warningSnackbarSaga(action) {
    yield put(addNotification(createSnackbar(action.payload.message, action.payload.props, 'warning')));
}

function* errorSnackbarSaga(action) {
    yield put(addNotification(createSnackbar(action.payload.message, action.payload.props, 'error')));
}

const createSnackbar = (message, props, variant) => (
    {
        key: v4(),
        message,
        props,
        variant,
    }
);
