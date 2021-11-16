import {createSlice} from "@reduxjs/toolkit";

export const linkDonatorSlice = createSlice({
    name: "linkDonator",
    initialState: {
        uuids: [],
        mode: "link",
        donator: "eodopen"
    },
    reducers: {
        addUuids: (state, action) => {
            state.uuids = state.uuids.concat(action.payload);
        },
        clearUuids: (state, action) => {
            state.uuids = [];
        },
        setDonator: (state, action) => {
            state.donator = action.payload;
        },
        setMode: (state, action) => {
            state.mode = action.payload;
        }
    }
});

export const getMode = state => state.linkDonatorModule.mode;
export const getUuids = state => state.linkDonatorModule.uuids;
export const getDonator = state => state.linkDonatorModule.donator;
export const {addUuids, clearUuids, setDonator, setMode} = linkDonatorSlice.actions;
export const createActionType = actionName => linkDonatorSlice.name + "/" + actionName;
