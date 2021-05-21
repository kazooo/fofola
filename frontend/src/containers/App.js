import React from 'react';
import {BrowserRouter, Route} from 'react-router-dom'
import {Home} from "./Home";
import {Reindex} from "./Reindex";

export const App = () => (
    <BrowserRouter>
        <Route exact path='/' component={Home} />
        <Route path='/reindex' component={Reindex} />
    </BrowserRouter>
);
