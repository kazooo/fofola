import {call, put, takeEvery} from "redux-saga/effects";
import {createAction} from "@reduxjs/toolkit";

import {request} from "../../redux/superagent";
import {
    createActionType,
    setIsLoading,
    setIsLoadingError,
    setVcs,
} from "./slice";
import {snackbar} from "../../utils/snack/saga";
import {
    cantCreateVcMsg,
    cantLoadVcMsg,
    cantUpdateVcMsg,
    successCreateVcMsg,
    successUpdateVcMsg
} from "../../utils/constants/messages";

const LOAD_VCS = createActionType('LOAD_VCS');
const CREATE_VC = createActionType('CREATE_VC');
const UPDATE_VC = createActionType('UPDATE_VC');

export const loadVirtualCollections = createAction(LOAD_VCS);
export const createVirtualCollection = createAction(CREATE_VC);
export const updateVirtualCollection = createAction(UPDATE_VC);

export default function* watcherSaga() {
    yield takeEvery(LOAD_VCS, loadVirtualCollectionsSaga);
    yield takeEvery(CREATE_VC, createVirtualCollectionSaga);
    yield takeEvery(UPDATE_VC, updateVirtualCollectionSaga);
}

function* loadVirtualCollectionsSaga() {
    try {
        yield put(setIsLoading(true));
        const response = yield call(() => request
            .get('/vc/all')
        );
        yield put(setVcs(response.body));
        yield put(setIsLoadingError(false));
    } catch (e) {
        yield put(snackbar.error(cantLoadVcMsg));
        console.error(e);
        yield put(setIsLoadingError(true));
    } finally {
        yield put(setIsLoading(false));
    }
}

function* createVirtualCollectionSaga(action) {
    try {
        yield call(() => request
            .post('/vc')
            .send(action.payload)
        );
        yield put(snackbar.success(successCreateVcMsg));
    } catch (e) {
        yield put(snackbar.error(cantCreateVcMsg));
        console.error(e);
    }
}

function* updateVirtualCollectionSaga(action) {
    try {
        yield call(() => request
            .put('/vc')
            .send(action.payload)
        );
        yield put(snackbar.success(successUpdateVcMsg));
    } catch (e) {
        yield put(snackbar.error(cantUpdateVcMsg));
        console.error(e);
    }
}
