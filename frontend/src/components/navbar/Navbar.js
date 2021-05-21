import {Link} from "react-router-dom";

export const Navbar = () => (
    <nav className="nav-wrapper red darken-3">
        <div className="container">
            <a href="/" className="brand-logo">Fofola</a>
            <ul className="right">
                <li><Link to="/">Home</Link></li>
                <li><Link to="/reindex">Reindex</Link></li>
            </ul>
        </div>
    </nav>
);
