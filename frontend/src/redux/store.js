import {configureStore, getDefaultMiddleware} from "@reduxjs/toolkit";
import {reindexSlice} from "../features/reindex/slice";
import reindexSaga from "../features/reindex/saga";
import createSagaMiddleware from "redux-saga";

const initialSagaMiddleware = createSagaMiddleware();

const reducers = {
    reindex: reindexSlice.reducer
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
