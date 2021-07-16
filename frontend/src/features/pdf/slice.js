import {createSlice} from "@reduxjs/toolkit";

export const pdfSlice = createSlice({
    name: "pdf",
    initialState: {
        uuids: [],
        outputFiles: []
    },
    reducers: {
        addUuids: (state, action) => {
            state.uuids = state.uuids.concat(action.payload);
        },
        clearUuids: (state, action) => {
            state.uuids = [];
        },
        setOutputFiles: (state, action) => {
            state.outputFiles = action.payload;
        },
        removeOutputFile: (state, action) => {
            state.outputFiles = state.outputFiles.filter(file => file !== action.payload);
        }
    }
});

export const getUuids = state => state.pdf.uuids;
export const getOutputFiles = state => state.pdf.outputFiles;
export const createActionType = actionName => pdfSlice.name + "/" + actionName;
export const {addUuids, clearUuids, setOutputFiles, removeOutputFile} = pdfSlice.actions;
