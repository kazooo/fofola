import {useDispatch, useSelector} from "react-redux";
import {Box} from "@material-ui/core";

import {DeleteIconButton, StopIconButton} from "components/button/iconbuttons";
import {getCurrentPage, getIsLoading, getProcessesInfo, setPage} from "./slice";
import {removeProcess, requestNewPageProcessesInfo, stopProcess} from "./saga";
import {FofolaTable} from "components/table/FofolaTable";
import {Paginator} from "components/table/Paginator";
import {columns} from "./constants";

export const KrameriusProcessTable = () => {

    const dispatch = useDispatch();
    const page = useSelector(state => getCurrentPage(state));
    const isLoading = useSelector(state => getIsLoading(state));
    const processesInfo = useSelector(state => getProcessesInfo(state));

    const canStop = state => state === 'RUNNING' || state === 'PLANNED';

    const createDataWithButtons = (data) => {
        return data.map((row) => ({
            ...row,
            action:
                <Box>
                    {
                        canStop(row.state) && (
                            <StopIconButton
                                onClick={() => dispatch(stopProcess(row.id))}
                                tooltip={"Pozastavit process"}
                            />
                        )
                    }
                    <DeleteIconButton
                        onClick={() => dispatch(removeProcess(row.id))}
                        tooltip={"Vymazat process"}
                    />
                </Box>
        }));
    }

    const preparedRows = createDataWithButtons(processesInfo);

    const handleChangePage = (newPage, processesPerPage) => {
        dispatch(setPage(newPage));
        dispatch(requestNewPageProcessesInfo());
    };

    const paginatorEnabled = () => processesInfo.length > 0;

    const paginator = <Paginator page={page} onChange={handleChangePage} enabled={paginatorEnabled()} />

    return <Box>
        <FofolaTable
            columns={columns}
            rows={preparedRows}
            paginator={paginator}
            isLoading={isLoading}
            loadingLabel="Načítám procesy..."
            notFoundLabel="Připojený Kramerius nevratil žadný proces"
        />
    </Box>
}
