import {create} from 'tools';
import moment from 'moment';
import {JobField} from './constants';

export const defaultValues = create({
    [JobField.Id]: null,
    [JobField.Active]: true,
    [JobField.Title]: '',
    [JobField.Description]: '',
    [JobField.CronExpression]: '',
    [JobField.CronExpressionExplanation]: '',
    [JobField.Type]: '',
    [JobField.Direction]: '',
    [JobField.Operation]: '',
    [JobField.Labels]: '',
    [JobField.Recursive]: true,
    [JobField.From]: null,
    [JobField.FromRelative]: false,
    [JobField.RelativeFrom]: 0,
    [JobField.To]: null,
    [JobField.SolrQuery]: '',
    [JobField.SolrFilterQuery]: '',
});

export const labels = new Map([
    [JobField.Title, 'feature.dnntJobs.form.fields.title.title'],
    [JobField.Description, 'feature.dnntJobs.form.fields.description.title'],
    [JobField.CronExpression, 'feature.dnntJobs.form.fields.cronExpression.title'],
    [JobField.CronExpressionExplanation, 'feature.dnntJobs.form.fields.cronExpressionExplanation.title'],
    [JobField.Active, 'feature.dnntJobs.form.fields.active.title'],
    [JobField.Type, 'feature.dnntJobs.form.fields.type.title'],
    [JobField.Direction, 'feature.dnntJobs.form.fields.direction.title'],
    [JobField.Operation, 'feature.dnntJobs.form.fields.operation.title'],
    [JobField.Labels, 'feature.dnntJobs.form.fields.labels.title'],
    [JobField.Recursive, 'feature.dnntJobs.form.fields.recursive.title'],
    [JobField.From, 'feature.dnntJobs.form.fields.from.title'],
    [JobField.FromRelative, 'feature.dnntJobs.form.fields.fromRelative.title'],
    [JobField.RelativeFrom, 'feature.dnntJobs.form.fields.relativeFrom.title'],
    [JobField.To, 'feature.dnntJobs.form.fields.to.title'],
    [JobField.SolrQuery, 'feature.dnntJobs.form.fields.solrQuery.title'],
    [JobField.SolrFilterQuery, 'feature.dnntJobs.form.fields.solrFilterQuery.title'],
]);

export const rules = new Map([
    [JobField.Title, { required: 'common.form.field.error.notNull' }],
    [JobField.Operation, { required: 'common.form.field.error.notNull' }],
    [JobField.Labels, { required: 'common.form.field.error.notNull' }],
    [JobField.Direction, { required: 'common.form.field.error.notNull' }],
    [JobField.SolrQuery, { required: 'common.form.field.error.notNull' }],
    [JobField.SolrFilterQuery, { required: 'common.form.field.error.notNull' }],
    [JobField.Type, { required: 'common.form.field.error.notNull' }],
    [JobField.From, { required: 'common.form.field.error.notNull' }],
    [JobField.RelativeFrom, { required: 'common.form.field.error.notNull' }],
    [
        JobField.CronExpression,
        {
            required: 'common.form.field.error.notNull',
            pattern: {
                value: /^(?:[0-9?*\/,-]+ ){5}[0-9?*\/,-]+$/,
                message: 'feature.dnntJobs.form.fields.cronExpression.error.invalidFormat',
            },
        },
    ],
    [
        JobField.To,
        {
            required: 'common.form.field.error.notNull',
            validate: {
                isAfterStartDate: (value, allValues) => {
                    const rawFrom = allValues[JobField.From];
                    if (!rawFrom) {
                        return true;
                    }
                    return moment(new Date(value)).isSameOrAfter(moment(new Date(rawFrom))) ||
                        'feature.dnntJobs.form.fields.to.error.cantBeAfterFromDate'
                },
            },
        }
    ],
]);

const textFieldProps = {
    variant: 'outlined',
    size: 'small',
    type: 'text',
};

const textAreaFieldProps = {
    variant: 'outlined',
    size: 'small',
    type: 'text',
    multiline: true,
};

const selectorFieldProps = {
    variant: 'outlined',
    size: 'small',
};

const datePickerProps = {
    variant: 'outlined',
    size: 'small',
};

const switchProps = {
    variant: 'outlined',
    size: 'small',
};

export const fieldProps = new Map([
    [JobField.Title, textFieldProps],
    [JobField.Description, {...textAreaFieldProps, minRows: 6}],
    [JobField.Type, selectorFieldProps],
    [JobField.Direction, selectorFieldProps],
    [JobField.Operation, selectorFieldProps],
    [JobField.Labels, selectorFieldProps],
    [JobField.Recursive, switchProps],
    [JobField.CronExpression, textFieldProps],
    [JobField.CronExpressionExplanation, {...textAreaFieldProps, minRows: 3}],
    [JobField.Active, switchProps],
    [JobField.From, datePickerProps],
    [JobField.FromRelative, switchProps],
    [JobField.RelativeFrom, textFieldProps],
    [JobField.To, datePickerProps],
    [JobField.SolrQuery, {...textAreaFieldProps, minRows: 3}],
    [JobField.SolrFilterQuery, {...textAreaFieldProps, minRows: 3}],
]);
