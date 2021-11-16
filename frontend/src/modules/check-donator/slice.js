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

export const getVcs = state => state.checkDonatorModule.vcs;
export const getMode = state => state.checkDonatorModule.mode;
export const getVcUuid = state => state.checkDonatorModule.vcUuid;
export const getDonator = state => state.checkDonatorModule.donator;
export const getIsLoading = state => state.checkDonatorModule.isLoading;
export const getOutputFiles = state => state.checkDonatorModule.outputFiles;
export const getIsLoadingError = state => state.checkDonatorModule.isLoadingError;
export const createActionType = actionName => checkDonatorSlice.name + "/" + actionName;
export const {setVcUuid, setMode, setDonator, setOutputFiles, removeOutputFile, clearSettings, setVcs, setIsLoading, setIsLoadingError} = checkDonatorSlice.actions;
