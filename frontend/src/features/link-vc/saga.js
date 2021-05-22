import {call, put, takeEvery} from "redux-saga/effects";
import {createAction} from "@reduxjs/toolkit";

import {clearUuids, createActionType, setVcs} from "./slice";
import {request} from "../../redux/superagent";

const CHANGE_VC = createActionType("CHANGE_VC");
const LOAD_VCS = createActionType('LOAD_VCS');

export const changeVc = createAction(CHANGE_VC);
export const loadVirtualCollections = createAction(LOAD_VCS);

export default function* watcherSaga() {
    yield takeEvery(CHANGE_VC, changeVcSaga);
    yield takeEvery(LOAD_VCS, loadVirtualCollectionsSaga);
}

function* changeVcSaga(action) {
    try {
        yield call(() => request
            .post('/internal-processes/new/vc_link')
            .send(action.payload)
        );
    } catch (e) {
        console.error(e);
    } finally {
        yield put(clearUuids());
    }
}

function* loadVirtualCollectionsSaga() {
    try {
        const response = yield call(() => request
            .get('/vc/all')
        );
        yield put(setVcs(response.body))
    } catch (e) {
        console.error(e);
    }
}
