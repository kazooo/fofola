import {configureStore, getDefaultMiddleware} from "@reduxjs/toolkit";

const reducers = {

};

const middlewares = [
    ...getDefaultMiddleware({thunk: false})
];

export const store = configureStore({
    reducer: reducers,
    middleware: middlewares
});
