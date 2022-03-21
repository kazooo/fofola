import {createSlice} from '@reduxjs/toolkit';

export const dnntInfoSlice = createSlice({
    name: 'dnntInfo',
    initialState: {
        info: [],
        isLoading: false,
    },
    reducers: {
        setInfo: (state, action) => {
            state.info = action.payload;
        },
        removeInfo: (state, action) => {
            const {uuid, sourceIdentifier} = action.payload;
            state.info = state.info.filter(info => info.uuid !== uuid && info.sourceIdentifier !== sourceIdentifier);
        },
        clearInfo: (state, action) => {
            state.info = [];
        },
        toggleIsLoading: (state, action) => {
            state.isLoading = !state.isLoading;
        },
    },
});

export const getInfo = state => state.dnntInfo.info;
export const getIsLoading = state => state.dnntInfo.isLoading;

export const {
    setInfo,
    removeInfo,
    clearInfo,
    toggleIsLoading,
} = dnntInfoSlice.actions;

export const createActionType = actionName => `${dnntInfoSlice.name}/${actionName}`;
