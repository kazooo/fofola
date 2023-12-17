import {useDispatch, useSelector} from 'react-redux';

import {HorizontalDirectedGrid} from 'components/layout/HorizontalDirectedGrid';
import {VerticalDirectedGrid} from 'components/layout/VerticalDirectedGrid';
import {ClearButton, DeleteButton} from 'components/button';
import {ModalWrapper} from 'components/form/ModalWrapper';
import {InlineP} from 'components/page/InlineP';

import {deleteUuids} from './saga';
import {
    clearUuids,
    getUuids
} from './slice';

export const DeletePanel = () => {

    const dispatch = useDispatch();
    const uuids = useSelector(getUuids);

    const handleOnClick = () => {
        dispatch(deleteUuids({ uuids }));
    };

    const clear = (e) => {
        e.preventDefault();
        dispatch(clearUuids(uuids));
    };

    return uuids.length > 0 && (
        <VerticalDirectedGrid>
            <HorizontalDirectedGrid spacing={10}>
                <ModalWrapper
                    onOk={handleOnClick}
                    title={'common.title.warning'}
                    titleColor={'secondary'}
                    description={'feature.delete.confirmComplete'}
                    okMsg={'common.yes'}
                    cancelMsg={'common.no'}
                >
                    <DeleteButton />
                </ModalWrapper>
                <InlineP>Celkem: {uuids.length}</InlineP>
                <ClearButton onClick={clear} />
            </HorizontalDirectedGrid>
        </VerticalDirectedGrid>
    );
}
