import {createSlice} from "@reduxjs/toolkit";

export const solrQuerySlice = createSlice({
    name: "solrQuery",
    initialState: {
        outputFiles: [],
    },
    reducers: {
        setOutputFiles: (state, action) => {
            state.outputFiles = action.payload ? action.payload : [];
        },
        removeOutputFile: (state, action) => {
            state.outputFiles = state.outputFiles.filter(file => file !== action.payload);
        },
    }
});

export const getOutputFiles = state => state.solrQueryModule.outputFiles;
export const createActionType = actionName => solrQuerySlice.name + "/" + actionName;
export const {setOutputFiles, removeOutputFile} = solrQuerySlice.actions;
