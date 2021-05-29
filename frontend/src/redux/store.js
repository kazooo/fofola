import {configureStore, getDefaultMiddleware} from "@reduxjs/toolkit";
import {krameriusProcessSlice} from "../features/kramerius-procesess/slice";
import {changeAccessSlice} from "../features/change-access/slice";
import {checkDonatorSlice} from "../features/check-donator/slice";
import {linkDonatorSlice} from "../features/link-donator/slice";
import {uuidInfoSlice} from "../features/uuid-info/slice";
import {reindexSlice} from "../features/reindex/slice";
import {linkVcSlice} from "../features/link-vc/slice";
import {deleteSlice} from "../features/delete/slice";

import internalProcessesSaga from "../features/internal-processes/saga";
import krameriusProcessSaga from "../features/kramerius-procesess/saga";
import checkDonatorSaga from "../features/check-donator/saga";
import changeAccessSaga from "../features/change-access/saga";
import linkDonatorSaga from "../features/link-donator/saga";
import uuidInfoSaga from "../features/uuid-info/saga";
import reindexSaga from "../features/reindex/saga";
import linkVcSaga from "../features/link-vc/saga";
import deleteSaga from "../features/delete/saga";
import createSagaMiddleware from "redux-saga";
import {internalProcessesSlice} from "../features/internal-processes/slice";

const initialSagaMiddleware = createSagaMiddleware();

const reducers = {
    internalProcesses: internalProcessesSlice.reducer,
    krameriusProcess: krameriusProcessSlice.reducer,
    checkDonator: checkDonatorSlice.reducer,
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

initialSagaMiddleware.run(internalProcessesSaga);
initialSagaMiddleware.run(krameriusProcessSaga);
initialSagaMiddleware.run(checkDonatorSaga);
initialSagaMiddleware.run(changeAccessSaga);
initialSagaMiddleware.run(linkDonatorSaga);
initialSagaMiddleware.run(uuidInfoSaga);
initialSagaMiddleware.run(reindexSaga);
initialSagaMiddleware.run(linkVcSaga);
initialSagaMiddleware.run(deleteSaga);
