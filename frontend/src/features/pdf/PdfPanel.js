import {useDispatch, useSelector} from "react-redux";
import {Panel} from "../../components/container/Panel";
import {InlineP} from "../../components/container/InlineP";
import {getUuids} from "./slice";
import {generatePdf} from "./saga";

export const PdfPanel = () => {

    const dispatch = useDispatch();
    const uuids = useSelector(state => getUuids(state));

    const handleOnClick = (e) => {
        e.preventDefault();
        dispatch(generatePdf());
    }

    return uuids.length > 0 &&
        <Panel>
            <button type="submit" onClick={handleOnClick}>Vygenerovat PDF</button>
            <InlineP>Celkem: {uuids.length}</InlineP>
        </Panel>;
};
