import {IconButton} from '@material-ui/core';
import CachedIcon from '@material-ui/icons/Cached';
import LockIcon from '@material-ui/icons/Lock';
import LockOpenIcon from '@material-ui/icons/LockOpen';
import StopIcon from '@material-ui/icons/Stop';
import DeleteOutlineIcon from '@material-ui/icons/DeleteOutline';
import NavigateNextIcon from '@material-ui/icons/NavigateNext';
import NavigateBeforeIcon from '@material-ui/icons/NavigateBefore';
import FirstPageIcon from '@material-ui/icons/FirstPage';
import GetAppIcon from '@material-ui/icons/GetApp';
import DescriptionIcon from '@material-ui/icons/Description';
import CloseIcon from '@material-ui/icons/Close';
import EditIcon from '@mui/icons-material/Edit';
import BoltTwoToneIcon from '@mui/icons-material/BoltTwoTone';
import PlayArrowIcon from '@mui/icons-material/PlayArrow';
import PauseIcon from '@mui/icons-material/Pause';
import InfoIcon from '@mui/icons-material/InfoOutlined';
import {useTranslation} from 'react-i18next';
import {mainColor, StyledTooltip} from '../common';

export const CloseIconButton = ({
    tooltip = 'common.button.close',
    onClick,
}) => (
    <IconButtonWrapper tooltip={tooltip} onClick={onClick}>
        <CloseIcon style={{ color: mainColor }} />
    </IconButtonWrapper>
);

export const RefreshIconButton = (props) => (
    <IconButtonWrapper
        {...props}
    >
        <CachedIcon style={{ color: mainColor }} />
    </IconButtonWrapper>
);

export const LockIconButton = (props) => (
    <IconButtonWrapper
        {...props}
    >
        <LockIcon style={{ color: mainColor }} />
    </IconButtonWrapper>
);

export const UnlockIconButton = (props) => (
    <IconButtonWrapper
        {...props}
    >
        <LockOpenIcon style={{ color: mainColor }} />
    </IconButtonWrapper>
);

export const StopIconButton = (props) => (
    <IconButtonWrapper
        {...props}
    >
        <StopIcon style={{ color: mainColor }} />
    </IconButtonWrapper>
);

export const DeleteIconButton = (props) => (
    <IconButtonWrapper
        {...props}
    >
        <DeleteOutlineIcon style={{ color: mainColor }} />
    </IconButtonWrapper>
);

export const DownloadIconButton = (props) => (
    <IconButtonWrapper
        {...props}
    >
        <GetAppIcon style={{ color: mainColor }} />
    </IconButtonWrapper>
);

export const NextIconButton = (props) => (
    <IconButtonWrapper
        {...props}
    >
        <NavigateNextIcon style={{ color: mainColor }} />
    </IconButtonWrapper>
);

export const PreviousIconButton = (props) => (
    <IconButtonWrapper
        {...props}
    >
        <NavigateBeforeIcon style={{ color: mainColor }} />
    </IconButtonWrapper>
);

export const FirstIconButton = (props) => (
    <IconButtonWrapper
        {...props}
    >
        <FirstPageIcon style={{ color: mainColor }} />
    </IconButtonWrapper>
);

export const LogsIconButton = (props) => (
    <IconButtonWrapper
        {...props}
    >
        <DescriptionIcon style={{ color: mainColor }} />
    </IconButtonWrapper>
);

export const EditIconButton = (props) => (
    <IconButtonWrapper
        {...props}
    >
        <EditIcon style={{ color: mainColor }} />
    </IconButtonWrapper>
);

export const InfoIconButton = (props) => (
    <IconButtonWrapper
        {...props}
    >
        <InfoIcon style={{ color: mainColor }} />
    </IconButtonWrapper>
);

export const TriggerIconButton = (props) => (
    <IconButtonWrapper
        {...props}
    >
        <BoltTwoToneIcon style={{ color: mainColor }} />
    </IconButtonWrapper>
);

export const LaunchIconButton = (props) => (
    <IconButtonWrapper
        {...props}
    >
        <PlayArrowIcon style={{ color: mainColor }} />
    </IconButtonWrapper>
);

export const PauseIconButton = (props) => (
    <IconButtonWrapper
        {...props}
    >
        <PauseIcon style={{ color: mainColor }} />
    </IconButtonWrapper>
);

const IconButtonWrapper = ({tooltip, onClick, children}) => {
    const {t} = useTranslation();

    const button = (
        <IconButton
            size='small'
            onClick={onClick}
        >
            {children}
        </IconButton>
    );

    const tooltipButton = (
        <StyledTooltip title={tooltip && t(tooltip)}>
            {button}
        </StyledTooltip>
    );

    return tooltip ? tooltipButton : button;
};
