import React from 'react';
import {useTranslation} from 'react-i18next';
import {Box, Grid, TextField} from '@material-ui/core';
import CheckCircleOutlineIcon from '@material-ui/icons/CheckCircleOutline';

import {UploadButton} from '../../components/button';

export const styles = {
    stringInputStyle: {
        width: '100%',
    },
    textInputStyle: {
        width: '100%',
    },
    wrapperStyle: {
        padding: '30px 15px 0 30px',
    },
    containerStyle: {
        width: '100%',
    }
};

const containerProps = {
    spacing: 2,
    alignContent: 'center',
    justifyContent: 'center',
    alignItems: 'center',
};

export const Panel = ({items}) => {
    const gridItems = items.map((item, i) =>
        <Grid item key={i} style={item.style}>
            {item.component}
        </Grid>
    );

    return (
        <Box style={styles.wrapperStyle} width={'100%'}>
            <Grid container style={styles.containerStyle} {...containerProps}>
                {gridItems}
            </Grid>
        </Box>
    );
}

export const VCNames = ({nameCz, nameEn, setNameCz, setNameEn}) => {
    const {t} = useTranslation();

    return (
        <Box style={styles.wrapperStyle}>
            <Grid container style={styles.containerStyle} {...containerProps}>
                <Grid item xs={6}>
                    <TextField
                        label={t('feature.vcManagement.form.vcNameCz')}
                        variant='outlined'
                        value={nameCz}
                        style={styles.stringInputStyle}
                        onChange={e => setNameCz(e.target.value)}
                    />
                </Grid>
                <Grid item xs={6}>
                    <TextField
                        label={t('feature.vcManagement.form.vcNameEn')}
                        variant='outlined'
                        value={nameEn}
                        style={styles.stringInputStyle}
                        onChange={e => setNameEn(e.target.value)}
                    />
                </Grid>
            </Grid>
        </Box>
    );
};

export const VCDescriptions = ({textCz, textEn, setTextCz, setTextEn}) => {
    const {t} = useTranslation();

    return (
        <Box style={styles.wrapperStyle}>
            <Grid container style={styles.containerStyle} {...containerProps}>
                <Grid item xs={6}>
                    <TextField
                        label={t('feature.vcManagement.form.vcDescriptionCz')}
                        multiline
                        rows={10}
                        value={textCz}
                        style={styles.textInputStyle}
                        variant='outlined'
                        onChange={e => setTextCz(e.target.value)}
                    />
                </Grid>
                <Grid item xs={6}>
                    <TextField
                        label={t('feature.vcManagement.form.vcDescriptionEn')}
                        multiline
                        rows={10}
                        value={textEn}
                        style={styles.textInputStyle}
                        variant='outlined'
                        onChange={e => setTextEn(e.target.value)}
                    />
                </Grid>
            </Grid>
        </Box>
    );
};

export const Buttons = ({anyContent, loaded, actionButton, deleteButton, cleanButton, fullImg, setFullImg, thumbImg, setThumbImg}) => {
    return (
        <Box style={styles.wrapperStyle}>
            <Grid container style={styles.containerStyle}>
                <Grid item xs={3} align='center'>
                    <UploadImgButton img={fullImg} setFunc={setFullImg} label={'feature.vcManagement.button.chooseFullImg'} />
                </Grid>
                <Grid item xs={3} align='center'>
                    <UploadImgButton img={thumbImg} setFunc={setThumbImg} label={'feature.vcManagement.button.chooseThumbImg'} />
                </Grid>
                {
                    anyContent && cleanButton && (
                        <Grid item xs={2} align='center'>
                            {cleanButton}
                        </Grid>
                    )
                }
                {
                    loaded && deleteButton && (
                        <Grid item xs={2} align='center'>
                            {deleteButton}
                        </Grid>
                    )
                }
                {
                    loaded && actionButton && (
                        <Grid item xs={2} align='center'>
                            {actionButton}
                        </Grid>
                    )
                }
            </Grid>
        </Box>
    );
};

const UploadImgButton = ({label, img, setFunc}) => {
    const {t} = useTranslation();

    const uploadImg = e => {
        const file = e.target.files[0];
        setFunc(file);
    };

    return (
        <Grid container justifyContent={'center'} alignItems={'center'}>
            <Grid item>
                <UploadButton onChange={uploadImg}>
                    {t(label)}
                    <input type='file' hidden />
                </UploadButton>
            </Grid>
            {
                img && (
                    <Grid item>
                        <CheckCircleOutlineIcon />
                    </Grid>
                )
            }
        </Grid>
    );
};
