import {createSlice} from "@reduxjs/toolkit";
import {SugoSessionDirectionExtended, SugoSessionOperation, SugoSessionRequestor, SugoSessionStatuses} from '../constants';

export const dnntSessionSlice = createSlice({
    name: 'dnntSession',
    initialState: {
        sessions: [],
        isLoading: false,
        currentPage: 0,
        numFound: 0,
        fromDateTime: null,
        toDateTime: null,
        direction: SugoSessionDirectionExtended.Any.value,
        requestor: SugoSessionRequestor.Any.value,
        operation: SugoSessionOperation.Any.value,
        status: SugoSessionStatuses.Any.value,
    },
    reducers: {
        setSessions: (state, action) => {
            state.sessions = action.payload;
        },
        clearSessions: (state, action) => {
            state.sessions = [];
        },
        toggleIsLoading: (state, action) => {
            state.isLoading = !state.isLoading;
        },
        setCurrentPage: (state, action) => {
            state.currentPage = action.payload;
        },
        setNumFound: (state, action) => {
            state.numFound = action.payload;
        },
        setFromDateTime: (state, action) => {
            state.fromDateTime = action.payload;
        },
        setToDateTime: (state, action) => {
            state.toDateTime = action.payload;
        },
        setDirection: (state, action) => {
            state.direction = action.payload;
        },
        setRequestor: (state, action) => {
            state.requestor = action.payload;
        },
        setOperation: (state, action) => {
            state.operation = action.payload;
        },
        setStatus: (state, action) => {
            state.status = action.payload;
        },
    }
});

export const getIsLoading = state => state.dnntSessionModule.isLoading;
export const getSessions = state => state.dnntSessionModule.sessions;
export const getCurrentPage = state => state.dnntSessionModule.currentPage;
export const getFromDateTime = state => state.dnntSessionModule.fromDateTime;
export const getToDateTime = state => state.dnntSessionModule.toDateTime;
export const getDirection = state => state.dnntSessionModule.direction;
export const getRequestor = state => state.dnntSessionModule.requestor;
export const getOperation = state => state.dnntSessionModule.operation;
export const getStatus = state => state.dnntSessionModule.status;
export const getIsPaginatorEnabled = state => state.dnntTransitionModule.numFound > 0;

export const {
    setSessions, setNumFound, clearSessions, toggleIsLoading,
    setCurrentPage, setOperation, setRequestor,
    setStatus, setToDateTime, setFromDateTime, setDirection
} = dnntSessionSlice.actions;

export const createActionType = actionName => `${dnntSessionSlice.name}/${actionName}`;
