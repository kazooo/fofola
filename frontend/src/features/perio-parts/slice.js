import {createSlice} from "@reduxjs/toolkit";

export const perioPartsPublishSlice = createSlice({
    name: "perioPartsPublish",
    initialState: {
        uuids: []
    },
    reducers: {
        addUuids: (state, action) => {
            state.uuids = state.uuids.concat(action.payload);
        },
        clearUuids: (state, action) => {
            state.uuids = [];
        }
    }
});

export const getUuids = state => state.perioPartsPublish.uuids;
export const {addUuids, clearUuids} = perioPartsPublishSlice.actions;
export const createActionType = actionName => perioPartsPublishSlice.name + "/" + actionName;
