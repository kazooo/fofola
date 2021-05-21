import React from "react";
import {Link} from "react-router-dom";
import homeImg from '../../img/home.png';

export const Navbar = () => (
    <Link to="/"><img src={homeImg} width="100" height="100" alt="Fofola Logo"/></Link>
);
