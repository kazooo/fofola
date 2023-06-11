import {call, put, select, takeEvery} from 'redux-saga/effects';
import {createAction} from '@reduxjs/toolkit';

import {request} from 'utils/superagent';
import {snackbar} from 'utils/snack/saga';
import {
    cantCreateVcMsg,
    cantDeleteVcMsg,
    cantLoadVcMsg,
    cantUpdateVcMsg,
    successCreateVcMsg,
    successDeleteVcMsg,
    successUpdateVcMsg
} from 'utils/constants/messages';

import {
    createActionType,
    getVcs,
    setIsLoading,
    setIsLoadingError,
    setVcs,
} from './slice';

const LOAD_VCS = createActionType('LOAD_VCS');
const CREATE_VC = createActionType('CREATE_VC');
const UPDATE_VC = createActionType('UPDATE_VC');
const DELETE_VC = createActionType('DELETE_VC');

export const loadVirtualCollections = createAction(LOAD_VCS);
export const createVirtualCollection = createAction(CREATE_VC);
export const updateVirtualCollection = createAction(UPDATE_VC);
export const deleteVirtualCollection = createAction(DELETE_VC);

export default function* watcherSaga() {
    yield takeEvery(LOAD_VCS, loadVirtualCollectionsSaga);
    yield takeEvery(CREATE_VC, createVirtualCollectionSaga);
    yield takeEvery(UPDATE_VC, updateVirtualCollectionSaga);
    yield takeEvery(DELETE_VC, deleteVirtualCollectionSaga);
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
        const formData = yield createVcRequest(action);
        yield call(() => request
            .post('/vc')
            .send(formData)
        );
        yield put(snackbar.success(successCreateVcMsg));
    } catch (e) {
        yield put(snackbar.error(cantCreateVcMsg));
        console.error(e);
    }
}

function* updateVirtualCollectionSaga(action) {
    try {
        const formData = yield createVcRequest(action);
        yield call(() => request
            .put('/vc')
            .send(formData)
        );
        yield put(snackbar.success(successUpdateVcMsg));
    } catch (e) {
        yield put(snackbar.error(cantUpdateVcMsg));
        console.error(e);
    }
}

function* deleteVirtualCollectionSaga(action) {
    try {
        yield call(() => request.delete('/vc').send(action.payload));
        const vcs = yield select(getVcs);
        const uuid = action.payload.uuid;
        const filteredVcs = vcs.filter(value => value.uuid !== uuid);
        setVcs(filteredVcs);
        yield put(snackbar.success(successDeleteVcMsg));
    } catch (e) {
        yield put(snackbar.error(cantDeleteVcMsg));
        console.error(e);
    }
}

function createVcRequest(action) {
    const {uuid, nameCz, nameEn, descriptionCz, descriptionEn, fullImg, thumbImg} = action.payload;

    const formData = new FormData();

    formData.append('fullImg', fullImg);
    formData.append('thumbImg', thumbImg);

    formData.append('vcData', new Blob([
            JSON.stringify({
                    'uuid': uuid,
                    'nameCz': nameCz,
                    'nameEn': nameEn,
                    'descriptionCz': descriptionCz,
                    'descriptionEn': descriptionEn,
                }
            )],
        {
            type: 'application/json'
        })
    );

    return formData;
}
