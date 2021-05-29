import {createSlice} from "@reduxjs/toolkit";

export const checkDonatorSlice = createSlice({
    name: "checkDonator",
    initialState: {
        outputFiles: []
    },
    reducers: {
        setOutputFiles: (state, action) => {
            state.outputFiles = action.payload;
        },
        removeOutputFile: (state, action) => {
            state.outputFiles = state.outputFiles.filter(file => file !== action.payload);
        }
    }
});

export const getOutputFiles = state => state.checkDonator.outputFiles;
export const {setOutputFiles, removeOutputFile} = checkDonatorSlice.actions;
