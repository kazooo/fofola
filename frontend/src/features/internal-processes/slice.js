import {createSlice} from "@reduxjs/toolkit";

export const internalProcessesSlice = createSlice({
    name: "internalProcesses",
    initialState: {
        page: 0,
        processesInfo: []
    },
    reducers: {
        setProcessesInfo: (state, action) => {
            state.processesInfo = action.payload;
        },
        setPage: (state, action) => {
            state.page = action.payload;
        }
    }
});

export const getCurrentPage = state => state.internalProcesses.page;
export const getInternalProcesses = state => state.internalProcesses.processesInfo;
export const {setProcessesInfo, setPage, setAutoReload} = internalProcessesSlice.actions;
