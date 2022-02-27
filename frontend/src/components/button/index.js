import privateImg from "../../img/private.png";
import {Button} from "@material-ui/core";
import PlayArrowIcon from '@material-ui/icons/PlayArrow';
import ClearIcon from '@material-ui/icons/Clear';
import LockOpenIcon from '@material-ui/icons/LockOpen';
import LockIcon from '@material-ui/icons/Lock';
import DeleteOutlineIcon from '@material-ui/icons/DeleteOutline';
import PublishIcon from '@material-ui/icons/Publish';
import AddIcon from '@material-ui/icons/Add';
import SearchIcon from '@material-ui/icons/Search';

export const StartButton = ({children, onClick}) => (
    <IconButton
        icon={<PlayArrowIcon />}
        onClick={onClick}
    >
        {children}
    </IconButton>
);

export const ClearButton = ({children, onClick}) => (
    <IconButton
        icon={<ClearIcon />}
        onClick={onClick}
    >
        {children}
    </IconButton>
);

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

export const DeleteButton = ({children, onClick}) => (
    <IconButton
        icon={<DeleteOutlineIcon />}
        onClick={onClick}
    >
        {children}
    </IconButton>
);

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

export const AddButton = ({children, onClick}) => (
    <IconButton
        icon={<AddIcon />}
        onClick={onClick}
    >
        {children}
    </IconButton>
);

export const SearchButton = ({children, onClick}) => (
    <IconButton
        icon={<SearchIcon />}
        onClick={onClick}
    >
        {children}
    </IconButton>
);

const IconButton = ({children, icon, onClick, onChange = null, type = "input"}) => (
    <Button
        variant="contained"
        component="label"
        onClick={onClick}
        onChange={onChange}
        type={type}
        style={{ fontSize: '12px' }}
        endIcon={icon}
    >
        {children}
    </Button>
)

const ImgButton = ({title, img, onClick}) => {

    const handleOnClick = (e) => {
        e.preventDefault();
        onClick();
    }

    return <button onClick={handleOnClick} title={title} style={{
        background: {img},
        width: "20px",
        height: "20px",
        border: "0",
        backgroundSize: "100%",
        marginLeft: "5px",
        marginRight: "5px",
    }}/>;
};

export const MakePrivateButton = ({onClick}) => (
    <ImgButton title={""} img={privateImg} onClick={onClick} />
);
