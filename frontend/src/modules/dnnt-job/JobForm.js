import {useTranslation} from 'react-i18next';
import {useDispatch} from 'react-redux';
import {closeJobForm} from './slice';
import React, {useEffect, useState} from 'react';
import {Box, Button, Divider, Grid, Modal} from '@material-ui/core';
import {
    validDnntLabels,
    SugoJobType,
    sugoJobTypes,
    SugoSessionDirection,
    sugoSessionDirections,
    SugoSessionOperation,
    sugoSessionOperations,
} from '../constants';
import cronstrue from 'cronstrue';
import {convertMinutesToMilliseconds, asEndOfDay, asStartOfDay, isValidCron, formatAsDateTime} from '../utils';
import {JobField, JobFormType} from './constants';
import {useForm} from '../../effects/useForm';
import {submitCreateJobForm, submitUpdateJobForm} from './saga';
import {get, set} from 'tools';
import {fieldProps, rules, labels} from './form';
import {createFormStyles, containerRowStyle, containerColumnStyle} from './style';

export const JobForm = ({type, values}) => {
    const {t} = useTranslation();
    const dispatch = useDispatch();
    const classes = createFormStyles();
    const [operationOverLabel, setOperationOverLabel] = useState(false);
    const {
        handleSubmit,
        getValue,
        setValue,
        reset,
        resetFields,
        components: {
            createTextField,
            createTextArea,
            createSelector,
            createSwitch,
            createDatepicker,
            createDurationField,
        }
    } = useForm({labels, rules, fieldProps, values});

    /* clean up function to be called when the component unmounts */
    useEffect(() => {
        return () => {
            reset();
        };
    }, []);

    const fieldStyle = {
        item: true,
        className: classes.fieldWrap
    }

    const onSubmitFormDispatch = new Map([
        [JobFormType.Create, submitCreateJobForm],
        [JobFormType.Update, submitUpdateJobForm],
    ]);

    const onSubmitButtonTitle = new Map([
        [JobFormType.Create, 'feature.dnntJobs.form.buttons.submit.create.title'],
        [JobFormType.Update, 'feature.dnntJobs.form.buttons.submit.update.title'],
    ]);

    const onSubmit = (data) => {
        const closeCallback = async (success) => {
            if (success) {
                /* wait a bit until the submitting process of react-form-hook finished */
                /* otherwise it produces an error */
                await new Promise(resolve => setTimeout(resolve, 200));
                dispatch(closeJobForm());
            }
        }
        const formattedFields = set(data, {
            [JobField.From]: formatAsDateTime(asStartOfDay(get(JobField.From, data))),
            [JobField.To]: formatAsDateTime(asEndOfDay(get(JobField.To, data))),
        })
        dispatch(onSubmitFormDispatch.get(type)(formattedFields, closeCallback));
    };

    const onCancel = () => {
        dispatch(closeJobForm());
    };

    const refreshCronExplanation = (cronExpression) => {
        const explanation = isValidCron(cronExpression) ? cronstrue.toString(cronExpression) : '';
        setValue(JobField.CronExpressionExplanation, explanation);
    };

    const isOperationOverLabel = (operation) => {
        const operationsOverLabel = [SugoSessionOperation.AddLabel.value, SugoSessionOperation.RemoveLabel.value];
        const isOverLabel = operationsOverLabel.includes(operation);
        if (!isOverLabel) {
            resetFields(JobField.Labels);
        }
        setOperationOverLabel(isOverLabel);
    }

    const formFromTo = (
        <Grid {...containerRowStyle}>
            <Grid item xs={6}>
                <Grid {...containerColumnStyle}>
                    <Grid {...fieldStyle}>
                        {getValue(JobField.FromRelative) && (
                            createDurationField({
                                fieldName: JobField.RelativeFrom,
                                className: classes.field,
                            })
                        )}
                        {!getValue(JobField.FromRelative) && (
                            createDatepicker({
                                fieldName: JobField.From,
                                className: classes.field,
                            })
                        )}
                    </Grid>
                </Grid>
            </Grid>
            <Grid item xs={6}>
                <Grid {...containerColumnStyle}>
                    <Grid item>
                        {createSwitch({
                            fieldName: JobField.FromRelative,
                            onChange: (value) => resetFields(JobField.From, JobField.RelativeFrom),
                        })}
                    </Grid>
                </Grid>
            </Grid>
        </Grid>
    );

    const formCustom = (
        <Grid item xs={12}>
            <Grid {...containerColumnStyle} alignItems={'flex-start'} alignContent={'center'}>
                <Grid item xs={12} className={classes.fieldWrap}>
                    {createTextArea({
                        fieldName: JobField.SolrQuery,
                        className: classes.field,
                    })}
                </Grid>
                {/*hide this section at the moment*/}
                {/*<Grid item xs={12} className={classes.fieldWrap}>*/}
                {/*    {createTextArea({*/}
                {/*        fieldName: JobField.SolrFilterQuery,*/}
                {/*        className: classes.field,*/}
                {/*    })}*/}
                {/*</Grid>*/}
            </Grid>
        </Grid>
    );

    const sectionByJobSessionType = new Map([
        [SugoJobType.Full.value, null],
        [SugoJobType.Changes.value, formFromTo],
        [SugoJobType.Custom.value, formCustom],
    ]);

    const generalSection = (
        <Grid {...containerRowStyle}>
            <Grid item xs={6}>
                <Grid {...containerColumnStyle}>
                    <Grid {...fieldStyle}>
                        {createTextField({
                            fieldName:JobField.Title,
                            className: classes.field,
                        })}
                    </Grid>
                    <Grid {...fieldStyle}>
                        {createSelector({
                            fieldName: JobField.Direction,
                            options: sugoSessionDirections.filter((item) => item !== SugoSessionDirection.Rest2Dst),
                            className: classes.field,
                        })}
                    </Grid>
                    <Grid {...fieldStyle}>
                        {createSelector({
                            fieldName: JobField.Operation,
                            options: sugoSessionOperations.filter((item) => ![SugoSessionOperation.Any, SugoSessionOperation.Clean].includes(item)),
                            className: classes.field,
                            onChange: (value) => isOperationOverLabel(value)
                        })}
                    </Grid>
                    {operationOverLabel && (
                        <Grid {...fieldStyle}>
                            {createSelector({
                                fieldName: JobField.Labels,
                                options: validDnntLabels,
                                className: classes.field,
                            })}
                        </Grid>
                    )}
                    <Grid {...fieldStyle}>
                        {createSelector({
                            fieldName: JobField.Type,
                            options: sugoJobTypes,
                            className: classes.field,
                            onChange: (value) => {
                                /* clear session specific fields */
                                resetFields(
                                    JobField.From,
                                    JobField.FromRelative,
                                    JobField.RelativeFrom,
                                    JobField.To,
                                    JobField.SolrQuery,
                                    JobField.SolrFilterQuery,
                                );
                            }
                        })}
                    </Grid>
                </Grid>
            </Grid>
            <Grid item xs={6}>
                <Grid {...containerColumnStyle}>
                    <Grid {...fieldStyle}>
                        {createTextArea({
                            fieldName: JobField.Description,
                            className: classes.field,
                        })}
                    </Grid>
                    <Grid item>
                        {createSwitch({ fieldName: JobField.Recursive })}
                    </Grid>
                </Grid>
            </Grid>
        </Grid>
    );

    const periodicitySection = (
        <Grid {...containerRowStyle} alignItems={'flex-start'}>
            <Grid item xs={6}>
                <Grid {...containerColumnStyle}>
                    <Grid {...fieldStyle}>
                        {createTextField({
                            fieldName: JobField.CronExpression,
                            className: classes.field,
                            onChange: (value) => refreshCronExplanation(value),
                        })}
                    </Grid>
                    <Grid item>
                        {createSwitch({ fieldName: JobField.Active })}
                    </Grid>
                </Grid>
            </Grid>
            <Grid item xs={6}>
                <Grid {...containerColumnStyle}>
                    <Grid {...fieldStyle}>
                        {createTextArea({
                            fieldName: JobField.CronExpressionExplanation,
                            className: classes.field,
                        })}
                    </Grid>
                </Grid>
            </Grid>
        </Grid>
    );

    const footer = (
        <Grid {...containerRowStyle} spacing={3}>
            <Grid item xs={6} align={'right'}>
                <Button
                    onClick={handleSubmit(onSubmit)}
                    color={'primary'}
                >
                    {t(onSubmitButtonTitle.get(type))}
                </Button>
            </Grid>
            <Grid item xs={6} align={'left'}>
                <Button
                    onClick={onCancel}
                    color={'primary'}
                >
                    {t('feature.dnntJobs.form.buttons.cancel.title')}
                </Button>
            </Grid>
        </Grid>
    );

    return (
        <Modal
            open={true}
            aria-labelledby='modal-modal-title'
            aria-describedby='modal-modal-description'
        >
            <form>
                <Box className={classes.form}>
                    <Box className={classes.formBox}>
                        <Grid container direction={'column'} spacing={4}>
                            <Grid item>
                                {generalSection}
                            </Grid>
                            <Divider variant={'middle'} />
                            <Grid item>
                                {periodicitySection}
                            </Grid>
                            <Divider variant={'middle'} />
                            <Grid item>
                                {sectionByJobSessionType.get(getValue(JobField.Type))}
                            </Grid>
                            <Grid item>
                                {footer}
                            </Grid>
                        </Grid>
                    </Box>
                </Box>
            </form>
        </Modal>
    );
};
