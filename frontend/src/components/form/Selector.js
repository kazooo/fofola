import {FormControl, InputLabel, makeStyles, MenuItem, Select} from "@material-ui/core";
import {useTranslation} from "react-i18next";

const useStyles = makeStyles((theme) => ({
    formControl: {
        margin: theme.spacing(1),
        minWidth: 120,
    },
}));

export const Selector = ({selectOptions,
                         selectLabel,
                         selectedOption,
                         onSelectOptionChange}) => {

    const {t} = useTranslation();
    const classes = useStyles();

    const options = selectOptions.map((option, index) => (
        <MenuItem key={index} value={option.value}>
            {option.text}
        </MenuItem>
    ))

    const selectedValue = selectOptions.find(
        option => option.value === selectedOption
    )?.value;

    return <FormControl variant="outlined" size="small" className={classes.formControl}>
        <InputLabel>{t(selectLabel)}</InputLabel>
        <Select
            value={selectedValue ? selectedValue : ''}
            onChange={e => onSelectOptionChange(e.target.value)}
            label={t(selectLabel)}
        >
            {options}
        </Select>
    </FormControl>
};
