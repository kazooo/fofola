import {takeEvery, call, put} from "redux-saga/effects";
import {createAction} from "@reduxjs/toolkit";

import {request} from "../../redux/superagent";
import {clearUuids, createActionType} from "./slice";

const PUBLISH_PERIO_PARTS = createActionType("PUBLISH_PERIO_PARTS");

export const publishPerioParts = createAction(PUBLISH_PERIO_PARTS);

export default function* watcherSaga() {
    yield takeEvery(PUBLISH_PERIO_PARTS, publishPerioPartsSaga);
}

function* publishPerioPartsSaga(action) {
    try {
        yield call(() => request
            .post("/internal-processes/new/perio_parts_pub")
            .send(action.payload)
        );
    } catch (e) {
        console.error(e);
    } finally {
        yield put(clearUuids())
    }
}
