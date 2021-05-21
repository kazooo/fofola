import React from 'react';
import {BrowserRouter, Route} from 'react-router-dom'
import {Home} from "./Home";
import {Reindex} from "../features/reindex/Reindex";
import {Delete} from "../features/delete/Delete";
import {ChangeAccess} from "../features/access/ChangeAccess";

export const App = () => (
    <BrowserRouter>
        <Route exact path='/' component={Home} />
        <Route path='/access' component={ChangeAccess} />
        <Route path='/reindex' component={Reindex} />
        <Route path='/delete' component={Delete} />
    </BrowserRouter>
);
