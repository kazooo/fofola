import {configureStore, getDefaultMiddleware} from "@reduxjs/toolkit";
import createSagaMiddleware from "redux-saga";

import {internalProcessesSlice} from "../features/internal-processes/slice";
import {krameriusProcessSlice} from "../features/kramerius-procesess/slice";
import {perioPartsPublishSlice} from "../features/perio-parts/slice";
import {changeAccessSlice} from "../features/change-access/slice";
import {checkDonatorSlice} from "../features/check-donator/slice";
import {linkDonatorSlice} from "../features/link-donator/slice";
import {solrQuerySlice} from "../features/solr-query/slice";
import {uuidInfoSlice} from "../features/uuid-info/slice";
import {linkDnntSlice} from "../features/link-dnnt/slice";
import {setImgSlice} from "../features/set-image/slice";
import {vcSlice} from "../features/vc-management/slice";
import {reindexSlice} from "../features/reindex/slice";
import {linkVcSlice} from "../features/link-vc/slice";
import {deleteSlice} from "../features/delete/slice";
import {pdfSlice} from "../features/pdf/slice";

import {snackbarSlice} from "../utils/snack/slice";

import internalProcessesSaga from "../features/internal-processes/saga";
import krameriusProcessSaga from "../features/kramerius-procesess/saga";
import publishPerioPartsSaga from "../features/perio-parts/saga";
import checkDonatorSaga from "../features/check-donator/saga";
import changeAccessSaga from "../features/change-access/saga";
import linkDonatorSaga from "../features/link-donator/saga";
import solrQuerySaga from "../features/solr-query/saga";
import uuidInfoSaga from "../features/uuid-info/saga";
import linkDnntSaga from '../features/link-dnnt/saga';
import setImgSaga from "../features/set-image/saga";
import vcSaga from "../features/vc-management/saga";
import reindexSaga from "../features/reindex/saga";
import linkVcSaga from "../features/link-vc/saga";
import deleteSaga from "../features/delete/saga";
import pdfSaga from "../features/pdf/saga";

import snackbarSaga from '../utils/snack/saga';

const initialSagaMiddleware = createSagaMiddleware();

const reducers = {
    perioPartsPublish: perioPartsPublishSlice.reducer,
    internalProcesses: internalProcessesSlice.reducer,
    krameriusProcess: krameriusProcessSlice.reducer,
    checkDonator: checkDonatorSlice.reducer,
    changeAccess: changeAccessSlice.reducer,
    linkDonator: linkDonatorSlice.reducer,
    solrQuery: solrQuerySlice.reducer,
    uuidInfo: uuidInfoSlice.reducer,
    linkDnnt: linkDnntSlice.reducer,
    reindex: reindexSlice.reducer,
    linkVc: linkVcSlice.reducer,
    delete: deleteSlice.reducer,
    setImg: setImgSlice.reducer,
    pdf: pdfSlice.reducer,
    vc: vcSlice.reducer,

    snackbar: snackbarSlice.reducer,
};

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

const sagas = [
    internalProcessesSaga,
    publishPerioPartsSaga,
    krameriusProcessSaga,
    checkDonatorSaga,
    changeAccessSaga,
    linkDonatorSaga,
    solrQuerySaga,
    uuidInfoSaga,
    linkDnntSaga,
    reindexSaga,
    linkVcSaga,
    deleteSaga,
    setImgSaga,
    pdfSaga,
    vcSaga,

    snackbarSaga,
];

sagas.forEach(saga => initialSagaMiddleware.run(saga));
