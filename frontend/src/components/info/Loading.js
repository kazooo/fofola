import {CircularProgress} from '@material-ui/core';
import {useTranslation} from 'react-i18next';

import {VerticalDirectedGrid} from '../layout/VerticalDirectedGrid';

export const Loading = ({label}) => {
    const {t} = useTranslation();

    return <VerticalDirectedGrid>
        <CircularProgress />
        {t(label)}
    </VerticalDirectedGrid>
}
