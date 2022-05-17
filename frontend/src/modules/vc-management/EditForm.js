import React, {useState} from 'react';
import {useTranslation} from 'react-i18next';
import {useDispatch, useSelector} from 'react-redux';
import {Box, TextField} from '@material-ui/core';
import Autocomplete from '@material-ui/lab/Autocomplete';

import {HorizontallyCenteredBox} from '../../components/layout/HorizontallyCenteredBox';
import {RefreshIconButton} from '../../components/button/iconbuttons';
import {ModalWrapper} from '../../components/form/ModalWrapper';
import {AddButton, ClearButton} from '../../components/button';
import {Error} from '../../components/info/Error';

import {deleteVirtualCollection, loadVirtualCollections, updateVirtualCollection} from './saga';
import {Buttons, Panel, VCDescriptions, VCNames} from './components';
import {getIsLoadingError, getVcs} from './slice';

export const EditForm = () => {
    const dispatch = useDispatch();
    const {t} = useTranslation();
    
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
            setNameCz(values.nameCz ? values.nameCz : '');
            setNameEn(values.nameEn ? values.nameEn : '');
            setDescriptionCz(values.descriptionCz ? values.descriptionCz : '');
            setDescriptionEn(values.descriptionEn ? values.descriptionEn : '');
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
            title={'common.title.warning'}
            titleColor={'secondary'}
            description={'feature.vcManagement.confirmDelete'}
            okMsg={'common.yes'}
            cancelMsg={'common.no'}
        >
            <ClearButton label={'feature.vcManagement.button.delete'} />
        </ModalWrapper>
    );

    const buttonFuncs = {
        actionButton: <AddButton onClick={updateVc} label={"common.button.edit"} />,
        deleteButton,
        cleanButton: <ClearButton onClick={cleanValues} />,
        setFullImg,
        setThumbImg,
    };

    const panelItems = [
        {
            style: {
                width: '50%',
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
                            label={t('feature.vcManagement.form.vcName')}
                            variant='outlined'
                            size='small'
                        />
                    }
                />
            )
        },
        {
            component: (
                <RefreshIconButton
                    onClick={reloadVcs}
                    tooltip={t('feature.vcManagement.button.loadVcs')}
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
                loaded={uuid && uuid !== ''}
                fullImg={fullImg}
                thumbImg={thumbImg}
                {...buttonFuncs}
            />
        </Box>
    );

    const error = (
        <HorizontallyCenteredBox>
             <Box mt={'20%'}>
                 <Error label={t('feature.vcManagement.loading.error')} />
             </Box>
        </HorizontallyCenteredBox>
    );

    return (
        <Box height={'100%'}>
            {
                isLoadingError ? error : content
            }
        </Box>
    );
};
