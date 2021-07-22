import {useDispatch, useSelector} from "react-redux";
import {Box, FormControl, InputLabel, makeStyles, MenuItem, Select} from "@material-ui/core";

import {LoadUuidsForm} from "../../components/temporary/LoadUuidsForm";
import {HorizontalDirectedGrid} from "../../components/temporary/HorizontalDirectedGrid";
import {addUuids, getLabel, getMode, setLabel, setMode} from "./slice";
import {labels, modes} from './constants';

const useStyles = makeStyles((theme) => ({
    formControl: {
        margin: theme.spacing(1),
        minWidth: 120,
    }
}));

export const LinkDnntForm = () => {
    const classes = useStyles();
    const dispatch = useDispatch();
    const mode = useSelector(state => getMode(state));
    const label = useSelector(state => getLabel(state));

    const createOptions = options => options.map((option, index) => (
        <MenuItem key={index} value={option.value}>
            {option.text}
        </MenuItem>
    ));

    const modeOptions = createOptions(modes);
    const labelOptions = createOptions(labels);

    const loadUuids = (uuids) => {
        dispatch(addUuids(uuids));
    };

    const changeLabel = (label) => {
        dispatch(setLabel(label));
    };

    const changeMode = (mode) => {
        dispatch(setMode(mode));
    }

    return <Box>
        <LoadUuidsForm addUuids={loadUuids} />
        <HorizontalDirectedGrid>
            <FormControl variant="outlined" size="small" className={classes.formControl}>
                <InputLabel>Label</InputLabel>
                <Select
                    value={label}
                    onChange={e => changeLabel(e.target.value)}
                    label={"Label"}
                >
                    {labelOptions}
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
