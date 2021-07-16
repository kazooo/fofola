import {Checkbox} from "../../components/form/Checkbox";
import {useDispatch} from "react-redux";
import {useInterval} from "../../effects/useInterval";
import {requestNewPageProcessesInfo, requestProcessesInfo} from "./saga";
import {useEffect, useState} from "react";

export const KrameriusProcessMenu = () => {

    const dispatch = useDispatch();
    const [autoReload, setAutoReload] = useState(true);
    const RELOAD_INTERVAL_MS = 5000;

    useEffect(() => {
        dispatch(requestNewPageProcessesInfo());
    });

    useInterval(() => {
        dispatch(requestProcessesInfo());
    }, autoReload ? RELOAD_INTERVAL_MS : null);

    return <Checkbox
        label={"Načítat automaticky (interval: 5 sec.)"}
        onChange={() => setAutoReload(!autoReload)}
        checked={autoReload}
    />;
};
