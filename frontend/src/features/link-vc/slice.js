import {createSlice} from "@reduxjs/toolkit";
import {LINK_MODE} from "./constants";

export const linkVcSlice = createSlice({
    name: "linkVc",
    initialState: {
        vcUuid: "",
        uuids: [],
        mode: LINK_MODE
    },
    reducers: {
        setUuids: (state, action) => {
            state.uuids = action.payload;
        },
        clearUuids: (state, action) => {
            state.uuids = [];
        },
        setVcUuid: (state, action) => {
            state.vcUuid = action.payload;
        },
        setMode: (state, action) => {
            state.mode = action.payload;
        }
    }
});

export const getMode = state => state.linkVc.mode;
export const getUuids = state => state.linkVc.uuids;
export const getVcUuid = state => state.linkVc.vcUuid;
export const {setUuids, clearUuids, setVcUuid, setMode} = linkVcSlice.actions;
