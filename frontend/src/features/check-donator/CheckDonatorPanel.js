import {useDispatch, useSelector} from "react-redux";
import {clearSettings, getDonator, getMode, getVcUuid} from "./slice";
import {HorizontalDirectedGrid} from "../../components/temporary/HorizontalDirectedGrid";
import {ClearButton, StartButton} from "../../components/button";
import {checkDonator} from "./saga";

export const CheckDonatorPanel = () => {

    const dispatch = useDispatch();
    const mode = useSelector(state => getMode(state));
    const vcUuid = useSelector(state => getVcUuid(state));
    const donator = useSelector(state => getDonator(state));

    const check = (e) => {
        e.preventDefault();
        dispatch(checkDonator({mode, vcUuid, donator}));
    }

    const clear = (e) => {
        e.preventDefault();
        dispatch(clearSettings());
    }

    return mode && vcUuid && donator &&
        <HorizontalDirectedGrid
            spacing={10}
        >
            <StartButton onClick={check}>Zkontrolovat</StartButton>
            <ClearButton onClick={clear}>Vyčistit nastavení</ClearButton>
        </HorizontalDirectedGrid>;
};
