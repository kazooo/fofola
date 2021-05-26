import {Table} from "../../components/container/Table";
import {useSelector} from "react-redux";
import {ProcessRow} from "./ProcessRow";
import {getProcessesInfo} from "./slice";

export const ProcessTable = () => {

    const processesInfo = useSelector(state => getProcessesInfo(state));
    const header = ["Typ", "Popis", "Stav", "Naplánováno", "Spuštěno", "Ukončeno", "Operace"];

    return processesInfo.length > 0 &&
        <div>
            <Table header={header}>
                {processesInfo.map(info => <ProcessRow data={info} />)}
            </Table>
        </div>
};
