import {useEffect} from 'react';
import {useDispatch} from 'react-redux';
import {makeStyles} from '@material-ui/core';

import {HorizontallyCenteredBox} from '../../components/layout/HorizontallyCenteredBox';
import {FofolaPage} from '../../components/page/FofolaPage';

import {loadVirtualCollections} from './saga';
import {Sidebar} from './Sidebar';

const useStyles = makeStyles((theme) => ({
    root: {
        backgroundColor: theme.palette.background.paper,
        flexGrow: 1,
        display: 'flex',
        minHeight: '520px',
        borderRadius: '5px',
        border: '1px solid rgb(212, 101, 3)'

    },
    options: {
        borderRight: `1px solid ${theme.palette.divider}`,
    },
    tabs: {
        width: '100%',
    }
}));

export const VcManagement = () => {
    const classes = useStyles();
    const dispatch = useDispatch();

    useEffect(() => {
        dispatch(loadVirtualCollections());
    }, [dispatch]);

    return (
        <FofolaPage>
            <HorizontallyCenteredBox width={'70%'}>
                <Sidebar classes={classes} />
            </HorizontallyCenteredBox>
        </FofolaPage>
    );
};
