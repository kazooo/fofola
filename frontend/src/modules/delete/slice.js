import {createSlice} from '@reduxjs/toolkit';

export const deleteSlice = createSlice({
    name: 'delete',
    initialState: {
        uuids: [],
        deleteFromSolrOnly: false,
        deleteRecursively: false,
    },
    reducers: {
        addUuids: (state, action) => {
            state.uuids = state.uuids.concat(action.payload);
        },
        clearUuids: (state, action) => {
            state.uuids = [];
        },
        toggleDeleteFromSolrOnly: (state, action) => {
            state.deleteFromSolrOnly = !state.deleteFromSolrOnly;
        },
        toggleDeleteRecursively: (state, action) => {
            state.deleteRecursively = !state.deleteRecursively;
        },
    }
});

export const getUuids = state => state.deleteModule.uuids;
export const getDeleteFromSolrOnly = state => state.deleteModule.deleteFromSolrOnly;
export const getDeleteRecursively = state => state.deleteModule.deleteRecursively;
export const {addUuids, clearUuids, toggleDeleteFromSolrOnly, toggleDeleteRecursively} = deleteSlice.actions;
export const createActionType = actionName => deleteSlice.name + '/' + actionName;
