import {useEffect} from 'react';
import {Box} from '@material-ui/core';
import {useDispatch, useSelector} from 'react-redux';

import {PauseIconButton, LaunchIconButton, StopIconButton} from "components/button/iconbuttons";
import {FofolaTable} from 'components/table/FofolaTable';
import {Paginator} from 'components/table/Paginator';

import {getCurrentPage, getIsLoading, getIsPaginatorEnabled, getSessions, setCurrentPage} from './slice';
import {requestSessionPage, pauseSession, launchSession, terminateSession} from './saga';
import {columns} from './constants';
import {SugoSessionStatus} from "../constants";

export const Table = () => {

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

    const isActive = (session) => session.status === SugoSessionStatus.Active.value;

    const isPaused = (session) => session.status === SugoSessionStatus.Paused.value;

    const createRowsWithButtons = () => sessions.map((session) => ({
        ...session,
        actions: (
            <Box>
                {isPaused(session) && (
                    <LaunchIconButton
                        onClick={() => dispatch(launchSession(session.id))}
                        tooltip={'feature.dnntSessions.table.buttons.launch.tooltip'}
                    />
                )}
                {isActive(session) && (
                    <PauseIconButton
                        onClick={() => dispatch(pauseSession(session.id))}
                        tooltip={'feature.dnntSessions.table.buttons.pause.tooltip'}
                    />
                )}
                {isPaused(session) && (
                    <StopIconButton
                        onClick={() => dispatch(terminateSession(session.id))}
                        tooltip={'feature.dnntSessions.table.buttons.terminate.tooltip'}
                    />
                )}
            </Box>
        ),
    }));

    return <Box>
        <FofolaTable
            columns={columns}
            rows={createRowsWithButtons()}
            paginator={paginator}
            isLoading={isLoading}
            loadingLabel={'feature.dnntSessions.table.loading.active'}
            notFoundLabel={'feature.dnntSessions.table.loading.notFound'}
        />
    </Box>
};
