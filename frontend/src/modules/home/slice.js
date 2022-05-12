import {createSlice} from '@reduxjs/toolkit';

export const homeSlice = createSlice({
    name: 'home',
    initialState: {
        appInfo: {
            startupTime: null,
            buildTime: null,
            version: null,
            gitBranch: null,
            commitId: null,
        }
    },
    reducers: {
        setAppInfo: (state, action) => {
            state.appInfo = action.payload;
        },
    },
});

export const getAppInfo = state => state.homeModule.appInfo;
export const createActionType = actionName => homeSlice.name + '/' + actionName;
export const {setAppInfo} = homeSlice.actions;
