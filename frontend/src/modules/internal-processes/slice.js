import {createSlice} from "@reduxjs/toolkit";

export const internalProcessesSlice = createSlice({
    name: "internalProcesses",
    initialState: {
        page: 0,
        isLoading: false,
        processesInfo: []
    },
    reducers: {
        setProcessesInfo: (state, action) => {
            state.processesInfo = action.payload ? action.payload : [];
        },
        removeProcessInfo: (state, action) => {
            state.processesInfo = state.processesInfo.filter(info => info.id !== action.payload);
        },
        setPage: (state, action) => {
            state.page = action.payload;
        },
        toggleIsLoading: (state) => {
            state.isLoading = !state.isLoading;
        }
    }
});

export const getCurrentPage = state => state.internalProcessesModule.page;
export const getIsLoading = state => state.internalProcessesModule.isLoading;
export const getInternalProcesses = state => state.internalProcessesModule.processesInfo;
export const createActionType = actionName => internalProcessesSlice.name + "/" + actionName;
export const {setProcessesInfo, removeProcessInfo, setPage, toggleIsLoading} = internalProcessesSlice.actions;
