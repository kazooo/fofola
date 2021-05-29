import {CHECK_HAS_DONATOR, CHECK_HASNT_DONATOR, EODOPEN, NORWAY} from "./constants";
import {TextForm} from "../../components/form/TextForm";
import {Selector} from "../../components/form/Selector";
import {useDispatch} from "react-redux";
import {useState} from "react";
import {Button} from "../../components/button";
import {checkDonator} from "./saga";

export const Header = () => {

    const dispatch = useDispatch();
    const [vcUuid, setVcUuid] = useState("");
    const [donator, setDonator] = useState(EODOPEN);
    const [mode, setMode] = useState(CHECK_HAS_DONATOR);

    const donators = [
        {
            value: EODOPEN,
            text: "EODOPEN"
        },
        {
            value: NORWAY,
            text: "NORWAY"
        }
    ];
    const modes = [
        {
            value: CHECK_HAS_DONATOR,
            text: "má donátora"
        },
        {
            value: CHECK_HASNT_DONATOR,
            text: "nemá donátora"
        }
    ];

    const handleOnClick = () => {
        if (vcUuid) {
            dispatch(checkDonator({vcUuid, donator, mode}));
        }
    }

    return <div>
        <TextForm
            label="Virtuální sbírka"
            size="33"
            placeholder="uuid:..."
            onChange={setVcUuid}
        />
        <Selector
            label="Název donátoru"
            options={donators}
            onChange={setDonator}
        />
        <Selector
            label="Zkontrolovat"
            options={modes}
            onChange={setMode}
        />
        <Button
            label="Začít kontrolu"
            onClick={handleOnClick}
        />
    </div>
};
