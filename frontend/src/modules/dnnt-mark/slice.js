import {createSelector, createSlice} from '@reduxjs/toolkit';
import {DnntLinkingMode} from '../constants';
import {DnntLabel} from '../constants';

export const linkDnntSlice = createSlice({
    name: 'linkDnnt',
    initialState: {
        uuids: [],
        mode: DnntLinkingMode.Link,
        label: DnntLabel.DNNTO.value,
        processRecursive: true,
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
        setProcessRecursive: (state, action) => {
            state.processRecursive = action.payload;
        }
    },
});

export const getMode = state => state.linkDnntModule.mode;
export const getUuids = state => state.linkDnntModule.uuids;
export const getLabel = state => state.linkDnntModule.label;
export const getProcessRecursive = state => state.linkDnntModule.processRecursive;

export const isLabelSelectorDisabled = createSelector(
    [getMode],
    (mode) => [DnntLinkingMode.Synchronize, DnntLinkingMode.Clean].includes(mode),
);

export const isProcessRecursiveCheckboxDisabled = createSelector(
    [getMode],
    (mode) =>  [DnntLinkingMode.Synchronize].includes(mode),
);

export const getLabelByMode = createSelector(
    [isLabelSelectorDisabled, getMode, getLabel],
    (disabled, mode, label) =>
        disabled && [DnntLinkingMode.Synchronize, DnntLinkingMode.Clean].includes(mode) ? null : label,
)

export const getProcessRecursiveByMode = createSelector(
    [isProcessRecursiveCheckboxDisabled, getMode, getProcessRecursive],
    (disabled, mode, processRecursive) =>
        disabled && [DnntLinkingMode.Synchronize].includes(mode) ? true : processRecursive,
);

export const {addUuids, clearUuids, setLabel, setMode, setProcessRecursive} = linkDnntSlice.actions;
export const createActionType = actionName => linkDnntSlice.name + '/' + actionName;
