import {useDispatch, useSelector} from "react-redux";
import {
    Box,
    Checkbox,
    FormControl,
    FormControlLabel,
    InputLabel,
    makeStyles,
    MenuItem,
    Select
} from "@material-ui/core";

import {LoadUuidsForm} from "../../components/form/LoadUuidsForm";
import {HorizontalDirectedGrid} from "../../components/layout/HorizontalDirectedGrid";
import {
    addUuids,
    getMode,
    isLabelSelectorDisabled,
    setLabel,
    setMode,
    setProcessRecursive,
    isProcessRecursiveCheckboxDisabled,
    getProcessRecursiveByMode,
    getLabelByMode,
} from "./slice";
import {dnntLabels} from "../constants";
import {modes} from './constants';
import {useTranslation} from "react-i18next";

const useStyles = makeStyles((theme) => ({
    formControl: {
        margin: theme.spacing(1),
        minWidth: 120,
    }
}));

export const LinkDnntForm = () => {
    const {t} = useTranslation();
    const classes = useStyles();
    const dispatch = useDispatch();
    const mode = useSelector(getMode);
    const label = useSelector(getLabelByMode);
    const processRecursive = useSelector(getProcessRecursiveByMode);

    const processRecursiveDisabled = useSelector(isProcessRecursiveCheckboxDisabled);
    const labelSelectorDisabled = useSelector(isLabelSelectorDisabled);

    const createOptions = options => options.map((option, index) => (
        <MenuItem key={index} value={option.value}>
            {t(option.text)}
        </MenuItem>
    ));

    const modeOptions = createOptions(modes);
    const labelOptions = createOptions(dnntLabels);

    const loadUuids = (uuids) => {
        dispatch(addUuids(uuids));
    };

    const changeLabel = (label) => {
        dispatch(setLabel(label));
    };

    const changeMode = (mode) => {
        dispatch(setMode(mode));
    }

    const changeProcessRecursive = (_, checked) => {
        dispatch(setProcessRecursive(checked));
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
                    disabled={labelSelectorDisabled}
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
            <FormControlLabel
                control={<Checkbox
                    checked={processRecursive}
                    onChange={changeProcessRecursive}
                    color="primary"
                    disabled={processRecursiveDisabled}
                />}
                label="Zpracovat všechny podřízené časti"
            />
        </HorizontalDirectedGrid>
    </Box>
};
