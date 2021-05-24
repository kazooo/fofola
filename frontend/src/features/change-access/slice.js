import {createSlice} from "@reduxjs/toolkit";

export const changeAccessSlice = createSlice({
    name: "changeAccess",
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

export const getUuids = state => state.changeAccess.uuids;
export const {setUuids, clearUuids} = changeAccessSlice.actions;
