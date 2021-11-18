import {useDispatch, useSelector} from "react-redux";
import {addUuids, getDonator, getMode, setDonator, setMode} from "./slice";
import {Box, FormControl, InputLabel, makeStyles, MenuItem, Select} from "@material-ui/core";
import {LoadUuidsForm} from "../../components/form/LoadUuidsForm";
import {HorizontalDirectedGrid} from "../../components/layout/HorizontalDirectedGrid";
import {donators, modes} from "./constants";

const useStyles = makeStyles((theme) => ({
    formControl: {
        margin: theme.spacing(1),
        minWidth: 120,
    }
}));

export const LinkDonatorForm = () => {

    const classes = useStyles();
    const dispatch = useDispatch();
    const mode = useSelector(state => getMode(state));
    const donator = useSelector(state => getDonator(state));

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

    const loadUuids = (uuids) => {
        dispatch(addUuids(uuids));
    };

    const changeDonator = (donator) => {
        dispatch(setDonator(donator));
    };

    const changeMode = (mode) => {
        dispatch(setMode(mode));
    }

    return <Box>
        <LoadUuidsForm addUuids={loadUuids} />
        <HorizontalDirectedGrid>
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
                <InputLabel>Režim</InputLabel>
                <Select
                    value={mode}
                    onChange={e => changeMode(e.target.value)}
                    label={"Režim"}
                >
                    {modeOptions}
                </Select>
            </FormControl>
        </HorizontalDirectedGrid>
    </Box>
};
