import {createSlice} from "@reduxjs/toolkit";

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
        toggleIsLoading: (state, action) => {
            state.isLoading = !state.isLoading;
        },
    },
});

export const getInfo = state => state.dnntInfo.info;
export const getIsLoading = state => state.dnntInfo.isLoading;

export const {
    setInfo,
    toggleIsLoading,
} = dnntInfoSlice.actions;

export const createActionType = actionName => `${dnntInfoSlice.name}/${actionName}`;
