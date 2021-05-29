import {createSlice} from "@reduxjs/toolkit";

export const pdfSlice = createSlice({
    name: "pdf",
    initialState: {
        uuids: [],
        outputFiles: []
    },
    reducers: {
        setUuids: (state, action) => {
            state.uuids = action.payload;
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
export const {setUuids, setOutputFiles, removeOutputFile} = pdfSlice.actions;
