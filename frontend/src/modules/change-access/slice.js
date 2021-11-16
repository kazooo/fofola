import {createSlice} from "@reduxjs/toolkit";

export const changeAccessSlice = createSlice({
    name: "changeAccess",
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

export const getUuids = state => state.changeAccessModule.uuids;
export const {addUuids, clearUuids} = changeAccessSlice.actions;
export const createActionType = actionName => changeAccessSlice.name + "/" + actionName;
