import {configureStore, getDefaultMiddleware} from "@reduxjs/toolkit";
import {changeAccessSlice} from "../features/access/slice";
import {reindexSlice} from "../features/reindex/slice";
import {deleteSlice} from "../features/delete/slice";
import changeAccessSaga from "../features/access/saga";
import reindexSaga from "../features/reindex/saga";
import deleteSaga from "../features/delete/saga";
import createSagaMiddleware from "redux-saga";

const initialSagaMiddleware = createSagaMiddleware();

const reducers = {
    changeAccess: changeAccessSlice.reducer,
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
initialSagaMiddleware.run(reindexSaga);
initialSagaMiddleware.run(deleteSaga);
