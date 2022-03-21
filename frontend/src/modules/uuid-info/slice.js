import {createSlice} from '@reduxjs/toolkit';

export const uuidInfoSlice = createSlice({
    name: 'uuidInfo',
    initialState: {
        uuidInfo: []
    },
    reducers: {
        addUuidInfo: (state, action) => {
            state.uuidInfo = state.uuidInfo.concat(action.payload);
        },
        addOneUuidInfo: (state, action) => {
            state.uuidInfo = state.uuidInfo.map(info => info.uuid !== action.payload.uuid ? info : action.payload);
        },
        removeUuidInfo: (state, action) => {
            state.uuidInfo = state.uuidInfo.filter(info => info.uuid !== action.payload);
        },
        clearUuidInfo: (state, action) => {
            state.uuidInfo = [];
        }
    }
});

export const getUuidInfo = state => state.uuidInfoModule.uuidInfo;
export const {addUuidInfo, addOneUuidInfo, removeUuidInfo, clearUuidInfo} = uuidInfoSlice.actions;
export const createActionType = actionName => uuidInfoSlice.name + '/' + actionName;
