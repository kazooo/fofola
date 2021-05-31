import {createSlice} from "@reduxjs/toolkit";

export const solrQuerySlice = createSlice({
    name: "solrQuery",
    initialState: {
        outputFiles: []
    },
    reducers: {
        setOutputFiles: (state, action) => {
            state.outputFiles = action.payload;
        }
    }
});

export const getOutputFiles = state => state.solrQuery.outputFiles;
export const {setOutputFiles} = solrQuerySlice.actions;
