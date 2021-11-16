import {takeEvery, call, put} from "redux-saga/effects";
import {createAction} from "@reduxjs/toolkit";
import {request} from "../../utils/superagent";
import {clearUuids, createActionType} from "./slice";
import {snackbar} from "../../utils/snack/saga";
import {getCantLinkDonatorLabelMsg, getLinkDonatorLabelMsg} from "../../utils/constants/messages";

const CHANGE_DONATOR = createActionType("CHANGE_DONATOR");

export const changeDonator = createAction(CHANGE_DONATOR);

export default function* watcherSaga() {
    yield takeEvery(CHANGE_DONATOR, changeDonatorSaga);
}

function* changeDonatorSaga(action) {
    const body = action.payload;
    try {
        yield call(() => request
            .post("/internal-processes/new/donator_link")
            .send(body)
        );
        yield put(snackbar.success(getLinkDonatorLabelMsg(body.uuids.length)));
    } catch (e) {
        yield put(snackbar.error(getCantLinkDonatorLabelMsg(body.uuids.length)));
        console.error(e);
    } finally {
        yield put(clearUuids())
    }
}
