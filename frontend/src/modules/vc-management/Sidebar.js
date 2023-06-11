import React, {useState} from 'react';
import {Box} from '@material-ui/core';
import {useSelector} from 'react-redux';
import {useTranslation} from 'react-i18next';

import {HorizontallyCenteredBox} from 'components/layout/HorizontallyCenteredBox';
import {Loading} from 'components/info/Loading';

import {getIsLoading} from './slice';
import {CreateForm} from './CreateForm';
import {EditForm} from './EditForm';
import {Options} from './Options';
import {Tabs} from './Tabs';

export const Sidebar = ({classes}) => {
    const {t} = useTranslation();
    const [tabNum, setTabNum] = useState(0);
    const isLoading = useSelector(state => getIsLoading(state));

    const handleChange = (event, newValue) => {
        setTabNum(newValue);
    };

    const panel = (
        <>
            <Options classes={classes} value={tabNum} handleChange={handleChange}>
                <span>{t('feature.vcManagement.tab.create')}</span>
                <span>{t('feature.vcManagement.tab.edit')}</span>
            </Options>
            <Tabs value={tabNum} classes={classes}>
                <CreateForm />
                <EditForm />
            </Tabs>
        </>
    );

    const loading = (
        <HorizontallyCenteredBox>
            <Loading label={'feature.vcManagement.loading.active'} />
        </HorizontallyCenteredBox>
    );

    return <Box className={classes.root}>
        {
            isLoading ? loading : panel
        }
    </Box>;
};


