import {createSlice} from "@reduxjs/toolkit";

export const linkDnntSlice = createSlice({
    name: 'linkDnnt',
    initialState: {
        uuids: [],
        mode: 'link',
        label: 'dnnto',
    },
    reducers: {
        addUuids: (state, action) => {
            state.uuids = state.uuids.concat(action.payload);
        },
        clearUuids: (state, action) => {
            state.uuids = [];
        },
        setMode: (state, action) => {
            state.mode = action.payload;
        },
        setLabel: (state, action) => {
            state.label = action.payload;
        },
    },
});

export const getMode = state => state.linkDnntModule.mode;
export const getUuids = state => state.linkDnntModule.uuids;
export const getLabel = state => state.linkDnntModule.label;
export const {addUuids, clearUuids, setLabel, setMode} = linkDnntSlice.actions;
export const createActionType = actionName => linkDnntSlice.name + "/" + actionName;
