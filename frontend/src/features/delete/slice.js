import {createSlice} from "@reduxjs/toolkit";

export const deleteSlice = createSlice({
    name: "delete",
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

export const getUuids = state => state.delete.uuids;
export const {addUuids, clearUuids} = deleteSlice.actions;
export const createActionType = actionName => deleteSlice.name + "/" + actionName;
