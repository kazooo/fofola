import {createSlice} from "@reduxjs/toolkit";

export const vcSlice = createSlice({
    name: 'vcSlice',
    initialState: {
        vcs: [],
        isLoading: false,
        isLoadingError: false,
    },
    reducers: {
        setVcs: (state, action) => {
            state.vcs = action.payload;
        },
        setIsLoading: (state, action) => {
            state.isLoading = action.payload;
        },
        setIsLoadingError: (state, action) => {
            state.isLoadingError = action.payload;
        }
    }
});

export const getVcs = state => state.vcModule.vcs;
export const getIsLoading = state => state.vcModule.isLoading;
export const getIsLoadingError = state => state.vcModule.isLoadingError;
export const createActionType = actionName => vcSlice.name + "/" + actionName;
export const {setVcs, setIsLoading, setIsLoadingError} = vcSlice.actions;
