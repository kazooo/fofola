import {
    Box,
    Button,
    Divider,
    FormControl,
    FormControlLabel,
    Grid,
    InputLabel,
    makeStyles,
    MenuItem,
    Modal,
    Select,
    Switch,
    TextField
} from "@material-ui/core";
import {useDispatch, useSelector} from "react-redux";
import React, {useEffect, useState} from "react";
import {useTranslation} from "react-i18next";

import {createFormInitialValues, JobFormType} from "./constants";
import {submitCreateJobForm, submitUpdateJobForm} from "./saga";
import {closeJobForm, getJobFormType} from "./slice";
import {
    SugoAutomaticJobSessionType,
    sugoAutomaticJobSessionTypes, SugoSessionDirection,
    SugoSessionDirectionExtended,
    sugoSessionDirections,
} from "../constants";

const createStyles = () => {
    const styles = {
        form: {
            position: 'absolute',
            top: '50%',
            left: '50%',
            transform: 'translate(-50%, -50%)',
            backgroundColor: 'white',
            border: '1px solid rgb(212, 101, 3)',
            borderRadius: '5px',
            boxShadow: 24,
            padding: 4,
        },
        formBox: {
            margin: '20px',
        },
        field: {
            width: 275,
        },
    };
    return makeStyles(styles)();
}

export const JobForm = () => {
    const {t} = useTranslation();
    const dispatch = useDispatch();
    const jobFormType = useSelector(getJobFormType);
    const [isOpen, setOpen] = useState(false);
    const classes = createStyles();

    useEffect(() => {
        setOpen(jobFormType !== null)
    }, [jobFormType]);

    const [formValues, setFormValues] = useState(createFormInitialValues);

    const updateFormValues = (formValues, fieldName, value) => {
        const names = fieldName.split('.');
        let current = formValues;
        /* traverse nested structures to get the required field */
        for (let i = 0; i < names.length; i++) {
            const name = names[i];
            if (i === names.length - 1) {
                current[name] = value;
            } else {
                current = current[name];
            }
        }
        return { ...formValues };
    };

    const handleInputChange = (event) => {
        const { name, value } = event.target;
        setFormValues((prevValues) => updateFormValues(
            {...prevValues},
            name,
            value
        ));
    };

    const onSubmitDispatch = new Map([
        [JobFormType.Create, submitCreateJobForm(formValues)],
        [JobFormType.Update, submitUpdateJobForm(formValues)],
    ]);

    const onSubmit = () => {
        dispatch(onSubmitDispatch.get(jobFormType));
        setFormValues(createFormInitialValues);
    };

    const onCancel = () => {
        setFormValues(createFormInitialValues);
        dispatch(closeJobForm());
    };

    const createSelectorOptions = options => options.map((option, index) => (
        <MenuItem key={index} value={option.value}>
            {option.text}
        </MenuItem>
    ));

    const jobSessionTypes = createSelectorOptions(sugoAutomaticJobSessionTypes);
    const directions = createSelectorOptions(sugoSessionDirections.filter((item) => item !== SugoSessionDirection.Rest2Dst));

    const createTextField = (id, value, label, inputProps) => (
        <FormControl variant={'outlined'} size={'small'}>
            <TextField
                id={id}
                name={id}
                label={t(label)}
                value={value}
                variant={'outlined'}
                size={'small'}
                type={'text'}
                inputProps={inputProps}
                onChange={handleInputChange}
                className={classes.field}
            />
        </FormControl>
    );

    const createTextArea = (id, value, label, inputProps) => (
        <FormControl variant={'outlined'} size={'small'}>
            <TextField
                id={id}
                name={id}
                label={t(label)}
                value={value}
                variant={'outlined'}
                size={'small'}
                type={'text'}
                multiline
                minRows={6}
                inputProps={inputProps}
                onChange={handleInputChange}
                className={classes.field}
            />
        </FormControl>
    );

    const createSwitchButton = (id, value, label) => (
        <FormControl variant={'outlined'} size={'small'}>
            <FormControlLabel
                control={
                    <Switch
                        id={id}
                        name={id}
                        label={t(label)}
                        value={value}
                        variant={'outlined'}
                        size={'small'}
                        onChange={(event, checked) => handleInputChange({
                            target: {
                                name: event.target.name,
                                value: checked,
                            }
                        })}
                    />
                }
                label={t(label)}
            />
        </FormControl>
    );

    const createSelectorField = (id, value, label, options, onChange = null) => {
        return (
            <FormControl variant='outlined' size='small'>
                <InputLabel>{t(label)}</InputLabel>
                <Select
                    id={id}
                    name={id}
                    label={t(label)}
                    value={value}
                    onChange={(event) => {
                        if(onChange) {
                            onChange(event);
                        }
                        handleInputChange(event);
                    }}
                    style={{ width: 275 }}
                >
                    {options}
                </Select>
            </FormControl>
        );
    };

    const formPeriodically = (
        <>
            {
                createTextField(
                    'cronExpression',
                    formValues.cronExpression,
                    'feature.dnntJobs.form.fields.cronExpression.title',
                )
            }
            {
                createTextField(
                    'cronExpressionExplanation',
                    formValues.cronExpressionExplanation,
                    'feature.dnntJobs.form.fields.cronExpressionExplanation.title',
                )
            }
        </>
    );

    const formFromTo = (
        <>
            {
                createTextField(
                    'operation.from',
                    formValues.operation.from,
                    'feature.dnntJobs.form.fields.from.title',
                    { maxLength: 40 }
                )
            }
            {
                createTextField(
                    'operation.to',
                    formValues.operation.to,
                    'feature.dnntJobs.form.fields.to.title',
                    { maxLength: 40 }
                )
            }
        </>
    );

    const formCustom = (
        <>
            {
                createTextField(
                    'operation.solrQuery',
                    formValues.operation.solrQuery,
                    'feature.dnntJobs.form.fields.solrQuery.title',
                    { maxLength: 40 }
                )
            }
        </>
    );

    const isDirection = (direction) => formValues.operation.direction === direction.value;

    return (
        <Modal
            open={isOpen}
            aria-labelledby='modal-modal-title'
            aria-describedby='modal-modal-description'
        >
            <Box className={classes.form}>
                <Box className={classes.formBox}>
                    <Grid container direction={'column'} spacing={4}>
                        <Grid item>
                            <Grid container alignItems={'flex-start'} direction={'row'} spacing={2}>
                                <Grid item xs={6}>
                                    <Grid container alignItems={'center'} direction={'column'} spacing={2}>
                                        <Grid item>
                                            {
                                                createTextField(
                                                    'title',
                                                    formValues.title,
                                                    'feature.dnntJobs.form.fields.title.title',
                                                    { maxLength: 40 }
                                                )
                                            }
                                        </Grid>
                                        <Grid item>
                                            {
                                                createSelectorField(
                                                    'operation.direction',
                                                    formValues.operation.direction,
                                                    'feature.dnntJobs.form.fields.direction.title',
                                                    directions,
                                                    () => {
                                                        /* reset session type if direction has been changed */
                                                        setFormValues((prevValues) => updateFormValues(
                                                            {...prevValues},
                                                            'operation.sessionType',
                                                            ''
                                                        ))
                                                    }
                                                )
                                            }
                                        </Grid>
                                        <Grid item>
                                            {
                                                createSwitchButton(
                                                    'active',
                                                    formValues.active,
                                                    'feature.dnntJobs.form.fields.active.title',
                                                )
                                            }
                                        </Grid>
                                    </Grid>
                                </Grid>
                                <Grid item xs={6}>
                                    <Grid container alignItems={'center'} direction={'column'} spacing={1}>
                                        <Grid item>
                                            {
                                                createTextArea(
                                                    'description',
                                                    formValues.description,
                                                    'feature.dnntJobs.form.fields.description.title',
                                                )
                                            }
                                        </Grid>
                                    </Grid>
                                </Grid>
                            </Grid>
                        </Grid>
                        {
                            formValues.operation.direction !== null && (
                                <>
                                    <Divider variant={'middle'}/>
                                    <Grid item>
                                        {
                                            createSelectorField(
                                                'operation.sessionType',
                                                formValues.operation.sessionType,
                                                'feature.dnntJobs.form.fields.sessionType.title',
                                                jobSessionTypes,
                                                () => {

                                                }
                                            )
                                        }
                                    </Grid>
                                </>
                            )
                        }
                        {
                            formValues.operation.sessionType !== null && (
                                <>
                                    <Divider variant={'middle'}/>
                                    <Grid item>
                                        {
                                            createSelectorField(
                                                'operation.sessionType',
                                                formValues.operation.sessionType,
                                                'feature.dnntJobs.form.fields.sessionType.title',
                                                jobSessionTypes,
                                                () => {

                                                }
                                            )
                                        }
                                    </Grid>
                                </>
                            )
                        }
                        <Grid item>
                            <Grid container alignItems={'center'} direction={'row'} spacing={3}>
                                <Grid item xs={6} align={'right'}>
                                    <Button onClick={onSubmit} color={'primary'}>
                                        {t('feature.dnntJobs.form.buttons.submit.title')}
                                    </Button>
                                </Grid>
                                <Grid item xs={6} align={'left'}>
                                    <Button onClick={onCancel} color={'primary'}>
                                        {t('feature.dnntJobs.form.buttons.cancel.title')}
                                    </Button>
                                </Grid>
                            </Grid>
                        </Grid>
                    </Grid>
                </Box>
            </Box>
        </Modal>
    );
};
