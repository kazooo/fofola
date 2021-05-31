import {createSlice} from "@reduxjs/toolkit";

export const perioPartsPublishSlice = createSlice({
    name: "perioPartsPublish",
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

export const getUuids = state => state.perioPartsPublish.uuids;
export const {setUuids, clearUuids} = perioPartsPublishSlice.actions;
