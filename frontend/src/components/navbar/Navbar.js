import {Link} from "react-router-dom";
import React from "react";

export const Navbar = () => (
    <nav className="nav-wrapper red darken-3">
        <div className="container">
            <Link to="/" className="brand-logo">Fofola</Link>
        </div>
    </nav>
);
