import {createSlice} from "@reduxjs/toolkit";

export const reindexSlice = createSlice({
    name: "reindex",
    initialState: {
        uuids: []
    },
    reducers: {
        setUuids: (state, action) => {
            state.uuids = action.payload;
        },
        clearUuids: (state, action) => {
            state.uuids = [];
        }
    }
});

export const getUuids = state => state.reindex.uuids;
export const {setUuids, clearUuids} = reindexSlice.actions;
