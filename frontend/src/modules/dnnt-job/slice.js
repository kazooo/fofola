import {createSlice} from "@reduxjs/toolkit";
import {JobFormType} from "./constants";

export const dnntJobSlice = createSlice({
    name: 'dnntJobs',
    initialState: {
        jobs: [],
        isLoading: false,
        jobFormType: null,
    },
    reducers: {
        setJobs: (state, action) => {
            state.jobs = action.payload;
        },
        removeJob: (state, action) => {
            state.jobs = state.jobs.filter(job => job.id !== action.payload);
        },
        cleanJobs: (state, action) => {
            state.jobs = [];
        },
        toggleIsLoading: (state, action) => {
            state.isLoading = !state.isLoading;
        },
        openCreateJobForm: (state, action) => {
            state.jobFormType = JobFormType.Create;
        },
        openUpdateJobForm: (state, action) => {
            state.jobFormType = JobFormType.Update;
        },
        closeJobForm: (state, action) => {
            state.jobFormType = null;
        },
    }
});

export const getJobs = state => state.dnntJobs.jobs;
export const getIsLoading = state => state.dnntJobs.isLoading;
export const getJobFormType = state => state.dnntJobs.jobFormType;

export const {
    setJobs,
    removeJob,
    cleanJobs,
    toggleIsLoading,
    openCreateJobForm,
    openUpdateJobForm,
    closeJobForm,
} = dnntJobSlice.actions;

export const createActionType = actionName => `${dnntJobSlice.name}/${actionName}`;
