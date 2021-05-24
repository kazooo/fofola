import {createSlice} from "@reduxjs/toolkit";

export const uuidInfoSlice = createSlice({
    name: "uuidInfo",
    initialState: {
        uuidInfo: []
    },
    reducers: {
        setUuidInfo: (state, action) => {
            state.uuidInfo = action.payload;
        },
        clearUuidInfo: (state, action) => {
            state.uuidInfo = [];
        }
    }
});

export const getUuidInfo = state => state.uuidInfo.uuidInfo;
export const {setUuidInfo, clearUuidInfo} = uuidInfoSlice.actions;
