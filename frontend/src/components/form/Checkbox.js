import {FormControlLabel, Checkbox as MuiCheckbox} from '@material-ui/core';

export const Checkbox = ({label, checked, onChange}) => {
    return (
        <FormControlLabel
            control={
                <MuiCheckbox
                    checked={checked}
                    color={'primary'}
                />
            }
            onChange={onChange}
            label={label}
        />
    );
};
