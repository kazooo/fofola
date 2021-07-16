import {createSlice} from "@reduxjs/toolkit";

export const krameriusProcessSlice = createSlice({
    name: "krameriusProcess",
    initialState: {
        processesInfo: [],
        isLoading: false,
        page: 0
    },
    reducers: {
        setProcessesInfo: (state, action) => {
            state.processesInfo = action.payload;
        },
        removeProcessInfo: (state, action) => {
            state.processesInfo = state.processesInfo.filter(info => info.uuid !== action.payload);
        },
        setPage: (state, action) => {
            state.page = action.payload;
        },
        toggleIsLoading: (state) => {
            state.isLoading = !state.isLoading;
        }
    }
});

export const getCurrentPage = state => state.krameriusProcess.page;
export const getIsLoading = state => state.krameriusProcess.isLoading;
export const getProcessesInfo = state => state.krameriusProcess.processesInfo;
export const createActionType = actionName => krameriusProcessSlice.name + "/" + actionName;
export const {setProcessesInfo, removeProcessInfo, setPage, toggleIsLoading} = krameriusProcessSlice.actions;
