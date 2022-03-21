import {useDispatch, useSelector} from 'react-redux';
import {Box} from '@material-ui/core';

import {CloseIconButton, RefreshIconButton} from '../../components/button/iconbuttons';
import {FofolaTable} from '../../components/table/FofolaTable';

import {getInfo, getIsLoading, removeInfo} from './slice';
import {synchronizeUuids} from './saga';
import {columns} from './constants';

export const Table = () => {

    const dispatch = useDispatch();
    const isLoading = useSelector(getIsLoading);
    const info = useSelector(getInfo);

    const createDataWithButtons = (data) => {
        return data.map((row) => ({
            tableAction: (
                <Box>
                    <CloseIconButton
                        onClick={() => {
                            const payload = {
                                uuid: row.uuid,
                                sourceIdentifier: row.sourceIdentifier,
                            };
                            dispatch(removeInfo(payload));
                        }}
                        tooltip={'Vymazat z tabulky'}
                    />
                </Box>
            ),
            ...row,
            action:
                <Box>
                    <RefreshIconButton
                        onClick={() => dispatch(synchronizeUuids([row.uuid]))}
                        tooltip={'Synchronizovat'}
                    />
                </Box>
        }));
    };

    const preparedRows = createDataWithButtons(info);

    return (
        <Box>
            <FofolaTable
                columns={columns}
                rows={preparedRows}
                isLoading={isLoading}
                loadingLabel='Načítám výsledky...'
                notFoundLabel='Žadný výsledek...'
            />
        </Box>
    );
};
