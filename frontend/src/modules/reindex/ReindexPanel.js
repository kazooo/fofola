import {InlineP} from "../../components/container/InlineP";
import {useDispatch, useSelector} from "react-redux";
import {clearUuids, getUuids} from "./slice";
import {reindexUuids} from "./saga";
import {ClearButton, StartButton} from "../../components/button";
import {HorizontalDirectedGrid} from "../../components/temporary/HorizontalDirectedGrid";

export const ReindexPanel = () => {

    const dispatch = useDispatch();
    const uuids = useSelector(state => getUuids(state));

    const reindex = (e) => {
        e.preventDefault();
        dispatch(reindexUuids(uuids));
    }

    const clear = (e) => {
        e.preventDefault();
        dispatch(clearUuids());
    }

    return uuids.length > 0 &&
        <HorizontalDirectedGrid
            spacing={10}
        >
            <StartButton onClick={reindex}>Reindexovat</StartButton>
            <InlineP>Celkem: {uuids.length}</InlineP>
            <ClearButton onClick={clear}>Vyčistit</ClearButton>
        </HorizontalDirectedGrid>;
}
