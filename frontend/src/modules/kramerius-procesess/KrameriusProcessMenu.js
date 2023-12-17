import {Grid} from "@material-ui/core";
import {useEffect, useState} from "react";
import {useDispatch} from "react-redux";

import {requestNewPageProcessesInfo, requestProcessesInfo} from "./saga";
import {RefreshIconButton} from "components/button/iconbuttons";
import {Checkbox} from "components/form/Checkbox";
import {useInterval} from "effects/useInterval";

export const KrameriusProcessMenu = () => {
    const dispatch = useDispatch();
    const [autoReload, setAutoReload] = useState(true);
    const RELOAD_INTERVAL_MS = 5000;

    useEffect(() => {
        dispatch(requestNewPageProcessesInfo());
    }, [dispatch]);

    useInterval(() => {
        dispatch(requestProcessesInfo());
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
                    onClick={() => dispatch(requestNewPageProcessesInfo())}
                    tooltip={"Obnovit stránku procesů"}
                />
            </Grid>
        </Grid>
    );
};
