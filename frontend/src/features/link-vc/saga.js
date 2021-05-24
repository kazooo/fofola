import {call, put, takeEvery} from "redux-saga/effects";
import {createAction} from "@reduxjs/toolkit";
import {clearUuids} from "./slice";
import {request} from "../../redux/superagent";

const CHANGE_VC = "CHANGE_VC";

export const changeVc = createAction(CHANGE_VC);

export default function* watcherSaga() {
    yield takeEvery(CHANGE_VC, changeVcSaga);
}

function* changeVcSaga(action) {
    try {
        yield call(() => request
            .post('/internal-processes/new/vc_link')
            .send(action.payload)
        );
        yield put(clearUuids());
    } catch (e) {
        console.error(e);
    }
}
