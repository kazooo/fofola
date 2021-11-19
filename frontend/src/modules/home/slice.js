import {createSlice} from "@reduxjs/toolkit";

export const homeSlice = createSlice({
    name: 'home',
    initialState: {
        startupTime: null,
        buildTime: null,
        version: null,
        gitBranch: null,
        commitId: null,
    },
    reducers: {
        setStartupTime: (state, action) => {
            state.startupTime = action.payload;
        },
        setBuildTime: (state, action) => {
            state.buildTime = action.payload;
        },
        setVersion: (state, action) => {
            state.version = action.payload;
        },
        setGitBranch: (state, action) => {
            state.gitBranch = action.payload;
        },
        setCommitId: (state, action) => {
            state.commitId = action.payload;
        },
    },
});

export const getStartupTime = state => state.homeModule.startupTime;
export const getBuildTime = state => state.homeModule.buildTime;
export const getVersion = state => state.homeModule.version;
export const getGitBranch = state => state.homeModule.gitBranch;
export const getCommitId = state => state.homeModule.commitId;
export const createActionType = actionName => homeSlice.name + "/" + actionName;
export const {setStartupTime, setVersion, setGitBranch, setBuildTime, setCommitId} = homeSlice.actions;
