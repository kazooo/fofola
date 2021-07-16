import {createSelector, createSlice} from "@reduxjs/toolkit";
import {IMG_FULL} from "./constants";

export const setImgSlice = createSlice({
    name: "setImg",
    initialState: {
        uuid: "",
        datastream: IMG_FULL,
        img: null,
    },
    reducers: {
        setUuid: (state, action) => {
            state.uuid = action.payload;
        },
        setDatastream: (state, action) => {
            state.datastream = action.payload;
        },
        setImg: (state, action) => {
            state.img = action.payload;
        },
        clearAll: (state, action) => {
            state.uuid = "";
            state.datastream = IMG_FULL;
            state.img = null;
        }
    }
});

export const getPayload = state => state.setImg;
export const isPayloadCompleted = createSelector(
    [getPayload],
    (payload) => {
        return payload.uuid !== "" && payload.datastream !== "" && payload.img !== null;
    }
);
export const getDatastream = state => getPayload(state).datastream;
export const {setUuid, setDatastream, setImg, clearAll} = setImgSlice.actions;
