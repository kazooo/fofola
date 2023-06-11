import {useDispatch, useSelector} from 'react-redux';
import {useEffect} from 'react';

import {FofolaPage} from 'components/page/FofolaPage';
import {HorizontallyCenteredBox} from 'components/layout/HorizontallyCenteredBox';

import {createDashboardStyle} from './style';
import {requestPage} from './saga';
import {Table} from './Table';
import {AlertWindow} from './AlertWindow';
import {isAlertWindowOpen as isAlertWindowOpenSelector} from './slice';

export const DnntAlert = () => {
    const classes = createDashboardStyle();
    const dispatch = useDispatch();
    const isAlertWindowOpen = useSelector(isAlertWindowOpenSelector);

    useEffect(() => {
        dispatch(requestPage(0, 20));
    }, [dispatch]);

    return (
        <FofolaPage>
            <HorizontallyCenteredBox width={'70%'}>
                <Table classes={classes} />
                {isAlertWindowOpen && <AlertWindow classes={classes} />}
            </HorizontallyCenteredBox>
        </FofolaPage>
    );
}
