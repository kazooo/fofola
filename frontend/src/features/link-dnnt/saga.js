import {clearUuids, createActionType} from "./slice";
import {createAction} from "@reduxjs/toolkit";
import {call, put, takeEvery} from "redux-saga/effects";
import {request} from "../../utils/superagent";
import {snackbar} from "../../utils/snack/saga";
import {getCantLinkDnntLabelMsg, getLinkDnntLabelMsg} from "../../utils/constants/messages";

const CHANGE_LABEL = createActionType('CHANGE_LABEL');

export const changeLabel = createAction(CHANGE_LABEL);

export default function* watcherSaga() {
    yield takeEvery(CHANGE_LABEL, changeLabelSaga);
}

function* changeLabelSaga(action) {
    const body = action.payload;
    try {
        yield call(() => request
            .post("/internal-processes/new/dnnt_link")
            .send(body)
        );
        yield put(snackbar.success(getLinkDnntLabelMsg(body.uuids.length)));
    } catch (e) {
        yield put(snackbar.error(getCantLinkDnntLabelMsg(body.uuids.length)));
        console.error(e);
    } finally {
        yield put(clearUuids());
    }
}
