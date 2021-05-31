import {useDispatch, useSelector} from "react-redux";
import {Panel} from "../../components/container/Panel";
import {InlineP} from "../../components/container/InlineP";
import {getUuids} from "./slice";
import {publishPerioParts} from "./saga";

export const PerioPartsPublishPanel = () => {

    const dispatch = useDispatch();
    const uuids = useSelector(state => getUuids(state));

    const handleOnClick = (e) => {
        e.preventDefault();
        dispatch(publishPerioParts({"root_uuids": uuids}));
    }

    return uuids.length > 0 &&
        <Panel>
            <button type="submit" onClick={handleOnClick}>Zveřejnit části periodik</button>
            <InlineP>Celkem: {uuids.length}</InlineP>
        </Panel>;
};
