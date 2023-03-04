import {Switch} from "@material-ui/core";
import {StyledTooltip} from "../common";
import {useTranslation} from "react-i18next";

export const TooltipedSwitch = ({tooltip, ...props}) => {
    const {t} = useTranslation();
    return (
        <StyledTooltip title={tooltip && t(tooltip)}>
            <Switch
                {...props}
            />
        </StyledTooltip>
    )
}