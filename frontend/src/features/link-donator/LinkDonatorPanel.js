import {useDispatch, useSelector} from "react-redux";
import {Panel} from "../../components/container/Panel";
import {InlineP} from "../../components/container/InlineP";
import {getDonator, getMode, getUuids} from "./slice";
import {changeDonator} from "./saga";

export const LinkDonatorPanel = () => {

    const dispatch = useDispatch();
    const mode = useSelector(state => getMode(state));
    const uuids = useSelector(state => getUuids(state));
    const donator = useSelector(state => getDonator(state));

    const handleOnClick = (e) => {
        e.preventDefault();
        dispatch(changeDonator({donator, mode, uuids}));
    }

    return uuids.length > 0 &&
        <Panel>
            <button type="submit" onClick={handleOnClick}>Spustit process</button>
            <InlineP>Celkem: {uuids.length}</InlineP>
        </Panel>;
};
