import {useDispatch, useSelector} from "react-redux";
import {InlineP} from "../../components/page/InlineP";
import {clearUuids, getDonator, getMode, getUuids} from "./slice";
import {changeDonator} from "./saga";
import {HorizontalDirectedGrid} from "../../components/layout/HorizontalDirectedGrid";
import {ClearButton, StartButton} from "../../components/button";

export const LinkDonatorPanel = () => {

    const dispatch = useDispatch();
    const mode = useSelector(state => getMode(state));
    const uuids = useSelector(state => getUuids(state));
    const donator = useSelector(state => getDonator(state));

    const handleOnClick = (e) => {
        e.preventDefault();
        dispatch(changeDonator({donator, mode, uuids}));
    }

    const clear = (e) => {
        e.preventDefault();
        dispatch(clearUuids(uuids));
    }

    return uuids.length > 0 && mode && donator &&
        <HorizontalDirectedGrid
            spacing={10}
        >
            <StartButton onClick={handleOnClick}>Spustit process</StartButton>
            <InlineP>Celkem: {uuids.length}</InlineP>
            <ClearButton onClick={clear}>Vyčistit</ClearButton>
        </HorizontalDirectedGrid>;
};
