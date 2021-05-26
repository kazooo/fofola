import {createSlice} from "@reduxjs/toolkit";

export const krameriusProcessSlice = createSlice({
    name: "krameriusProcess",
    initialState: {
        processesInfo: [],
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
        }
    }
});

export const getCurrentPage = state => state.krameriusProcess.page;
export const getProcessesInfo = state => state.krameriusProcess.processesInfo;
export const {setProcessesInfo, removeProcessInfo, setPage, setAutoReload} = krameriusProcessSlice.actions;
