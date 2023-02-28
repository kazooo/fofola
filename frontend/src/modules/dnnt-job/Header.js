import {useDispatch} from "react-redux";
import {Box, Grid} from "@material-ui/core";
import {useEffect, useState} from "react";
import {requestJobPreviews} from "./saga";
import {useInterval} from "../../effects/useInterval";
import {Checkbox} from "../../components/form/Checkbox";
import {AddButton} from "../../components/button";
import {RefreshIconButton} from "../../components/button/iconbuttons";
import {openCreateJobForm} from "./slice";
import {JobForm} from "./JobForm";

export const Header = () => {
    const dispatch = useDispatch();
    const [autoReload, setAutoReload] = useState(false);
    const RELOAD_INTERVAL_MS = 5000;

    useEffect(() => {
        dispatch(requestJobPreviews())
    }, [dispatch]);

    useInterval(() => {
        dispatch(requestJobPreviews())
    }, autoReload ? RELOAD_INTERVAL_MS : null);

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
            <JobForm />
        </Box>
    )
};
