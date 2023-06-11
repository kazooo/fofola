import {Box} from '@material-ui/core';
import React from 'react';
import i18n from 'i18next';
import {useDispatch, useSelector} from 'react-redux';

import {InfoIconButton} from 'components/button/iconbuttons';
import {FofolaTable} from 'components/table/FofolaTable';

import {AlertIssueTypeTranslationKey} from './constants';
import {getAlerts, getIsLoading} from './slice';
import {alertTableColumns} from './table';
import {openAlertWindow} from './saga';

export const Table = ({classes}) => {

    const dispatch = useDispatch();
    const alerts = useSelector(getAlerts);
    const isLoading = useSelector(getIsLoading);

    const createRows = () => alerts.map((alert) => {
        const params = alert.parameters;
        const issueTypeTranslationKey = AlertIssueTypeTranslationKey[alert.issueType];
        return ({
            ...alert,
            issueType: i18n.t(`constant.sugo.alert.issueType.${issueTypeTranslationKey}.title`),
            shortDescription: i18n.t(`constant.sugo.alert.issueType.${issueTypeTranslationKey}.shortDescription`, params),
            actions: (
                <Box>
                    <InfoIconButton
                        onClick={() => dispatch(openAlertWindow(alert.id))}
                        tooltip={'feature.dnntAlerts.table.buttons.openAlert.tooltip'}
                    />
                </Box>
            )
        })
    });

    return <Box className={classes.root}>
        <FofolaTable
            columns={alertTableColumns}
            rows={createRows()}
            isLoading={isLoading}
            loadingLabel={'feature.dnntAlerts.table.loading.active'}
            notFoundLabel={'feature.dnntAlerts.table.loading.notFound'}
        />
    </Box>;
};
