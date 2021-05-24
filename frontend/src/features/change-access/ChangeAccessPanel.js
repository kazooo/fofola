import {useDispatch, useSelector} from "react-redux";
import {Panel} from "../../components/container/Panel";
import {InlineP} from "../../components/container/InlineP";
import {clearUuids, getUuids} from "./slice";
import {privateUuids, publicUuids} from "./saga";

export const ChangeAccessPanel = () => {

    const dispatch = useDispatch();
    const uuids = useSelector(state => getUuids(state));

    const handlePublic = (e) => {
        e.preventDefault();
        dispatch(publicUuids(uuids));
    }

    const handlePrivate = (e) => {
        e.preventDefault();
        dispatch(privateUuids(uuids));
    }

    const handleClear = (e) => {
        e.preventDefault();
        dispatch(clearUuids());
    }

    return uuids.length > 0 &&
        <Panel>
            <button type="submit" onClick={handlePublic}>Zveřejnit</button>
            <button type="submit" onClick={handlePrivate}>Zneveřejnit</button>
            <button type="submit" onClick={handleClear}>Vyčistit</button>
            <InlineP>Celkem: {uuids.length}</InlineP>
        </Panel>;
};
