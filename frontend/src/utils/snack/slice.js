import {createSlice} from "@reduxjs/toolkit";

export const snackbarSlice = createSlice({
    name: 'snackbar',
    initialState: {
        notifications: [],
    },
    reducers: {
        addNotification: (state, action) => {
            state.notifications.push(action.payload);
        },
        removeNotification: (state, action) => {
            const key = action.payload;
            state.notifications = state.notifications.filter(notification => notification.key !== key);
        },
    }
});

export const getNotifications = state => state.snackBarModule.notifications;
export const {addNotification, removeNotification} = snackbarSlice.actions;
export const createActionType = actionName => snackbarSlice.name + "/" + actionName;
