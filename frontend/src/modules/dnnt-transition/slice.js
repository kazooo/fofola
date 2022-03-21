import {createSlice} from '@reduxjs/toolkit';

import {ExtendedFieldValue, SugoSessionRequestor} from '../constants';

export const dnntTransitionSlice = createSlice({
    name: 'dnntTransition',
    initialState: {
        transitions: [],
        isLoading: false,
        currentPage: 0,
        numFound: 0,
        fromDateTime: null,
        toDateTime: null,
        internalUuid: '',
        model: ExtendedFieldValue.ANY.value,
        access: ExtendedFieldValue.ANY.value,
        cnb: '',
        sourceIdentifier: '',
        sourceUuid: '',
        requestor: SugoSessionRequestor.Any.value,
    },
    reducers: {
        setTransitions: (state, action) => {
            state.transitions = action.payload;
        },
        clearTransitions: (state, action) => {
            state.transitions = [];
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
        setInternalUuid: (state, action) => {
            state.internalUuid = action.payload;
        },
        setName: (state, action) => {
            state.name = action.payload;
        },
        setModel: (state, action) => {
            state.model = action.payload;
        },
        setAccess: (state, action) => {
            state.access = action.payload;
        },
        setCnb: (state, action) => {
            state.cnb = action.payload;
        },
        setSourceIdentifier: (state, action) => {
            state.sourceIdentifier = action.payload;
        },
        setSourceUuid: (state, action) => {
            state.sourceUuid = action.payload;
        },
        setRequestor: (state, action) => {
            state.requestor = action.payload;
        },
    },
});

export const getTransitions = state => state.dnntTransitionModule.transitions;
export const getIsLoading = state => state.dnntTransitionModule.isLoading;
export const getCurrentPage = state => state.dnntTransitionModule.currentPage;
export const getFromDateTime = state => state.dnntTransitionModule.fromDateTime;
export const getToDateTime = state => state.dnntTransitionModule.toDateTime;
export const getInternalUuid = state => state.dnntTransitionModule.internalUuid;
export const getModel = state => state.dnntTransitionModule.model;
export const getAccess = state => state.dnntTransitionModule.access;
export const getCnb = state => state.dnntTransitionModule.cnb;
export const getSourceIdentifier = state => state.dnntTransitionModule.sourceIdentifier;
export const getSourceUuid = state => state.dnntTransitionModule.sourceUuid;
export const getRequestor = state => state.dnntTransitionModule.requestor;
export const getIsPaginatorEnabled = state => state.dnntTransitionModule.numFound > 0;

export const {
    setTransitions, setNumFound, toggleIsLoading,
    setCurrentPage, setFromDateTime, setToDateTime,
    setInternalUuid, setModel,
    setAccess, setCnb, setSourceIdentifier, setSourceUuid, setRequestor
} = dnntTransitionSlice.actions;

export const createActionType = actionName => `${dnntTransitionSlice.name}/${actionName}`;
