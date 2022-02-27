import {useEffect} from 'react';
import {Box} from '@material-ui/core';
import {useDispatch, useSelector} from 'react-redux';

import {Paginator} from '../../components/table/Paginator';
import {FofolaTable} from '../../components/table/FofolaTable';

import {getCurrentPage, getIsLoading, getTransitions, setCurrentPage} from './slice';
import {requestSessionPage} from './saga';
import {columns} from './constants';

export const Table = () => {

    const dispatch = useDispatch();
    const page = useSelector(getCurrentPage);
    const isLoading = useSelector(getIsLoading);
    const transitions = useSelector(getTransitions);

    useEffect(() => {
        dispatch(requestSessionPage());
    }, [dispatch]);

    const handleChangePage = (newPage, processesPerPage) => {
        dispatch(setCurrentPage(newPage));
        dispatch(requestSessionPage());
    };

    const paginatorEnabled = () => transitions.size > 0;

    const paginator = <Paginator page={page} onChange={handleChangePage}  enabled={paginatorEnabled()} />

    return <Box>
        <FofolaTable
            columns={columns}
            rows={transitions}
            paginator={paginator}
            isLoading={isLoading}
            loadingLabel='Načítám změny...'
            notFoundLabel='Žadná DNNT změna nebyla nalezena...'
        />
    </Box>
};
