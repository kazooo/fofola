import {createAction} from "@reduxjs/toolkit";
import {call, put, takeEvery} from "redux-saga/effects";
import {request} from "../../utils/superagent";
import {clearAll} from "./slice";

const SET_IMAGE = 'SET_IMAGE';

export const setImg = createAction(SET_IMAGE);

export default function* watcherSaga() {
    yield takeEvery(SET_IMAGE, setImgSaga);
}

function* setImgSaga(action) {
    try {
        yield call(() => request
            .post('/internal-processes/new/img_editing')
            .field('uuid', action.payload.uuid)
            .field('datastream', action.payload.datastream)
            .attach('image', action.payload.img)
        );
        yield put(clearAll())
    } catch (e) {
        console.error(e);
    }
}
