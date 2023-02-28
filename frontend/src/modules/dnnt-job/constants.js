import {SugoSessionRequestor} from "../constants";

export const createFormInitialValues = Object.freeze({
    title: '',
    description: '',
    cronExpression: '',
    cronExpressionExplanation: '',
    active: true,
    operation: {
        sessionType: '',
        from: '',
        to: '',
        direction: '',
        requestor: SugoSessionRequestor.Auto.value,
        solrQuery: '',
    },
});

export const JobFormType = Object.freeze({
    Create: 'createJobForm',
    Update: 'updateJobForm',
});

export const jobTableColumns = Object.freeze([
    {
        id: 'id',
        label: 'feature.dnntJobs.table.columns.id',
        width: 100,
        align: 'center'
    },
    {
        id: 'title',
        label: 'feature.dnntJobs.table.columns.title',
        width: 100,
        align: 'center'
    },
    {
        id: 'description',
        label: 'feature.dnntJobs.table.columns.description',
        width: 100,
        align: 'center'
    },
    {
        id: 'cronExpressionExplanation',
        label: 'feature.dnntJobs.table.columns.cronExpressionExplanation',
        width: 100,
        align: 'center'
    },
    {
        id: 'lastExecution',
        label: 'feature.dnntJobs.table.columns.lastExecution',
        width: 100,
        align: 'center'
    },
    {
        id: 'nextExecution',
        label: 'feature.dnntJobs.table.columns.nextExecution',
        width: 100,
        align: 'center'
    },
    {
        id: 'actions',
        label: 'feature.dnntJobs.table.columns.actions',
        maxWidth: 100,
        align: 'center',
    },
]);
