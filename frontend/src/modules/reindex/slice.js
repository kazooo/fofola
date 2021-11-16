import {createSlice} from "@reduxjs/toolkit";

export const reindexSlice = createSlice({
    name: "reindex",
    initialState: {
        uuids: []
    },
    reducers: {
        addUuids: (state, action) => {
            state.uuids = state.uuids.concat(...action.payload);
        },
        clearUuids: (state, action) => {
            state.uuids = [];
        }
    }
});

export const getUuids = state => state.reindexModule.uuids;
export const {addUuids, clearUuids} = reindexSlice.actions;
export const createActionType = actionName => reindexSlice.name + "/" + actionName;
