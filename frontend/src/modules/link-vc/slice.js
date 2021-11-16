import {createSlice} from "@reduxjs/toolkit";
import {LINK_MODE} from "./constants";

export const linkVcSlice = createSlice({
    name: "linkVc",
    initialState: {
        vcUuid: '',
        uuids: [],
        mode: LINK_MODE,
        vcs: [],
        isLoading: false,
        isLoadingError: false,
    },
    reducers: {
        addUuids: (state, action) => {
            state.uuids = state.uuids.concat(action.payload);
        },
        clearUuids: (state, action) => {
            state.uuids = [];
        },
        setVcUuid: (state, action) => {
            state.vcUuid = action.payload;
        },
        setMode: (state, action) => {
            state.mode = action.payload;
        },
        setVcs: (state, action) => {
            state.vcs = action.payload;
        },
        setIsLoading: (state, action) => {
            state.isLoading = action.payload;
        },
        setIsLoadingError: (state, action) => {
            state.isLoadingError = action.payload;
        },
    }
});

export const getVcs = state => state.linkVcModule.vcs;
export const getMode = state => state.linkVcModule.mode;
export const getUuids = state => state.linkVcModule.uuids;
export const getVcUuid = state => state.linkVcModule.vcUuid;
export const getIsLoading = state => state.linkVcModule.isLoading;
export const getIsLoadingError = state => state.linkVcModule.isLoadingError;
export const createActionType = actionName => linkVcSlice.name + "/" + actionName;
export const {addUuids, clearUuids, setVcUuid, setMode, setVcs, setIsLoading, setIsLoadingError} = linkVcSlice.actions;
