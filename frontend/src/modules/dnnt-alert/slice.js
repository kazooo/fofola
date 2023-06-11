import {createSlice} from '@reduxjs/toolkit';

export const dnntAlertSlice = createSlice({
    name: 'dnntAlert',
    initialState: {
        alerts: [],
        isLoading: false,
        openAlert: null,
        currentPage: 0,
        numFound: 0,
    },
    reducers: {
        setAlerts: (state, action) => {
            state.alerts = action.payload;
        },
        clearAlerts: (state, action) => {
            state.alerts = [];
        },
        toggleIsLoading: (state, action) => {
            state.isLoading = !state.isLoading;
        },
        openAlertWindow: (state, action) => {
            state.openAlert = action.payload;
        },
        closeAlertWindow: (state, action) => {
            state.openAlert = null;
        },
        setCurrentPage: (state, action) => {
            state.currentPage = action.payload;
        },
        setNumFound: (state, action) => {
            state.numFound = action.payload;
        },
    },
});

export const getIsLoading = state => state.dnntAlertModule.isLoading;
export const getAlerts = state => state.dnntAlertModule.alerts;
export const getCurrentPage = state => state.dnntAlertModule.currentPage;
export const getNumFound = state => state.dnntAlertModule.numFound;
export const getOpenAlert = state => state.dnntAlertModule.openAlert;
export const isAlertWindowOpen = state => state.dnntAlertModule.openAlert !== null;

export const {
    setAlerts,
    clearAlerts,
    openAlertWindow,
    closeAlertWindow,
    toggleIsLoading,
    setCurrentPage,
    setNumFound,
} = dnntAlertSlice.actions;

export const createActionType = (actionName) => `${dnntAlertSlice.name}/${actionName}`;
