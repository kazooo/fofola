import {createSelector, createSlice} from '@reduxjs/toolkit';
import {JobFormType} from './constants';
import {defaultValues} from './form';

export const dnntJobSlice = createSlice({
    name: 'dnntJobs',
    initialState: {
        jobs: [],
        isLoading: false,
        jobFormType: null,
        jobFormValues: defaultValues,
    },
    reducers: {
        setJob(state, action) {
            const jobIndex = state.jobs.findIndex(job => job.id === action.payload.id);
            if (jobIndex !== -1) {
                state.jobs[jobIndex] = action.payload;
            }
        },
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
            state.jobFormValues = defaultValues;
        },
        openUpdateJobForm: (state, action) => {
            state.jobFormType = JobFormType.Update;
            state.jobFormValues = action.payload;
        },
        closeJobForm: (state, action) => {
            state.jobFormType = null;
        },
    }
});

export const getJobs = state => state.dnntJobs.jobs;
export const getJob = (jobId) =>
    createSelector(getJobs, (jobs) => jobs.filter((job) => job.id === jobId))
export const getIsLoading = state => state.dnntJobs.isLoading;
export const getJobFormType = state => state.dnntJobs.jobFormType;
export const getJobFormValues = state => state.dnntJobs.jobFormValues;

export const {
    setJob,
    setJobs,
    removeJob,
    cleanJobs,
    toggleIsLoading,
    openCreateJobForm,
    openUpdateJobForm,
    closeJobForm,
} = dnntJobSlice.actions;

export const createActionType = actionName => `${dnntJobSlice.name}/${actionName}`;
