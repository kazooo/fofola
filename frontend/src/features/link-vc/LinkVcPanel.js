import {useDispatch, useSelector} from "react-redux";
import {getMode, getUuids, getVcUuid} from "./slice";
import {Panel} from "../../components/container/Panel";
import {InlineP} from "../../components/container/InlineP";
import {changeVc} from "./saga";

export const LinkVcPanel = () => {

    const dispatch = useDispatch();
    const mode = useSelector(state => getMode(state));
    const uuids = useSelector(state => getUuids(state));
    const vcUuid = useSelector(state => getVcUuid(state));

    const handleOnClick = () => {
        dispatch(changeVc({vcUuid, mode, uuids}));
    }

    return uuids.length > 0 && vcUuid && <Panel>
        <button type="submit" onClick={handleOnClick}>Spustit process</button>
        <InlineP>Celkem: {uuids.length}</InlineP>
    </Panel>
};
