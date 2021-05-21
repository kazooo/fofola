import {Panel} from "../../components/container/Panel";
import {InlineP} from "../../components/container/InlineP";
import {useDispatch, useSelector} from "react-redux";
import {getUuids} from "./slice";
import {reindexUuids} from "./saga";

export const ReindexPanel = () => {

    const dispatch = useDispatch();
    const uuids = useSelector(state => getUuids(state));

    const handleOnClick = (e) => {
        e.preventDefault();
        dispatch(reindexUuids(uuids));
    }

    return uuids.length > 0 &&
        <Panel>
            <button type="submit" onClick={handleOnClick}>Reindexovat</button>
            <InlineP>Celkem: {uuids.length}</InlineP>
        </Panel>;
}
