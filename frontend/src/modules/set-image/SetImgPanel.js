import {useDispatch, useSelector} from "react-redux";
import {clearAll, getPayload, isPayloadCompleted} from "./slice";
import {setImg} from "./saga";
import {HorizontalDirectedGrid} from "../../components/layout/HorizontalDirectedGrid";
import {ClearButton, StartButton} from "../../components/button";

export const SetImgPanel = () => {

    const dispatch = useDispatch();
    const payload = useSelector(getPayload);
    const completed = useSelector(isPayloadCompleted);

    const sendPayload = () => {
        dispatch(setImg(payload));
    }

    const clear = (e) => {
        e.preventDefault();
        dispatch(clearAll());
    }

    return completed &&
        <HorizontalDirectedGrid
            spacing={10}
        >
            <StartButton onClick={sendPayload}>Změnit obrázek</StartButton>
            <ClearButton onClick={clear}>Vyčistit</ClearButton>
        </HorizontalDirectedGrid>;
}
