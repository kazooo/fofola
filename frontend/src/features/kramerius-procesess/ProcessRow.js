import {useDispatch} from "react-redux";
import {RemoveButton, StopButton} from "../../components/button";
import {removeProcess, stopProcess} from "./saga";

export const ProcessRow = ({data}) => {

    const dispatch = useDispatch();

    return <tr>
        <td>{data.def}</td>
        <td>{data.name}</td>
        <td>{data.state}</td>
        <td>{data.planned}</td>
        <td>{data.started}</td>
        <td>{data.finished}</td>
        <td>
            <StopButton onClick={() => dispatch(stopProcess(data.uuid))} />
            <RemoveButton onClick={() => dispatch(removeProcess(data.uuid))} />
        </td>
    </tr>
};
