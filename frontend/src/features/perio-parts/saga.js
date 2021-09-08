import {takeEvery, call, put} from "redux-saga/effects";
import {createAction} from "@reduxjs/toolkit";

import {request} from "../../redux/superagent";
import {clearUuids, createActionType} from "./slice";
import {snackbar} from "../../utils/snack/saga";
import {getCantPerioPartsMsg, getPerioPartsMsg} from "../../utils/constants/messages";

const PUBLISH_PERIO_PARTS = createActionType("PUBLISH_PERIO_PARTS");

export const publishPerioParts = createAction(PUBLISH_PERIO_PARTS);

export default function* watcherSaga() {
    yield takeEvery(PUBLISH_PERIO_PARTS, publishPerioPartsSaga);
}

function* publishPerioPartsSaga(action) {
    const body = action.payload;
    try {
        yield call(() => request
            .post("/internal-processes/new/perio_parts_pub")
            .send(body)
        );
        yield put(snackbar.success(getPerioPartsMsg(body.root_uuids.length)));
    } catch (e) {
        yield put(snackbar.error(getCantPerioPartsMsg(body.root_uuids.length)));
        console.error(e);
    } finally {
        yield put(clearUuids())
    }
}
