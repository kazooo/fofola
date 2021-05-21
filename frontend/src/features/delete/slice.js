import {createSlice} from "@reduxjs/toolkit";

export const deleteSlice = createSlice({
    name: "delete",
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

export const getUuids = state => state.delete.uuids;
export const {setUuids, clearUuids} = deleteSlice.actions;
