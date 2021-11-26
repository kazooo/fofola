import React, {useState} from "react";
import {useDispatch, useSelector} from "react-redux";
import {Box, TextField} from "@material-ui/core";
import Autocomplete from "@material-ui/lab/Autocomplete";

import {HorizontallyCenteredBox} from "../../components/layout/HorizontallyCenteredBox";
import {Buttons, styles, VCDescriptions, VCNames} from "./components";
import {Error} from "../../components/info/Error";
import {getIsLoadingError, getVcs} from "./slice";
import {updateVirtualCollection} from "./saga";
import {AddButton} from "../../components/button";

export const EditForm = () => {
    const dispatch = useDispatch();
    const [uuid, setUuid] = useState(null);
    const [nameCz, setNameCz] = useState('');
    const [nameEn, setNameEn] = useState('');
    const [descriptionCz, setDescriptionCz] = useState('');
    const [descriptionEn, setDescriptionEn] = useState('');
    const [fullImg, setFullImg] = useState(null);
    const [thumbImg, setThumbImg] = useState(null);
    const vcs = useSelector(state => getVcs(state));

    const isLoadingError = useSelector(state => getIsLoadingError(state));

    const updateVc = () => {
        dispatch(updateVirtualCollection({uuid, nameCz, nameEn, descriptionCz, descriptionEn, fullImg, thumbImg}));
    }

    const loadVcUuid = (event, values) => {
        if (values) {
            setNameCz(values.nameCz);
            setNameEn(values.nameEn);
            setDescriptionCz(values.descriptionCz);
            setDescriptionEn(values.descriptionEn);
        }
        setUuid(values ? values.uuid : '')
    }

    const handleClear = () => {
        setNameCz('');
        setNameEn('');
        setDescriptionCz('');
        setDescriptionEn('');
        setFullImg(null);
        setThumbImg(null);
    }

    const buttonFuncs = {
        actionButton: <AddButton onClick={updateVc}>Upravit</AddButton>,
        updateVc,
        handleClear,
        setFullImg,
        setThumbImg,
    }

    const content = (
        <Box>
            <Box style={styles.wrapperStyle}>
                <HorizontallyCenteredBox width={'50%'}>
                    <Autocomplete
                        options={vcs}
                        getOptionLabel={(option) => option.nameCz}
                        onChange={loadVcUuid}
                        renderInput={(params) =>
                            <TextField
                                {...params}
                                label="Název virtuální sbirky"
                                variant="outlined"
                                size="small"
                            />
                        }
                    />
                </HorizontallyCenteredBox>
            </Box>

            <VCNames
                nameCz={nameCz}
                nameEn={nameEn}
                setNameCz={setNameCz}
                setNameEn={setNameEn}
            />

            <VCDescriptions
                textCz={descriptionCz}
                textEn={descriptionEn}
                setTextCz={setDescriptionCz}
                setTextEn={setDescriptionEn}
            />

            <Buttons
                anyContent={nameCz || nameEn || descriptionCz || descriptionEn || fullImg || thumbImg}
                ready={nameCz && nameEn && descriptionCz && descriptionEn}
                fullImg={fullImg}
                thumbImg={thumbImg}
                {...buttonFuncs}
            />
        </Box>
    );

    const error = (
        <HorizontallyCenteredBox>
             <Box mt={"20%"}>
                 <Error label={'Chyba při načtení virtuálních sbírek!'} />
             </Box>
        </HorizontallyCenteredBox>
    );

    return (
      <Box height={"100%"}>
          {
              isLoadingError ? error : content
          }
      </Box>
    );
};
