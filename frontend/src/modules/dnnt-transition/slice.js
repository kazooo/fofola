import {createSlice} from '@reduxjs/toolkit';

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
    },
});

export const getTransitions = state => state.dnntTransitionModule.transitions;
export const getIsLoading = state => state.dnntTransitionModule.isLoading;
export const getCurrentPage = state => state.dnntTransitionModule.currentPage;
export const getFromDateTime = state => state.dnntTransitionModule.fromDateTime;
export const getToDateTime = state => state.dnntTransitionModule.toDateTime;
export const getInternalUuid = state => state.dnntTransitionModule.internalUuid;
export const getIsPaginatorEnabled = state => state.dnntTransitionModule.numFound > 0;

export const {
    setTransitions, setNumFound, toggleIsLoading,
    setCurrentPage, setFromDateTime, setToDateTime,
    setInternalUuid
} = dnntTransitionSlice.actions;

export const createActionType = actionName => `${dnntTransitionSlice.name}/${actionName}`;
