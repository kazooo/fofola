import {Controller} from "react-hook-form";
import {useTranslation} from "react-i18next";
import {InputAdornment, TextField} from "@material-ui/core";

export const FormDurationField = ({
    name,
    label,
    placeholder,
    control,
    defaultValue = '',
    rules = {},
    fieldStyle = {},
    error,
    onChange: onChangeProp = null,
}) => {
    const {t} = useTranslation();
    return (
        <Controller
            name={name}
            control={control}
            defaultValue={defaultValue}
            rules={rules}
            render={({ field: { onChange, ...restProps } }) => (
                <TextField
                    {...restProps}
                    {...fieldStyle}
                    label={t(label)}
                    type={'number'}
                    placeholder={placeholder && t(placeholder)}
                    error={!!error}
                    helperText={error ? t(error.message) : ''}
                    onChange={(event) => {
                        onChange(event);
                        const selectedValue = event.target.value;
                        onChangeProp && onChangeProp(selectedValue);
                    }}
                    InputProps={{
                        endAdornment: <InputAdornment position="end">min</InputAdornment>,
                    }}
                />
            )}
        />
    );
};
