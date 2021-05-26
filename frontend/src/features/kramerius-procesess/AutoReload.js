import {Checkbox} from "../../components/form/Checkbox";
import {useDispatch} from "react-redux";
import {useInterval} from "../../effects/useInterval";
import {requestProcessesInfo} from "./saga";
import {useState} from "react";

export const AutoReload = () => {

    const dispatch = useDispatch();
    const [autoReload, setAutoReload] = useState(true);

    useInterval(() => {
        dispatch(requestProcessesInfo());
    }, autoReload ? 5000 : null);

    return <Checkbox
        label={"Načítat automaticky (interval: 5 sec.)"}
        onChange={() => setAutoReload(!autoReload)}
        checked={autoReload}
    />;
};
