import {FormControlLabel, Checkbox as MuiCheckbox} from '@material-ui/core';
import {useTranslation} from "react-i18next";

export const Checkbox = ({label, checked, onChange}) => {
    const {t} = useTranslation();

    return (
        <FormControlLabel
            control={
                <MuiCheckbox
                    checked={checked}
                    color={'primary'}
                />
            }
            onChange={onChange}
            label={t(label)}
        />
    );
};
