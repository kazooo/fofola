import privateImg from '../../img/private.png';
import {Button, withStyles} from '@material-ui/core';
import PlayArrowIcon from '@material-ui/icons/PlayArrow';
import ClearIcon from '@material-ui/icons/Clear';
import LockOpenIcon from '@material-ui/icons/LockOpen';
import LockIcon from '@material-ui/icons/Lock';
import DeleteOutlineIcon from '@material-ui/icons/DeleteOutline';
import PublishIcon from '@material-ui/icons/Publish';
import AddIcon from '@material-ui/icons/Add';
import SearchIcon from '@material-ui/icons/Search';
import {useTranslation} from 'react-i18next';
import CachedIcon from '@material-ui/icons/Cached';
import DoneIcon from '@material-ui/icons/Done';
import {mainColor, StyledTooltip} from "components/common";

export const DoneButton = ({
    onClick,
    tooltip,
    disabled = false,
    label = 'common.button.done',
    variant = 'contained',
}) => {
    const {t} = useTranslation();

    return (
        <IconButton icon={<DoneIcon/>} onClick={onClick} tooltip={tooltip} disabled={disabled} variant={variant}>
            {t(label)}
        </IconButton>
    );
}

export const StartButton = ({onClick, label = 'common.button.create'}) => {
    const {t} = useTranslation();

    return (
        <IconButton
            icon={<PlayArrowIcon/>}
            onClick={onClick}
        >
            {t(label)}
        </IconButton>
    );
};

export const ClearButton = ({onClick, label = 'common.button.clear'}) => {
    const {t} = useTranslation();

    return (
        <IconButton
            icon={<ClearIcon />}
            onClick={onClick}
        >
            {t(label)}
        </IconButton>
    );
};

export const LockButton = ({children, onClick}) => (
    <IconButton
        icon={<LockIcon />}
        onClick={onClick}
    >
        {children}
    </IconButton>
);

export const UnlockButton = ({children, onClick}) => (
    <IconButton
        icon={<LockOpenIcon />}
        onClick={onClick}
    >
        {children}
    </IconButton>
);

export const DeleteButton = ({onClick, label = 'common.button.delete'}) => {
    const {t} = useTranslation();

    return (
        <IconButton
            icon={<DeleteOutlineIcon/>}
            onClick={onClick}
        >
            {t(label)}
        </IconButton>
    );
}

export const UploadButton = ({children, onClick, onChange = null, type = null}) => (
    <IconButton
        icon={<PublishIcon />}
        onClick={onClick}
        onChange={onChange}
        type={type}
    >
        {children}
    </IconButton>
);

export const AddButton = ({onClick, label = 'common.button.add'}) => {
    const {t} = useTranslation();

    return (
        <IconButton
            icon={<AddIcon/>}
            onClick={onClick}
        >
            {t(label)}
        </IconButton>
    );
};

export const SearchButton = ({children, onClick}) => (
    <IconButton
        icon={<SearchIcon />}
        onClick={onClick}
    >
        {children}
    </IconButton>
);

export const RefreshButton = ({onClick, label = 'common.button.refresh'}) => {
    const {t} = useTranslation();

    return (
        <IconButton
            icon={<CachedIcon/>}
            onClick={onClick}
        >
            {t(label)}
        </IconButton>
    );
};

export const IconButton = ({
    children,
    icon,
    tooltip,
    onClick,
    onChange = null,
    type = 'input',
    disabled = false,
    variant = 'contained',
}) => {
    const {t} = useTranslation();

    const StyledButton = withStyles({
        root: {
            "&.Mui-disabled": {
                pointerEvents: "auto"
            }
        }
    })(Button);

    const button = (
        <StyledButton
            variant={variant}
            component={'label'}
            size={'small'}
            onClick={disabled ? undefined : onClick}
            onChange={onChange}
            type={type}
            style={{
                fontSize: '15px',
                borderColor: mainColor,
                textTransform: 'none',
            }}
            disabled={disabled}
            endIcon={icon}
        >
            {children}
        </StyledButton>
    );

    const tooltipButton = (
        <StyledTooltip title={tooltip && t(tooltip)}>
            {button}
        </StyledTooltip>
    );

    return tooltip ? tooltipButton : button;
}

const ImgButton = ({title, img, onClick}) => {

    const handleOnClick = (e) => {
        e.preventDefault();
        onClick();
    }

    return <button onClick={handleOnClick} title={title} style={{
        background: {img},
        width: '20px',
        height: '20px',
        border: '0',
        backgroundSize: '100%',
        marginLeft: '5px',
        marginRight: '5px',
    }}/>;
};

export const MakePrivateButton = ({onClick}) => (
    <ImgButton title={''} img={privateImg} onClick={onClick} />
);
