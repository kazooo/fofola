import {useDispatch} from "react-redux";
import {Panel} from "../../components/container/Panel";
import {TextForm} from "../../components/form/TextForm";
import {Selector} from "../../components/form/Selector";
import {IMG_FULL, IMG_THUMB} from "./constants";
import {FileUploadWithButton} from "../../components/form/FileUploadWithButton";
import {setDatastream, setImg, setUuid} from "./slice";

export const SetImageForm = () => {

    const dispatch = useDispatch();

    const datastreams = [
        {
            value: IMG_THUMB,
            text: "THUMBNAIL"
        },
        {
            value: IMG_FULL,
            text: "FULL"
        }
    ]

    const loadUuid = uuid => {
        dispatch(setUuid(uuid));
    }

    const loadDatastream = datastream => {
        dispatch(setDatastream(datastream));
    }

    const loadImg = img => {
        dispatch(setImg(img));
    }

    return <Panel>
        <TextForm
            label="UUID stránky"
            size="33"
            placeholder="uuid:..."
            onChange={loadUuid}
        />
        <Selector
            label="Datastream"
            options={datastreams}
            onChange={loadDatastream}
        />
        <FileUploadWithButton
            label="Vyberte obrázek"
            acceptTypes={'.jpg'}
            submitFunc={loadImg}
        />
    </Panel>
};
