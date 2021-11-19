import {Link} from "react-router-dom";
import './Service.css';

export const Service = (props) => (
    <Link to={props.link} className="button">
        {props.children}
    </Link>
);
