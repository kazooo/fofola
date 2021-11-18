import {useDispatch, useSelector} from "react-redux";
import {InlineP} from "../../components/page/InlineP";
import {clearUuids, getUuids} from "./slice";
import {generatePdf} from "./saga";
import {HorizontalDirectedGrid} from "../../components/layout/HorizontalDirectedGrid";
import {ClearButton, StartButton} from "../../components/button";

export const PdfPanel = () => {

    const dispatch = useDispatch();
    const uuids = useSelector(state => getUuids(state));

    const generate = (e) => {
        e.preventDefault();
        dispatch(generatePdf());
    }

    const clear = (e) => {
        e.preventDefault();
        dispatch(clearUuids());
    }

    return uuids.length > 0 &&
        <HorizontalDirectedGrid
            spacing={10}
        >
            <StartButton onClick={generate}>Vygenerovat PDF</StartButton>
            <InlineP>Celkem: {uuids.length}</InlineP>
            <ClearButton onClick={clear}>Vyčistit</ClearButton>
        </HorizontalDirectedGrid>;
};
