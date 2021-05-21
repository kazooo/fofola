import {createSlice} from "@reduxjs/toolkit";

export const linkDonatorSlice = createSlice({
    name: "linkDonator",
    initialState: {
        uuids: [],
        mode: "link",
        donator: "eodopen"
    },
    reducers: {
        setUuids: (state, action) => {
            state.uuids = action.payload;
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

export const getMode = state => state.linkDonator.mode;
export const getUuids = state => state.linkDonator.uuids;
export const getDonator = state => state.linkDonator.donator;
export const {setUuids, clearUuids, setDonator, setMode} = linkDonatorSlice.actions;
