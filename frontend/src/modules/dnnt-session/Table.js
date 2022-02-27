import {Box} from '@material-ui/core';
import {useDispatch, useSelector} from 'react-redux';

import {FofolaTable} from '../../components/table/FofolaTable';
import {Paginator} from '../../components/table/Paginator';

import {getCurrentPage, getIsLoading, getSessions, setCurrentPage} from './slice';
import {requestSessionPage} from "./saga";
import {columns} from './constants';
import {useEffect} from "react";

export const  Table = () => {

    const dispatch = useDispatch();
    const page = useSelector(getCurrentPage);
    const isLoading = useSelector(getIsLoading);
    const sessions = useSelector(getSessions);

    useEffect(() => {
        dispatch(requestSessionPage());
    }, [dispatch]);

    const handleChangePage = (newPage, processesPerPage) => {
        dispatch(setCurrentPage(newPage));
        dispatch(requestSessionPage());
    };

    const paginatorEnabled = () => sessions.size > 0;

    const paginator = <Paginator page={page} onChange={handleChangePage}  enabled={paginatorEnabled()} />

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
