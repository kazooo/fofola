import {useDispatch, useSelector} from "react-redux";

import {HorizontalDirectedGrid} from "../../components/temporary/HorizontalDirectedGrid";
import {clearUuids, getMode, getUuids, getVcUuid} from "./slice";
import {ClearButton, StartButton} from "../../components/button";
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

    const clear = (e) => {
        e.preventDefault();
        dispatch(clearUuids(uuids));
    }

    return uuids.length > 0 && vcUuid && mode &&
        <HorizontalDirectedGrid
            spacing={10}
        >
            <StartButton onClick={handleOnClick}>Spustit process</StartButton>
            <InlineP>Celkem: {uuids.length}</InlineP>
            <ClearButton onClick={clear}>Vyčistit</ClearButton>
        </HorizontalDirectedGrid>;
};
