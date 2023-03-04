import React, {useState} from "react";
import {DesktopDatePicker} from "@mui/x-date-pickers/DesktopDatePicker";
import {useTranslation} from "react-i18next";

export const DatePicker = ({
    id,
    label,
    value,
    preValidate = null,
    onChange: onChangeParam,
    validators = [],
    inputFormat = 'DD/MM/YYYY',
    renderInput = null,
}) => {
    const {t} = useTranslation();
    const [errorMessages, setErrorMessages] = useState([]);

    const validate = (value) => {
        const errorMessages = [];

        validators.forEach(({isValid, errorMsg}) => {
            if (!isValid(value)) {
                errorMessages.push(t(errorMsg));
            }
        });

        setErrorMessages(errorMessages);
        return errorMessages.length === 0;
    };

    const onChange = (value) => {
        const preValidatedValue = preValidate ? preValidate(value) : value;
        if (validate(preValidatedValue)) {
            onChangeParam(preValidatedValue)
        } else {
            onChangeParam(null);
        }
    };

    return (
        <DesktopDatePicker
            id={id}
            label={t(label)}
            inputFormat={inputFormat}
            value={value}
            onChange={(value) => onChange(value)}
            renderInput={(params) => {
                const mergedProps = {
                    ...params,
                    ...renderInput.props,
                    error: errorMessages.length > 0,
                    helperText: errorMessages.join(', '),
                }
                return React.cloneElement(renderInput, {...mergedProps})
            }}
        />
    );
};
