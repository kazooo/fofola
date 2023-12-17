import {Link} from 'react-router-dom';
import {useTranslation} from 'react-i18next';

import './Service.css';

export const Service = ({title, link, enabled}) => {
    const {t} = useTranslation();
    
    return (
        <Link
            to={link}
            className={enabled ? 'button-enabled' : 'button-disabled'}
        >
            {t(title)}
        </Link>
    );
};
