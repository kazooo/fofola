import {useDispatch, useSelector} from 'react-redux';
import {useSnackbar} from 'notistack';

import {getNotifications, removeNotification} from './slice';
import i18n from 'utils/i18n';
import {Trans} from 'react-i18next';
import React from 'react';

export const SnackbarToaster = () => {
    const dispatch = useDispatch();
    const {enqueueSnackbar} = useSnackbar();

    const notifications = useSelector(state => getNotifications(state));

    notifications.forEach(({ key, message, props, variant = 'default' }) => {
        enqueueSnackbar(
            <Trans
                i18n={i18n}
                i18nKey={message}
                values={props}
            />,
            {
                key,
                variant,
                autoHideDuration: 3000,
                anchorOrigin: {
                    vertical: 'top',
                    horizontal: 'right',
                },
            },
        );
        dispatch(removeNotification(key));
    });

    // Shell Component
    // Does not render anything, but uses hooks in order to update the store
    return <></>;
};
