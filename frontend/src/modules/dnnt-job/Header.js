import {useDispatch, useSelector} from 'react-redux';
import {Box, Grid} from '@material-ui/core';
import {useEffect, useState} from 'react';

import {RefreshIconButton} from 'components/button/iconbuttons';
import {Checkbox} from 'components/form/Checkbox';
import {useInterval} from 'effects/useInterval';
import {AddButton} from 'components/button';

import {getJobFormType, getJobFormValues, openCreateJobForm} from './slice';
import {requestJobPreviews} from './saga';
import {JobForm} from './JobForm';

export const Header = () => {
    const dispatch = useDispatch();
    const formValues = useSelector(getJobFormValues);
    const [autoReload, setAutoReload] = useState(false);
    const RELOAD_INTERVAL_MS = 5000;

    useEffect(() => {
        dispatch(requestJobPreviews())
    }, [dispatch]);

    useInterval(() => {
        dispatch(requestJobPreviews())
    }, autoReload ? RELOAD_INTERVAL_MS : null);

    const [isFormOpen, setFormOpen] = useState(false);
    const jobFormType = useSelector(getJobFormType);

    useEffect(() => {
        setFormOpen(jobFormType !== null)
    }, [jobFormType]);

    return (
        <Box>
            <Grid container spacing={2} alignItems={'center'}>
                <Grid item>
                    <Checkbox
                        label={'feature.dnntJobs.header.autoReload'}
                        onChange={() => setAutoReload(!autoReload)}
                        checked={autoReload}
                    />
                </Grid>
                <Grid item>
                    <RefreshIconButton
                        onClick={() => dispatch(requestJobPreviews())}
                        tooltip={'feature.dnntJobs.header.buttons.refresh.tooltip'}
                    />
                </Grid>
                <Grid item>
                    <AddButton
                        onClick={() => dispatch(openCreateJobForm())}
                        label={'feature.dnntJobs.header.buttons.create.title'}
                    />
                </Grid>
            </Grid>
            {isFormOpen && <JobForm type={jobFormType} values={formValues} />}
        </Box>
    )
};
