import {useDispatch} from "react-redux";
import {useEffect, useState} from "react";
import {Grid} from "@material-ui/core";

import {requestInternalProcesses, requestNewPageInternalProcesses} from "./saga";
import {RefreshIconButton} from "../../components/button/iconbuttons";
import {Checkbox} from "../../components/form/Checkbox";
import {useInterval} from "../../effects/useInterval";

export const InternalProcessMenu = () => {
    const dispatch = useDispatch();
    const [autoReload, setAutoReload] = useState(true);
    const RELOAD_INTERVAL_MS = 5000;

    useEffect(() => {
        dispatch(requestNewPageInternalProcesses());
    }, [dispatch]);

    useInterval(() => {
        dispatch(requestInternalProcesses());
    }, autoReload ? RELOAD_INTERVAL_MS : null);

    return (
        <Grid container spacing={2} alignItems={"center"}>
            <Grid item>
                <Checkbox
                    label={"Načítat automaticky (interval: 5 sec.)"}
                    onChange={() => setAutoReload(!autoReload)}
                    checked={autoReload}
                />
            </Grid>
            <Grid item>
                <RefreshIconButton
                    onClick={() => dispatch(requestNewPageInternalProcesses())}
                    tooltip={"Obnovit stránku procesů"}
                />
            </Grid>
        </Grid>
    );
};
