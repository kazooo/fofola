import {useTranslation} from 'react-i18next';
import {TextField as MUITextField} from '@material-ui/core';

export const TextField = ({
                              label,
                              value,
                              variant,
                              placeholder,
                              onChange,
                              size,
                              inputProps}) => {

    const {t} = useTranslation();

    return (
        <MUITextField
            label={t(label)}
            value={value}
            variant={variant}
            placeholder={placeholder}
            onChange={onChange}
            size={size}
            inputProps={inputProps}
        />
    );
};
