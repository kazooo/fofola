import privateImg from "../../img/private.png";
import reindexImg from "../../img/reindex.png";
import publicImg from "../../img/public.png";
import removeImg from "../../img/remove.png";
import stopImg from "../../img/stop.png";

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

export const MakePublicButton = ({onClick}) => (
    <ImgButton title={""} img={publicImg} onClick={onClick} />
);

export const ReindexButton = ({onClick}) => (
    <ImgButton title={""} img={reindexImg} onClick={onClick} />
);

export const RemoveButton = ({onClick}) => (
    <ImgButton title={""} img={removeImg} onClick={onClick} />
);

export const StopButton = ({onClick}) => (
    <ImgButton title={""} img={stopImg} onClick={onClick} />
);
