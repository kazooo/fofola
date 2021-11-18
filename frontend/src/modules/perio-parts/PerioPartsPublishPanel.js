import {useDispatch, useSelector} from "react-redux";
import {InlineP} from "../../components/page/InlineP";
import {clearUuids, getUuids} from "./slice";
import {publishPerioParts} from "./saga";
import {HorizontalDirectedGrid} from "../../components/layout/HorizontalDirectedGrid";
import {ClearButton, StartButton} from "../../components/button";

export const PerioPartsPublishPanel = () => {

    const dispatch = useDispatch();
    const uuids = useSelector(state => getUuids(state));

    const publish = (e) => {
        e.preventDefault();
        dispatch(publishPerioParts({"root_uuids": uuids}));
    };

    const clear = (e) => {
        e.preventDefault();
        dispatch(clearUuids());
    };

    return uuids.length > 0 &&
        <HorizontalDirectedGrid
            spacing={10}
        >
            <StartButton onClick={publish}>Spustit process</StartButton>
            <InlineP>Celkem: {uuids.length}</InlineP>
            <ClearButton onClick={clear}>Vyčistit</ClearButton>
        </HorizontalDirectedGrid>;
};
