import {useDispatch} from "react-redux";
import {Panel} from "../../components/container/Panel";
import {FormWithButton} from "../../components/form/FormWithButton";
import {TextFileReadWithButton} from "../../components/form/TextFileReadWithButton";
import {getUuidInfo} from "./saga";

export const UuidInfoForm = () => {

    const dispatch = useDispatch();

    const loadOneUuid = (uuid) => {
        loadUuids([uuid]);
    }

    const loadUuids = (uuids) => {
        dispatch(getUuidInfo(uuids));
    }

    return <Panel>
        <FormWithButton
            type="text"
            size="33"
            label="Zadejte UUID"
            button="Načíst uuid"
            placeholder="uuid:..."
            submitFunc={loadOneUuid}
        />
        <TextFileReadWithButton
            label="Vyberte soubor s UUID"
            submitFunc={loadUuids}
        />
    </Panel>;
};
