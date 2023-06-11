import ErrorOutlineOutlinedIcon from '@material-ui/icons/ErrorOutlineOutlined';
import {VerticalDirectedGrid} from '../layout/VerticalDirectedGrid';
import {useTranslation} from 'react-i18next';

export const Error = ({label}) => {
    const {t} = useTranslation();
    return <VerticalDirectedGrid>
        <ErrorOutlineOutlinedIcon fontSize='large' color='error' />
        {t(label)}
    </VerticalDirectedGrid>
}
