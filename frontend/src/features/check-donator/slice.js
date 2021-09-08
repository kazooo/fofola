import {createSlice} from "@reduxjs/toolkit";
import {CHECK_HAS_DONATOR, EODOPEN} from "./constants";

export const checkDonatorSlice = createSlice({
    name: "checkDonator",
    initialState: {
        vcUuid: "",
        donator: EODOPEN,
        mode: CHECK_HAS_DONATOR,
        outputFiles: [],
        vcs: [],
        isLoading: false,
        isLoadingError: false,
    },
    reducers: {
        setVcUuid: (state, action) => {
            state.vcUuid = action.payload;
        },
        setDonator: (state, action) => {
            state.donator = action.payload;
        },
        setMode: (state, action) => {
            state.mode = action.payload;
        },
        setOutputFiles: (state, action) => {
            state.outputFiles = action.payload;
        },
        removeOutputFile: (state, action) => {
            state.outputFiles = state.outputFiles.filter(file => file !== action.payload);
        },
        clearSettings: (state, action) => {
            state.vcUuid = "";
            state.donator = EODOPEN;
            state.mode = CHECK_HAS_DONATOR;
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

export const getVcs = state => state.checkDonator.vcs;
export const getMode = state => state.checkDonator.mode;
export const getVcUuid = state => state.checkDonator.vcUuid;
export const getDonator = state => state.checkDonator.donator;
export const getIsLoading = state => state.checkDonator.isLoading;
export const getOutputFiles = state => state.checkDonator.outputFiles;
export const getIsLoadingError = state => state.checkDonator.isLoadingError;
export const createActionType = actionName => checkDonatorSlice.name + "/" + actionName;
export const {setVcUuid, setMode, setDonator, setOutputFiles, removeOutputFile, clearSettings, setVcs, setIsLoading, setIsLoadingError} = checkDonatorSlice.actions;
