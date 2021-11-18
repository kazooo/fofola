import {useEffect} from "react";
import {useDispatch, useSelector} from "react-redux";
import Autocomplete from "@material-ui/lab/Autocomplete";
import {Box, FormControl, InputLabel, makeStyles, MenuItem, Select, TextField} from "@material-ui/core";

import {getDonator, getMode, getVcs, getVcUuid, setDonator, setMode, setVcUuid} from "./slice";
import {requestCheckDonatorOutputs} from "./saga";
import {donators, modes} from "./constants";

import {HorizontalDirectedGrid} from "../../components/layout/HorizontalDirectedGrid";
import {LoadingComponent} from "../../components/page/LoadingComponent";
import {useInterval} from "../../effects/useInterval";

const useStyles = makeStyles((theme) => ({
    formControl: {
        margin: theme.spacing(1),
        minWidth: 120,
    }
}));

export const CheckDonatorForm = () => {

    const classes = useStyles();
    const dispatch = useDispatch();
    const vcs = useSelector(state => getVcs(state));
    const mode = useSelector(state => getMode(state));
    const donator = useSelector(state => getDonator(state));

    const RELOAD_INTERVAL_MS = 5000;

    useEffect(() => {
        dispatch(requestCheckDonatorOutputs());
    });

    useInterval(() => {
        dispatch(requestCheckDonatorOutputs());
    }, RELOAD_INTERVAL_MS);

    const donatorOptions = donators.map((option, index) => (
        <MenuItem key={index} value={option.value}>
            {option.text}
        </MenuItem>
    ));

    const modeOptions = modes.map((option, index) => (
        <MenuItem key={index} value={option.value}>
            {option.text}
        </MenuItem>
    ));

    const loadVcUuid = (event, values) => {
        if (values) {
            dispatch(setVcUuid(values.uuid));
        } else {
            dispatch(setVcUuid(''));
        }
    }

    const changeDonator = donator => {
        dispatch(setDonator(donator));
    };

    const changeMode = mode => {
        dispatch(setMode(mode));
    };

    const form = <Box>
        <HorizontalDirectedGrid>
            <Autocomplete
                options={vcs}
                getOptionLabel={(option) => option.nameCz}
                style={{ width: 300 }}
                onChange={loadVcUuid}
                renderInput={(params) =>
                    <TextField
                        {...params}
                        label="Název virtuální sbirky"
                        variant="outlined"
                        size="small"
                    />
                }
            />
            <FormControl variant="outlined" size="small" className={classes.formControl}>
                <InputLabel>Donátor</InputLabel>
                <Select
                    value={donator}
                    onChange={e => changeDonator(e.target.value)}
                    label={"Donátor"}
                >
                    {donatorOptions}
                </Select>
            </FormControl>
            <FormControl variant="outlined" size="small" className={classes.formControl}>
                <InputLabel>Zkontrolovat</InputLabel>
                <Select
                    value={mode}
                    onChange={e => changeMode(e.target.value)}
                    label={"Zkontrolovat"}
                >
                    {modeOptions}
                </Select>
            </FormControl>
        </HorizontalDirectedGrid>
    </Box>;

    return <Box>
        {
            vcs.length > 0
                ? form
                : <LoadingComponent label={'Načítám virtuální sbirky...'} />
        }
    </Box>
};
