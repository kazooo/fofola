import {call, put, takeEvery} from "redux-saga/effects";
import {createAction} from "@reduxjs/toolkit";

import {clearUuids, createActionType, setIsLoading, setIsLoadingError, setVcs} from "./slice";
import {request} from "../../redux/superagent";
import {snackbar} from "../../utils/snack/saga";
import {cantLoadVcMsg, getCantVcMsg, getLinkVcMsg} from "../../utils/constants/messages";

const CHANGE_VC = createActionType("CHANGE_VC");
const LOAD_VCS = createActionType('LOAD_VCS');

export const changeVc = createAction(CHANGE_VC);
export const loadVirtualCollections = createAction(LOAD_VCS);

export default function* watcherSaga() {
    yield takeEvery(CHANGE_VC, changeVcSaga);
    yield takeEvery(LOAD_VCS, loadVirtualCollectionsSaga);
}

function* changeVcSaga(action) {
    const uuids = action.payload;
    try {
        yield call(() => request
            .post('/internal-processes/new/vc_link')
            .send(action.payload)
        );
        yield put(snackbar.success(getLinkVcMsg(uuids.length)));
    } catch (e) {
        yield put(snackbar.error(getCantVcMsg(uuids.length)));
        console.error(e);
    } finally {
        yield put(clearUuids());
    }
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
