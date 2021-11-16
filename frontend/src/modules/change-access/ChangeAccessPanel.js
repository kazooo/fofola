import {useDispatch, useSelector} from "react-redux";
import {InlineP} from "../../components/container/InlineP";
import {clearUuids, getUuids} from "./slice";
import {privateUuids, publicUuids} from "./saga";
import {HorizontalDirectedGrid} from "../../components/temporary/HorizontalDirectedGrid";
import {ClearButton, LockButton, UnlockButton} from "../../components/button";

export const ChangeAccessPanel = () => {

    const dispatch = useDispatch();
    const uuids = useSelector(state => getUuids(state));

    const makePublic = (e) => {
        e.preventDefault();
        dispatch(publicUuids(uuids));
    }

    const makePrivate = (e) => {
        e.preventDefault();
        dispatch(privateUuids(uuids));
    }

    const clear = (e) => {
        e.preventDefault();
        dispatch(clearUuids());
    }

    return uuids.length > 0 &&
        <HorizontalDirectedGrid
            spacing={3}
        >
            <UnlockButton onClick={makePublic}>Zveřejnit</UnlockButton>
            <LockButton onClick={makePrivate}>Zneveřejnit</LockButton>
            <ClearButton onClick={clear}>Vyčistit</ClearButton>
            <InlineP>Celkem: {uuids.length}</InlineP>
        </HorizontalDirectedGrid>;
};
