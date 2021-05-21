import {Panel} from "../../components/container/Panel";
import {Selector} from "../../components/form/Selector";
import {FormWithButton} from "../../components/form/FormWithButton";
import {FileReadWithButton} from "../../components/form/FileReadWithButton";
import {useDispatch} from "react-redux";
import {setDonator, setMode, setUuids} from "./slice";

export const LinkDonatorForm = () => {

    const dispatch = useDispatch();
    const donators = [
        {
            value: "eodopen",
            text: "EODOPEN"
        },
        {
            value: "norway",
            text: "NORWAY"
        }
    ];
    const modes = [
        {
            value: "link",
            text: "přidat"
        },
        {
            value: "unlink",
            text: "odebrat"
        }
    ];

    const loadOneUuid = (uuid) => {
        loadUuids([uuid]);
    };

    const loadUuids = (uuids) => {
        dispatch(setUuids(uuids));
    };

    const changeDonator = (donator) => {
        dispatch(setDonator(donator));
    };

    const changeMode = (mode) => {
        dispatch(setMode(mode));
    }

    return <Panel>
        <Selector
            label="Název donátoru"
            options={donators}
            onChange={changeDonator}
        />
        <Selector
            label="Režim"
            options={modes}
            onChange={changeMode}
        />
        <FormWithButton
            type="text"
            size="33"
            label="UUID kořene"
            button="Načíst uuid"
            placeholder="uuid:..."
            submitFunc={loadOneUuid}
        />
        <FileReadWithButton
            label="Vyberte soubor s UUID kořenů"
            submitFunc={loadUuids}
        />
    </Panel>
};
