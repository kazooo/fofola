import {useDispatch} from "react-redux";
import {useState} from "react";
import {Panel} from "../../components/container/Panel";
import {TextForm} from "../../components/form/TextForm";
import {Selector} from "../../components/form/Selector";
import {IMG_FULL, IMG_THUMB} from "./constants";
import {Button} from "../../components/button";
import {FileUploadWithButton} from "../../components/form/FileUploadWithButton";

export const SetImageForm = () => {

    const dispatch = useDispatch();
    const [uuid, setUuid] = useState("");
    const [datastream, setDatastream] = useState();
    const [imgPath, setImgPath] = useState();

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

    return <Panel>
        <TextForm
            label="UUID stránky"
            size="33"
            placeholder="uuid:..."
            onChange={setUuid}
        />
        <Selector
            label="Model"
            options={datastreams}
            onChange={setDatastream}
        />
        <FileUploadWithButton
            label="Vyberte obrázek"
            submitFunc={}
        />
    </Panel>
};
