import {configureStore, getDefaultMiddleware} from "@reduxjs/toolkit";
import {reindexSlice} from "../features/reindex/slice";
import {deleteSlice} from "../features/delete/slice";
import reindexSaga from "../features/reindex/saga";
import deleteSaga from "../features/delete/saga";
import createSagaMiddleware from "redux-saga";

const initialSagaMiddleware = createSagaMiddleware();

const reducers = {
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

initialSagaMiddleware.run(reindexSaga);
initialSagaMiddleware.run(deleteSaga);
