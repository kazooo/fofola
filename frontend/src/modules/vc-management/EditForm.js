import React, {useState} from "react";
import {useDispatch, useSelector} from "react-redux";
import {Box, TextField} from "@material-ui/core";
import Autocomplete from "@material-ui/lab/Autocomplete";

import {HorizontallyCenteredBox} from "../../components/layout/HorizontallyCenteredBox";
import {deleteVirtualCollection, loadVirtualCollections, updateVirtualCollection} from "./saga";
import {RefreshIconButton} from "../../components/button/iconbuttons";
import {Buttons, Panel, VCDescriptions, VCNames} from "./components";
import {ModalWrapper} from "../../components/form/ModalWrapper";
import {AddButton, ClearButton} from "../../components/button";
import {Error} from "../../components/info/Error";
import {getIsLoadingError, getVcs} from "./slice";

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
    const [autocompleteValue, setAutocompleteValue] = useState(null);

    const isLoadingError = useSelector(state => getIsLoadingError(state));

    const updateVc = () => {
        dispatch(updateVirtualCollection({uuid, nameCz, nameEn, descriptionCz, descriptionEn, fullImg, thumbImg}));
    };

    const reloadVcs = () => {
        dispatch(loadVirtualCollections());
    };

    const loadVcUuid = (event, values) => {
        if (values) {
            setNameCz(values.nameCz);
            setNameEn(values.nameEn);
            setDescriptionCz(values.descriptionCz);
            setDescriptionEn(values.descriptionEn);
            setAutocompleteValue(values);
            setUuid(values.uuid);
        } else {
            cleanAll();
        }
    };

    const cleanAll = () => {
        setUuid('');
        setAutocompleteValue(null);
        cleanValues();
    };

    const cleanValues = () => {
        setNameCz('');
        setNameEn('');
        setDescriptionCz('');
        setDescriptionEn('');
        setFullImg(null);
        setThumbImg(null);
    };

    const deleteVc = () => {
        dispatch(deleteVirtualCollection({uuid, nameCz, nameEn}));
        cleanAll();
    };

    const deleteButton = (
        <ModalWrapper
            callback={deleteVc}
            title={'POZOR'}
            titleColor={'secondary'}
            description={'Chcete opravdu smazat vybranou virtuální sbírku?'}
            okMsg={'Ano'}
            cancelMsg={'Ne'}
        >
            <ClearButton>Vymazat sbírku</ClearButton>
        </ModalWrapper>
    );

    const buttonFuncs = {
        actionButton: <AddButton onClick={updateVc}>Upravit</AddButton>,
        deleteButton,
        cleanButton: <ClearButton onClick={cleanValues}>Vyčistit</ClearButton>,
        setFullImg,
        setThumbImg,
    };

    const panelItems = [
        {
            style: {
                width: "50%",
            },
            component: (
                <Autocomplete
                    options={vcs}
                    value={autocompleteValue}
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
            )
        },
        {
            component: (
                <RefreshIconButton
                    onClick={reloadVcs}
                    tooltip={"Načíst aktuální virtuální sbírky"}
                />
            )
        }
    ];

    const content = (
        <Box>
            <Panel items={panelItems} />

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
                loaded={uuid && uuid !== ''}
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
