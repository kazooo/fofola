import {useSelector} from 'react-redux';
import {Box} from '@material-ui/core';

import {FofolaTable} from '../../components/table/FofolaTable';

import {getInfo, getIsLoading} from './slice';
import {columns} from './constants';

export const Table = () => {
    
    const isLoading = useSelector(getIsLoading);
    const info = useSelector(getInfo);

    return (
        <Box>
            <FofolaTable
                columns={columns}
                rows={info}
                isLoading={isLoading}
                loadingLabel='Načítám výsledky...'
                notFoundLabel='Žadný výsledek...'
            />
        </Box>
    );
};
