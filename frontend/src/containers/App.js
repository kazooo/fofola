import React from 'react';
import {BrowserRouter, Route} from 'react-router-dom'
import {Home} from "./Home";
import {Reindex} from "./Reindex";
import {Navbar} from "../components/navbar/Navbar";

export const App = () => (
    <BrowserRouter>
        <Navbar />
        <Route exact path='/' component={Home} />
        <Route path='/reindex' component={Reindex} />
    </BrowserRouter>
);
