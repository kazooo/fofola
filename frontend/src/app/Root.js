import React from 'react';
import {Provider} from "react-redux";
import {SnackbarProvider} from "notistack";

import {App} from "./App";
import {store} from "../redux/store";
import {SnackbarToaster} from "../utils/snack/SnackbarToaster";
import {AdapterDayjs} from "@mui/x-date-pickers/AdapterDayjs";
import {LocalizationProvider} from "@mui/x-date-pickers/LocalizationProvider";

export const Root = () => (
    <Provider store={store}>
        <LocalizationProvider dateAdapter={AdapterDayjs}>
            <SnackbarProvider maxSnack={5}>
                <App />
                <SnackbarToaster />
            </SnackbarProvider>
        </LocalizationProvider>
    </Provider>
);
