import {useDispatch, useSelector} from 'react-redux';
import {Box, Switch} from '@material-ui/core';

import {DeleteIconButton, EditIconButton} from "../../components/button/iconbuttons";
import {FofolaTable} from "../../components/table/FofolaTable";
import {getIsLoading, getJobs, openUpdateJobForm} from "./slice";
import {jobTableColumns} from "./constants";
import {deleteJob, toggleJobActivity} from "./saga";

export const Table = () => {
    const dispatch = useDispatch();
    const isLoading = useSelector(getIsLoading);
    const jobs = useSelector(getJobs);

    const getLabel = (job) => job.active ?
        'feature.dnntJobs.table.buttons.toggleJobActivity.tooltip.disable'
        :
        'feature.dnntJobs.table.buttons.toggleJobActivity.tooltip.enable';

    const createRowsWithButtons = () =>
        jobs.map((job) => ({
            ...job,
            action: (
                <Box>
                    <Switch
                        label={getLabel(job)}
                        value={job.active}
                        onChange={() => dispatch(toggleJobActivity(job.id))}
                    />
                    <EditIconButton
                        onClick={() => dispatch(openUpdateJobForm())}
                        tooltip={'feature.dnntJobs.table.buttons.editJob'}
                    />
                    <DeleteIconButton
                        onClick={() => dispatch(deleteJob(job.id))}
                        tooltip={'feature.dnntJobs.table.buttons.editJob'}
                    />
                </Box>
            )
        }));

    return (
        <Box>
            <FofolaTable
                columns={jobTableColumns}
                rows={createRowsWithButtons()}
                isLoading={isLoading}
                loadingLabel={'feature.dnntJobs.table.loading.active'}
                notFoundLabel={'feature.dnntJobs.table.loading.error'}
            />
        </Box>
    )
};
