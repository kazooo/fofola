import {useDispatch} from "react-redux";
import {useEffect, useState} from "react";
import {useInterval} from "../../effects/useInterval";
import {Checkbox} from "../../components/form/Checkbox";
import {requestInternalProcesses, requestNewPageInternalProcesses} from "./saga";
import {Box, Grid} from "@material-ui/core";
import {RefreshIconButton} from "../../components/button/iconbuttons";

export const InternalProcessMenu = () => {
    const dispatch = useDispatch();
    const [autoReload, setAutoReload] = useState(true);
    const RELOAD_INTERVAL_MS = 5000;

    useEffect(() => {
        dispatch(requestNewPageInternalProcesses());
    }, []);

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
                    onClick={() => dispatch(requestInternalProcesses())}
                    tooltip={"Obnovit stránku procesů"}
                />
            </Grid>
        </Grid>
    );
};
