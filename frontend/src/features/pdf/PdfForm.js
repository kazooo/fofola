import {Panel} from "../../components/container/Panel";
import {FormWithButton} from "../../components/form/FormWithButton";
import {FileReadWithButton} from "../../components/form/FileReadWithButton";
import {useDispatch} from "react-redux";
import {setUuids} from "./slice";

export const PdfForm = () => {

    const dispatch = useDispatch();

    const loadOneUuid = (uuid) => {
        loadUuids([uuid]);
    }

    const loadUuids = (uuids) => {
        dispatch(setUuids(uuids));
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
        <FileReadWithButton
            label="Vyberte soubor s UUID"
            submitFunc={loadUuids}
        />
    </Panel>;
};
