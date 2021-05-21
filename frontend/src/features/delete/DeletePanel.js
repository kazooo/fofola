import {useDispatch, useSelector} from "react-redux";
import {Panel} from "../../components/container/Panel";
import {InlineP} from "../../components/container/InlineP";
import {getUuids} from "./slice";
import {deleteUuids} from "./saga";

export const DeletePanel = () => {

    const dispatch = useDispatch();
    const uuids = useSelector(state => getUuids(state));

    const handleOnClick = (e) => {
        e.preventDefault();
        dispatch(deleteUuids(uuids));
    }

    return uuids.length > 0 &&
        <Panel>
            <button type="submit" onClick={handleOnClick}>Vymazat</button>
            <InlineP>Celkem: {uuids.length}</InlineP>
        </Panel>;
}