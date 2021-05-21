import {configureStore, getDefaultMiddleware} from "@reduxjs/toolkit";
import {linkDonatorSlice} from "../features/link-donator/slice";
import {changeAccessSlice} from "../features/access/slice";
import {reindexSlice} from "../features/reindex/slice";
import {deleteSlice} from "../features/delete/slice";
import linkDonatorSaga from "../features/link-donator/saga";
import changeAccessSaga from "../features/access/saga";
import reindexSaga from "../features/reindex/saga";
import deleteSaga from "../features/delete/saga";
import createSagaMiddleware from "redux-saga";

const initialSagaMiddleware = createSagaMiddleware();

const reducers = {
    changeAccess: changeAccessSlice.reducer,
    linkDonator: linkDonatorSlice.reducer,
    reindex: reindexSlice.reducer,
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
initialSagaMiddleware.run(reindexSaga);
initialSagaMiddleware.run(deleteSaga);
