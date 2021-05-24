import {useDispatch} from "react-redux";
import {Panel} from "../../components/container/Panel";
import {FormWithButton} from "../../components/form/FormWithButton";
import {FileReadWithButton} from "../../components/form/FileReadWithButton";
import {Selector} from "../../components/form/Selector";
import {setMode, setUuids, setVcUuid} from "./slice";
import {LINK_MODE, UNLINK_MODE} from "./constants";

export const LinkVcForm = () => {

    const dispatch = useDispatch();
    const modes = [
        {
            value: LINK_MODE,
            text: "přidat"
        },
        {
            value: UNLINK_MODE,
            text: "odebrat"
        }
    ];

    const loadVcUuid = uuid => {
        dispatch(setVcUuid(uuid));
    }

    const loadOneUuid = uuid => {
        dispatch(setUuids([uuid]));
    }

    const loadUuids = uuids => {
        dispatch(setUuids(uuids));
    }

    const changeMode = mode => {
        dispatch(setMode(mode));
    }

    return <Panel>
        <label style={{margin: "5px 10px 5px 0"}}>UUID vurtuální sbírky</label>
        <input
            type="text"
            size="33"
            placeholder="uuid:..."
            onChange={e => loadVcUuid(e.target.value)}
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
