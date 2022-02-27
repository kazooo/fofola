import {call, put, select, takeEvery} from "redux-saga/effects";
import {
    createActionType,
    getCurrentPage,
    getDirection,
    getFromDateTime, getOperation,
    getRequestor, getStatus, getToDateTime,
    setSessions,
    toggleIsLoading
} from "./slice";
import {createAction} from "@reduxjs/toolkit";
import {request} from "../../utils/superagent";
import {snackbar} from "../../utils/snack/saga";
import {cantLoadNextPage} from "../../utils/constants/messages";
import moment from "moment";

const DATE_PATTERN = "hh:mm DD/MM/YYYY";
const OUTPUT_DATE_PATTERN = "YYYY-MM-DDThh:mm:ss.sss";

const REQUEST_CURRENT_PAGE = createActionType("REQUEST_CURRENT_PAGE");

export const requestSessionPage = createAction(REQUEST_CURRENT_PAGE);

export default function* watcherSaga() {
    yield takeEvery(REQUEST_CURRENT_PAGE, requestWithLoading);
};

function* requestWithLoading(action) {
    yield put(toggleIsLoading());

    const page = yield select(getCurrentPage);
    const requestor = yield select(getRequestor);
    const direction = yield select(getDirection);
    const operation = yield select(getOperation);
    const status = yield select(getStatus);
    const fromDateTime = yield select(getFromDateTime);
    const toDateTime = yield select(getToDateTime);

    /* filter 'any' values */
    const queryParams = Object.entries(
        {
            page,
            requestor,
            direction,
            operation,
            status,
            from: fromDateTime && moment(fromDateTime, DATE_PATTERN).format(OUTPUT_DATE_PATTERN),
            to: toDateTime && moment(toDateTime, DATE_PATTERN).format(OUTPUT_DATE_PATTERN)
        }
    ).filter(([_, value]) => value !== 'any' && value !== '' && value !== null);

    try {
        const payload = yield call(() => request
            .get("/sugo/sessions")
            .query(Object.fromEntries(queryParams))
        );
        const sessions = payload.body['sessions'];
        yield put(setSessions(sessions));
    } catch (e) {
        yield put(snackbar.error(cantLoadNextPage));
        console.error(e);
    }

    yield put(toggleIsLoading());
}
