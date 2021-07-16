import {createSlice} from "@reduxjs/toolkit";
import {MONOGRAPH, PRIVATE_ACCESS, UUID} from "./constants";

export const solrQuerySlice = createSlice({
    name: "solrQuery",
    initialState: {
        outputFiles: [],
        model: MONOGRAPH,
        from: '',
        to: '',
        access: PRIVATE_ACCESS,
        field: UUID,
    },
    reducers: {
        setOutputFiles: (state, action) => {
            state.outputFiles = action.payload;
        },
        removeOutputFile: (state, action) => {
            state.outputFiles = state.outputFiles.filter(file => file !== action.payload);
        },
        setModel: (state, action) => {
            state.model = action.payload;
        },
        setFrom: (state, action) => {
            state.from = action.payload;
        },
        setTo: (state, action) => {
            state.to = action.payload;
        },
        setAccess: (state, action) => {
            state.access = action.payload;
        },
        setField: (state, action) => {
            state.field = action.payload;
        },
        clearParams: (state, action) => {
            state.model = MONOGRAPH;
            state.from = '';
            state.to = '';
            state.access = PRIVATE_ACCESS;
            state.field = UUID;
        },
    }
});

export const getTo = state => state.solrQuery.to;
export const getFrom = state => state.solrQuery.from;
export const getModel = state => state.solrQuery.model;
export const getField = state => state.solrQuery.field;
export const getAccess = state => state.solrQuery.access;
export const getOutputFiles = state => state.solrQuery.outputFiles;
export const allRequiredParamsExist = state => getTo(state) && getFrom(state);
export const createActionType = actionName => solrQuerySlice.name + "/" + actionName;
export const {setOutputFiles, removeOutputFile, setModel, setFrom, setTo, setAccess, setField, clearParams} = solrQuerySlice.actions;
