import {createSlice} from "@reduxjs/toolkit";

export const uuidInfoSlice = createSlice({
    name: "uuidInfo",
    initialState: {
        uuidInfo: []
    },
    reducers: {
        addUuidInfo: (state, action) => {
            state.uuidInfo = state.uuidInfo.concat(action.payload);
        },
        clearUuidInfo: (state, action) => {
            state.uuidInfo = [];
        }
    }
});

export const getUuidInfo = state => state.uuidInfo.uuidInfo;
export const {addUuidInfo, clearUuidInfo} = uuidInfoSlice.actions;
export const createActionType = actionName => uuidInfoSlice.name + "/" + actionName;
