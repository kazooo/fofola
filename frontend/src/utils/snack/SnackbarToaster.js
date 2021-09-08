import {useDispatch, useSelector} from "react-redux";
import {useSnackbar} from "notistack";

import {getNotifications, removeNotification} from "./slice";

export const SnackbarToaster = () => {
    const dispatch = useDispatch();
    const {enqueueSnackbar} = useSnackbar();

    const notifications = useSelector(state => getNotifications(state));

    notifications.forEach(({ key, message, variant = 'default' }) => {
        enqueueSnackbar(message,
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
