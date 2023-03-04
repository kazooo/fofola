import {useTranslation} from "react-i18next";
import {Controller} from "react-hook-form";
import {FormControl, FormHelperText, InputLabel, Select} from "@material-ui/core";
import React from "react";

export const FormSelector = ({
    name,
    label,
    control,
    options,
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
            render={({ field: { onChange, ...restProps} }) => (
                <FormControl {...fieldStyle} error={!!error}>
                    <InputLabel>{t(label)}</InputLabel>
                    <Select
                        {...restProps}
                        {...fieldStyle}
                        label={t(label)}
                        onChange={(event) => {
                            onChange(event);
                            const selectedValue = event.target.value;
                            onChangeProp && onChangeProp(selectedValue);
                        }}
                    >
                        {options}
                    </Select>
                    <FormHelperText>
                        {error ? t(error.message) : ''}
                    </FormHelperText>
                </FormControl>
            )}
        />
    );
};
