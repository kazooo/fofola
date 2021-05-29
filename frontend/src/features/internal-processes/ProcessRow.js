import {useDispatch} from "react-redux";
import {LogsButton, RemoveButton, StopButton} from "../../components/button";
import {openInternalProcessLogs, removeInternalProcess, stopInternalProcess} from "./saga";

export const ProcessRow = ({data}) => {

    const dispatch = useDispatch();

    return <tr>
        <td>{data.id}</td>
        <td>{data.type}</td>
        <td>{data.state}</td>
        <td>{data.start}</td>
        <td>{data.finish}</td>
        <td>{data.terminationReason}</td>
        <td>
            <StopButton onClick={() => dispatch(stopInternalProcess(data.id))} />
            <RemoveButton onClick={() => dispatch(removeInternalProcess(data.id))} />
            <LogsButton onClick={() => dispatch(openInternalProcessLogs(data.id))} />
        </td>
    </tr>
};
