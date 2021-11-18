import {IconButton} from "@material-ui/core";
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

export const RefreshIconButton = ({onClick}) => (
    <IconButton
        size="small"
        onClick={onClick}
    >
        <CachedIcon />
    </IconButton>
);

export const LockIconButton = ({onClick}) => (
    <IconButton
        size="small"
        onClick={onClick}
    >
        <LockIcon />
    </IconButton>
);

export const UnlockIconButton = ({onClick}) => (
    <IconButton
        size="small"
        onClick={onClick}
    >
        <LockOpenIcon />
    </IconButton>
);

export const StopIconButton = ({onClick}) => (
    <IconButton
        size="small"
        onClick={onClick}
    >
        <StopIcon />
    </IconButton>
);

export const DeleteIconButton = ({onClick}) => (
    <IconButton
        size="small"
        onClick={onClick}
    >
        <DeleteOutlineIcon />
    </IconButton>
);

export const DownloadIconButton = ({onClick}) => (
    <IconButton
        size="small"
        onClick={onClick}
    >
        <GetAppIcon />
    </IconButton>
);

export const NextIconButton = ({onClick}) => (
    <IconButton
        size="small"
        onClick={onClick}
    >
        <NavigateNextIcon />
    </IconButton>
);

export const PreviousIconButton = ({onClick}) => (
    <IconButton
        size="small"
        onClick={onClick}
    >
        <NavigateBeforeIcon />
    </IconButton>
);

export const FirstIconButton = ({onClick}) => (
    <IconButton
        size="small"
        onClick={onClick}
    >
        <FirstPageIcon />
    </IconButton>
);

export const LogsIconButton = ({onClick}) => (
    <IconButton
        size="small"
        onClick={onClick}
    >
        <DescriptionIcon />
    </IconButton>
);
