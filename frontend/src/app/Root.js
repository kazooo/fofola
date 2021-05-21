import React from 'react';
import {App} from "./App";
import {Provider} from "react-redux";
import {store} from "../redux/store";

export const Root = () => (
    <Provider store={store}>
        <App />
    </Provider>
);
