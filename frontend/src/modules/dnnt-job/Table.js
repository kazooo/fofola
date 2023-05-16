import {useDispatch, useSelector} from 'react-redux';
import {Box} from '@material-ui/core';

import {DeleteIconButton, EditIconButton, TriggerIconButton} from 'components/button/iconbuttons';
import {FofolaTable} from 'components/table/FofolaTable';
import {getIsLoading, getJobs} from './slice';
import {jobTableColumns} from './table';
import {deleteJob, toggleJobActivity, openUpdateJobForm, triggerJob} from './saga';
import {TooltipedSwitch} from 'components/button/switch';

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
            actions: (
                <Box>
                    <TooltipedSwitch
                        tooltip={getLabel(job)}
                        checked={job.active}
                        onChange={() => dispatch(toggleJobActivity(job.id))}
                    />
                    <TriggerIconButton
                        onClick={() => dispatch(triggerJob(job.id))}
                        tooltip={'feature.dnntJobs.table.buttons.triggerJob.tooltip'}
                    />
                    <EditIconButton
                        onClick={() => dispatch(openUpdateJobForm(job.id))}
                        tooltip={'feature.dnntJobs.table.buttons.editJob.tooltip'}
                    />
                    <DeleteIconButton
                        onClick={() => dispatch(deleteJob(job.id))}
                        tooltip={'feature.dnntJobs.table.buttons.deleteJob.tooltip'}
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
                notFoundLabel={'feature.dnntJobs.table.loading.notFound'}
            />
        </Box>
    )
};
