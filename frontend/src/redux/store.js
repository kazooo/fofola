import {configureStore, getDefaultMiddleware} from "@reduxjs/toolkit";
import {snackbarSlice} from "../utils/snack/slice";
import snackbarSaga from '../utils/snack/saga';
import createSagaMiddleware from "redux-saga";
import modules from "../modules";

const sagas = Object.values(modules).map(module => module.saga);
sagas.push(snackbarSaga);

const reducers = Object.entries(modules).map(module => module)
    .reduce((o, [moduleName, moduleComponents]) => ({ ...o, [moduleName]: moduleComponents.reducer}), {});
reducers['snackBarModule'] = snackbarSlice.reducer;

const initialSagaMiddleware = createSagaMiddleware();

const middlewares = [
    ...getDefaultMiddleware({
        thunk: false,
        serializableCheck: {
            ignoredActions: ['setImg/setImg'],
            ignoredActionPaths: ['payload.img'],
            ignoredPaths: ['setImg.img'],
        },
    }),
    initialSagaMiddleware
];

export const store = configureStore({
    reducer: reducers,
    middleware: middlewares
});

sagas.forEach(saga => initialSagaMiddleware.run(saga));
