import {JobField} from './constants';
import moment from "moment";

export const jobTableColumns = Object.freeze([
    {
        id: JobField.Id,
        label: 'feature.dnntJobs.table.columns.id',
        width: 100,
        align: 'center'
    },
    {
        id: JobField.Title,
        label: 'feature.dnntJobs.table.columns.title',
        width: 200,
        align: 'center'
    },
    {
        id: JobField.CronExpressionExplanation,
        label: 'feature.dnntJobs.table.columns.cronExpressionExplanation',
        width: 100,
        align: 'center'
    },
    {
        id: JobField.LastExecution,
        label: 'feature.dnntJobs.table.columns.lastExecution',
        width: 110,
        align: 'center',
        format: (value) => value && moment(value).format('HH:mm:ss DD/MM/YYYY'),
    },
    {
        id: JobField.NextExecution,
        label: 'feature.dnntJobs.table.columns.nextExecution',
        width: 100,
        align: 'center',
        format: (value) => value && moment(value).format('HH:mm:ss DD/MM/YYYY'),
    },
    {
        id: 'actions',
        label: 'feature.dnntJobs.table.columns.actions',
        maxWidth: 100,
        align: 'center',
    },
]);
