import {Table} from "../../components/container/Table";
import {useSelector} from "react-redux";
import {getInternalProcesses} from "./slice";
import {ProcessRow} from "./ProcessRow";

export const ProcessTable = () => {

    const internalProcesses = useSelector(state => getInternalProcesses(state));
    const header = ["PID", "Typ", "Stav", "Spuštěno", "Ukončeno", "Info", "Operace"];

    return internalProcesses.length > 0 && <div>
        <Table header={header}>
            {internalProcesses.map(info =>
                <ProcessRow data={info} />
            )}
        </Table>
    </div>
};
