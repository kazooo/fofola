import {useInterval} from "../../effects/useInterval";
import {Checkbox} from "../../components/form/Checkbox";
import {useDispatch} from "react-redux";
import {useState} from "react";
import {requestInternalProcesses} from "./saga";

export const AutoReload = () => {

    const dispatch = useDispatch();
    const [autoReload, setAutoReload] = useState(true);

    useInterval(() => {
        dispatch(requestInternalProcesses());
    }, autoReload ? 5000 : null);

    return <Checkbox
        label={"Načítat automaticky (interval: 5 sec.)"}
        onChange={() => setAutoReload(!autoReload)}
        checked={autoReload}
    />;
};
