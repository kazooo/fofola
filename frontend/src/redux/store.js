import {configureStore, getDefaultMiddleware} from "@reduxjs/toolkit";
import {linkDonatorSlice} from "../features/link-donator/slice";
import {changeAccessSlice} from "../features/change-access/slice";
import {uuidInfoSlice} from "../features/uuid-info/slice";
import {reindexSlice} from "../features/reindex/slice";
import {linkVcSlice} from "../features/link-vc/slice";
import {deleteSlice} from "../features/delete/slice";

import linkDonatorSaga from "../features/link-donator/saga";
import changeAccessSaga from "../features/change-access/saga";
import uuidInfoSaga from "../features/uuid-info/saga";
import reindexSaga from "../features/reindex/saga";
import linkVcSaga from "../features/link-vc/saga";
import deleteSaga from "../features/delete/saga";
import createSagaMiddleware from "redux-saga";

const initialSagaMiddleware = createSagaMiddleware();

const reducers = {
    changeAccess: changeAccessSlice.reducer,
    linkDonator: linkDonatorSlice.reducer,
    uuidInfo: uuidInfoSlice.reducer,
    reindex: reindexSlice.reducer,
    linkVc: linkVcSlice.reducer,
    delete: deleteSlice.reducer
};

const middlewares = [
    ...getDefaultMiddleware({thunk: false}),
    initialSagaMiddleware
];

export const store = configureStore({
    reducer: reducers,
    middleware: middlewares
});

initialSagaMiddleware.run(changeAccessSaga);
initialSagaMiddleware.run(linkDonatorSaga);
initialSagaMiddleware.run(uuidInfoSaga);
initialSagaMiddleware.run(reindexSaga);
initialSagaMiddleware.run(linkVcSaga);
initialSagaMiddleware.run(deleteSaga);
