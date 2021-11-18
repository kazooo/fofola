import {useDispatch, useSelector} from "react-redux";
import {InlineP} from "../../components/page/InlineP";
import {clearUuids, getUuids} from "./slice";
import {deleteUuids} from "./saga";
import {HorizontalDirectedGrid} from "../../components/layout/HorizontalDirectedGrid";
import {ClearButton, DeleteButton} from "../../components/button";

export const DeletePanel = () => {

    const dispatch = useDispatch();
    const uuids = useSelector(state => getUuids(state));

    const handleOnClick = (e) => {
        e.preventDefault();
        dispatch(deleteUuids(uuids));
    }

    const clear = (e) => {
        e.preventDefault();
        dispatch(clearUuids(uuids));
    }

    return uuids.length > 0 &&
        <HorizontalDirectedGrid
            spacing={10}
        >
            <DeleteButton onClick={handleOnClick}>Vymazat</DeleteButton>
            <InlineP>Celkem: {uuids.length}</InlineP>
            <ClearButton onClick={clear}>Vyčistit</ClearButton>
        </HorizontalDirectedGrid>;
}
