import {Box} from "@material-ui/core";
import {useDispatch, useSelector} from "react-redux";

import {getCurrentPage, getInternalProcesses, getIsLoading, setPage} from "./slice";
import {
    openInternalProcessLogs,
    removeInternalProcess,
    requestNewPageInternalProcesses,
    stopInternalProcess
} from "./saga";

import {DeleteIconButton, LogsIconButton, StopIconButton} from "../../components/button/iconbuttons";
import {FofolaTable} from "../../components/table/FofolaTable";
import {Paginator} from "../../components/table/Paginator";
import {columns} from "./constants";

export const InternalProcessesTable = () => {

    const dispatch = useDispatch();
    const page = useSelector(state => getCurrentPage(state));
    const isLoading = useSelector(state => getIsLoading(state));
    const processesInfo = useSelector(state => getInternalProcesses(state));

    const createDataWithButtons = (data) => {
        return data.map((row) => ({
            ...row,
            action:
                <Box>
                    {
                        row.state === 'ACTIVE' &&
                        <StopIconButton
                            onClick={() => dispatch(stopInternalProcess(row.id))}
                            tooltip={"Pozastavit proces"}
                        />
                    }
                    <DeleteIconButton
                        onClick={() => dispatch(removeInternalProcess(row.id))}
                        tooltip={"Vymazat proces"}
                    />
                    <LogsIconButton
                        onClick={() => dispatch(openInternalProcessLogs(row.id))}
                        tooltip={"Logy procesu"}
                    />
                </Box>
        }));
    }

    const preparedRows = createDataWithButtons(processesInfo);

    const handleChangePage = (newPage, processesPerPage) => {
        dispatch(setPage(newPage));
        dispatch(requestNewPageInternalProcesses());
    };

    const paginatorEnabled = () => processesInfo.size > 0;

    const paginator = <Paginator page={page} onChange={handleChangePage}  enabled={paginatorEnabled()} />

    return <Box>
        <FofolaTable
            columns={columns}
            rows={preparedRows}
            paginator={paginator}
            isLoading={isLoading}
            loadingLabel="Načítám procesy..."
            notFoundLabel="Fofola nemá žadný proces"
        />
    </Box>
};
