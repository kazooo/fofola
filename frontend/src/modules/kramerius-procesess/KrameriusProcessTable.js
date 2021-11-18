import {useDispatch, useSelector} from "react-redux";
import {Box} from "@material-ui/core";
import {FofolaTable} from "../../components/table/FofolaTable";
import {getCurrentPage, getIsLoading, getProcessesInfo, setPage} from "./slice";
import {columns} from "./constants";
import {DeleteIconButton, StopIconButton} from "../../components/button/iconbuttons";
import {removeProcess, requestNewPageProcessesInfo, stopProcess} from "./saga";
import {Paginator} from "../../components/table/Paginator";

export const KrameriusProcessTable = () => {

    const dispatch = useDispatch();
    const page = useSelector(state => getCurrentPage(state));
    const isLoading = useSelector(state => getIsLoading(state));
    const processesInfo = useSelector(state => getProcessesInfo(state));

    const createDataWithButtons = (data) => {
        return data.map((row) => ({
            ...row,
            action:
                <Box>
                    <StopIconButton onClick={() => dispatch(stopProcess([row.uuid]))}/>
                    <DeleteIconButton onClick={() => dispatch(removeProcess([row.uuid]))}/>
                </Box>
        }));
    }

    const preparedRows = createDataWithButtons(processesInfo);

    const handleChangePage = (newPage, processesPerPage) => {
        dispatch(setPage(newPage));
        dispatch(requestNewPageProcessesInfo());
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
}
