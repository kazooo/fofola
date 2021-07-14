import {useDispatch, useSelector} from "react-redux";
import {getPayload, isPayloadCompleted} from "./slice";
import {Panel} from "../../components/container/Panel";
import {Button} from "../../components/button";
import {setImg} from "./saga";

export const SetImgPanel = () => {

    const dispatch = useDispatch();
    const payload = useSelector(getPayload);
    const completed = useSelector(isPayloadCompleted);

    const sendPayload = () => {
        dispatch(setImg(payload));
    }

    return completed &&
        <Panel>
            <Button label={'Změnit obrázek'} onClick={sendPayload} />
        </Panel>;
}
