import {useDispatch, useSelector} from 'react-redux';

import {HorizontalDirectedGrid} from '../../components/layout/HorizontalDirectedGrid';
import {ClearButton, StartButton} from '../../components/button';
import {InlineP} from '../../components/page/InlineP';

import {clearUuids, getLabel, getMode, getProcessRecursive, getUuids} from './slice';
import {changeLabel} from './saga';

export const LinkDnntPanel = () => {
    const dispatch = useDispatch();
    const mode = useSelector(getMode);
    const uuids = useSelector(getUuids);
    const label = useSelector(getLabel);
    const processRecursive = useSelector(getProcessRecursive);

    const handleOnClick = (e) => {
        e.preventDefault();
        dispatch(changeLabel({label, mode, uuids, processRecursive}));
    }

    const clear = (e) => {
        e.preventDefault();
        dispatch(clearUuids(uuids));
    }

    return uuids.length > 0 && mode && label &&
        <HorizontalDirectedGrid
            spacing={10}
        >
            <StartButton onClick={handleOnClick}>Spustit process</StartButton>
            <InlineP>Celkem: {uuids.length}</InlineP>
            <ClearButton onClick={clear}>Vyčistit</ClearButton>
        </HorizontalDirectedGrid>;
};
