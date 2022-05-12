import {Link} from 'react-router-dom';
import {useTranslation} from 'react-i18next';

import './Service.css';

export const Service = ({title, link}) => {
    const {t} = useTranslation();
    
    return (
        <Link to={link} className='button'>
            {t(title)}
        </Link>
    );
};
