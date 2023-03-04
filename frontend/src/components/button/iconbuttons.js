import {IconButton, Tooltip, withStyles} from "@material-ui/core";
import CachedIcon from '@material-ui/icons/Cached';
import LockIcon from "@material-ui/icons/Lock";
import LockOpenIcon from "@material-ui/icons/LockOpen";
import StopIcon from '@material-ui/icons/Stop';
import DeleteOutlineIcon from "@material-ui/icons/DeleteOutline";
import NavigateNextIcon from '@material-ui/icons/NavigateNext';
import NavigateBeforeIcon from '@material-ui/icons/NavigateBefore';
import FirstPageIcon from '@material-ui/icons/FirstPage';
import GetAppIcon from '@material-ui/icons/GetApp';
import DescriptionIcon from '@material-ui/icons/Description';
import CloseIcon from '@material-ui/icons/Close';
import EditIcon from '@mui/icons-material/Edit';
import {useTranslation} from "react-i18next";
import {StyledTooltip} from "../common";

export const CloseIconButton = (props) => (
    <IconButtonWrapper
        {...props}
    >
        <CloseIcon />
    </IconButtonWrapper>
);

export const RefreshIconButton = (props) => (
    <IconButtonWrapper
        {...props}
    >
        <CachedIcon />
    </IconButtonWrapper>
);

export const LockIconButton = (props) => (
    <IconButtonWrapper
        {...props}
    >
        <LockIcon />
    </IconButtonWrapper>
);

export const UnlockIconButton = (props) => (
    <IconButtonWrapper
        {...props}
    >
        <LockOpenIcon />
    </IconButtonWrapper>
);

export const StopIconButton = (props) => (
    <IconButtonWrapper
        {...props}
    >
        <StopIcon />
    </IconButtonWrapper>
);

export const DeleteIconButton = (props) => (
    <IconButtonWrapper
        {...props}
    >
        <DeleteOutlineIcon />
    </IconButtonWrapper>
);

export const DownloadIconButton = (props) => (
    <IconButtonWrapper
        {...props}
    >
        <GetAppIcon />
    </IconButtonWrapper>
);

export const NextIconButton = (props) => (
    <IconButtonWrapper
        {...props}
    >
        <NavigateNextIcon />
    </IconButtonWrapper>
);

export const PreviousIconButton = (props) => (
    <IconButtonWrapper
        {...props}
    >
        <NavigateBeforeIcon />
    </IconButtonWrapper>
);

export const FirstIconButton = (props) => (
    <IconButtonWrapper
        {...props}
    >
        <FirstPageIcon />
    </IconButtonWrapper>
);

export const LogsIconButton = (props) => (
    <IconButtonWrapper
        {...props}
    >
        <DescriptionIcon />
    </IconButtonWrapper>
);

export const EditIconButton = (props) => (
    <IconButtonWrapper
        {...props}
    >
        <EditIcon />
    </IconButtonWrapper>
);

const IconButtonWrapper = ({tooltip, onClick, children}) => {
    const {t} = useTranslation();
    return (
        <StyledTooltip title={tooltip && t(tooltip)}>
            <IconButton
                size="small"
                onClick={onClick}
            >
                {children}
            </IconButton>
        </StyledTooltip>
    );
};
