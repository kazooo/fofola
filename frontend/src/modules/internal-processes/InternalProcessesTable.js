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
import {Paginator} from "../../components/table/Paginator";
import {FofolaTable} from "../../components/table/FofolaTable";
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
                        <StopIconButton onClick={() => dispatch(stopInternalProcess(row.id))}/>
                    }
                    <DeleteIconButton onClick={() => dispatch(removeInternalProcess(row.id))}/>
                    <LogsIconButton onClick={() => dispatch(openInternalProcessLogs(row.id))} />
                </Box>
        }));
    }

    const preparedRows = createDataWithButtons(processesInfo);

    const handleChangePage = (newPage, processesPerPage) => {
        dispatch(setPage(newPage));
        dispatch(requestNewPageInternalProcesses());
    };

    const paginator = <Paginator page={page} onChange={handleChangePage}/>

    return <Box>
        <FofolaTable
            columns={columns}
            rows={preparedRows}
            paginator={paginator}
            isLoading={isLoading}
            loadingLabel="Načítám procesy..."
        />
    </Box>
};
