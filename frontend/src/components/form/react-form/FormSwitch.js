import {FormControlLabel, Switch} from "@material-ui/core";
import React from "react";
import {useTranslation} from "react-i18next";
import {Controller} from "react-hook-form";

export const FormSwitch = ({
    name,
    label,
    control,
    rules = {},
    fieldStyle = {},
    onChange: onChangeProp = null,
}) => {
    const {t} = useTranslation();
    return (
        <Controller
            name={name}
            control={control}
            rules={rules}
            render={({ field: { value, onChange, ...restProp } }) => {
                return (
                    <FormControlLabel
                        control={
                            <Switch
                                {...restProp}
                                {...fieldStyle}
                                checked={value}
                                onChange={(event) => {
                                    onChange(event);
                                    const checked = event.target.checked;
                                    onChangeProp && onChangeProp(checked);
                                }}
                            />
                        }
                        label={t(label)}
                    />
                )
            }}
        />
    );
};
