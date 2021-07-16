import {useDispatch, useSelector} from "react-redux";
import {datastreams} from "./constants";
import {getDatastream, isPayloadCompleted, setDatastream, setImg, setUuid} from "./slice";
import {LoadUuidWithSelectorForm} from "../../components/temporary/LoadUuidWithSelectorForm";
import {UploadButton} from "../../components/button";
import {HorizontalDirectedGrid} from "../../components/temporary/HorizontalDirectedGrid";

export const SetImageForm = () => {

    const dispatch = useDispatch();
    const completed = useSelector(isPayloadCompleted);
    const datastream = useSelector(state => getDatastream(state));

    const loadUuid = uuid => {
        dispatch(setUuid(uuid));
    }

    const changeDatastream = ds => {
        dispatch(setDatastream(ds));
    }

    const loadImg = e => {
        const fileUploaded = e.target.files[0];
        dispatch(setImg(fileUploaded));
    }

    return <HorizontalDirectedGrid>
        <LoadUuidWithSelectorForm
            onLoad={loadUuid}
            selectOptions={datastreams}
            selectLabel={"Režim"}
            onSelectOptionChange={changeDatastream}
            selectedOption={datastream}
        />
        {!completed &&
            <UploadButton onChange={loadImg}>
                Nahrat obrázek
                <input type="file" hidden />
            </UploadButton>
        }
    </HorizontalDirectedGrid>
};
