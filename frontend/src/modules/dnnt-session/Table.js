import {Box} from '@material-ui/core';
import {useDispatch, useSelector} from 'react-redux';

import {FofolaTable} from '../../components/table/FofolaTable';
import {Paginator} from '../../components/table/Paginator';

import {getCurrentPage, getIsLoading, getIsPaginatorEnabled, getSessions, setCurrentPage} from './slice';
import {requestSessionPage} from './saga';
import {columns} from './constants';
import {useEffect} from 'react';

export const  Table = () => {

    const dispatch = useDispatch();
    const page = useSelector(getCurrentPage);
    const isLoading = useSelector(getIsLoading);
    const sessions = useSelector(getSessions);
    const paginatorEnabled = useSelector(getIsPaginatorEnabled);

    useEffect(() => {
        dispatch(requestSessionPage());
        return () => {
            dispatch(setCurrentPage(0));
        }
    }, [dispatch]);

    const handleChangePage = (newPage, processesPerPage) => {
        dispatch(setCurrentPage(newPage));
        dispatch(requestSessionPage());
    };

    const paginator = <Paginator page={page} onChange={handleChangePage}  enabled={paginatorEnabled} />

    return <Box>
        <FofolaTable
            columns={columns}
            rows={sessions}
            paginator={paginator}
            isLoading={isLoading}
            loadingLabel='Načítám procesy...'
            notFoundLabel='Žadný DNNT process nebyl nalezen...'
        />
    </Box>
};
