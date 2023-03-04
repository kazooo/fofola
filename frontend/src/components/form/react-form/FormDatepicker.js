import {useTranslation} from "react-i18next";
import {Controller} from "react-hook-form";
import {DesktopDatePicker} from "@mui/x-date-pickers/DesktopDatePicker";
import {TextField} from "@material-ui/core";

export const FormDatepicker = ({
    name,
    label,
    defaultValue = null,
    format = 'DD/MM/YYYY',
    control,
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
            render={({ field: { onChange, ...restProps } }) => {
                return (
                    <DesktopDatePicker
                        {...restProps}
                        {...fieldStyle}
                        label={t(label)}
                        inputFormat={format}
                        renderInput={(params) =>
                            <TextField
                                {...{...params, ...fieldStyle}}
                                error={!!error}
                                helperText={error ? t(error.message) : ''}
                            />
                        }
                        onChange={(selectedValue) => {
                            onChange(selectedValue);
                            onChangeProp && onChangeProp(selectedValue);
                        }}
                    />
                )
            }}
        />
    );
};
