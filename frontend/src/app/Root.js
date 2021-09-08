import React from 'react';
import {Provider} from "react-redux";
import {SnackbarProvider} from "notistack";

import {App} from "./App";
import {store} from "../redux/store";
import {SnackbarToaster} from "../utils/snack/SnackbarToaster";

export const Root = () => (
    <Provider store={store}>
        <SnackbarProvider maxSnack={5}>
            <App />
            <SnackbarToaster />
        </SnackbarProvider>
    </Provider>
);
