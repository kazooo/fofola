import {useDispatch, useSelector} from 'react-redux';

import {HorizontalDirectedGrid} from 'components/layout/HorizontalDirectedGrid';
import {VerticalDirectedGrid} from 'components/layout/VerticalDirectedGrid';
import {LabeledCheckbox} from 'components/form/LabeledCheckbox';
import {ClearButton, DeleteButton} from 'components/button';
import {ModalWrapper} from 'components/form/ModalWrapper';
import {InlineP} from 'components/page/InlineP';

import {deleteUuids} from './saga';
import {
    getDeleteFromSolrOnly,
    getDeleteRecursively,
    toggleDeleteFromSolrOnly,
    toggleDeleteRecursively,
    clearUuids,
    getUuids
} from './slice';

export const DeletePanel = () => {

    const dispatch = useDispatch();
    const uuids = useSelector(getUuids);
    const deleteFromSolrOnly = useSelector(getDeleteFromSolrOnly);
    const deleteRecursively = useSelector(getDeleteRecursively);

    const handleOnClick = () => {
        dispatch(deleteUuids({ uuids, deleteFromSolrOnly, deleteRecursively }));
        handleToggleDeleteFromSolrOnly();
    };

    const clear = (e) => {
        e.preventDefault();
        dispatch(clearUuids(uuids));
    };

    const handleToggleDeleteFromSolrOnly = () => {
        if (deleteRecursively) {
            dispatch(toggleDeleteRecursively());
        }
        dispatch(toggleDeleteFromSolrOnly());
    };

    const handleToggleDeleteRecursively = () => {
        dispatch(toggleDeleteRecursively());
    };

    return uuids.length > 0 && (
        <VerticalDirectedGrid>
            <HorizontalDirectedGrid>
                <LabeledCheckbox
                    label={'feature.delete.form.deleteFromSolrOnly'}
                    onChange={handleToggleDeleteFromSolrOnly}
                    checked={deleteFromSolrOnly}
                />
                {
                    deleteFromSolrOnly && (
                        <LabeledCheckbox
                            label={'feature.delete.form.deleteRecursively'}
                            onChange={handleToggleDeleteRecursively}
                            checked={deleteRecursively}
                        />
                    )
                }
            </HorizontalDirectedGrid>
            <HorizontalDirectedGrid spacing={10}>
                <ModalWrapper
                    onOk={handleOnClick}
                    title={'common.title.warning'}
                    titleColor={'secondary'}
                    description={
                        deleteFromSolrOnly ?
                            'feature.delete.confirmSolrOnly'
                            :
                            'feature.delete.confirmComplete'
                    }
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
