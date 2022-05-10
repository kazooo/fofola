import {useDispatch, useSelector} from 'react-redux';

import {HorizontalDirectedGrid} from '../../components/layout/HorizontalDirectedGrid';
import {VerticalDirectedGrid} from '../../components/layout/VerticalDirectedGrid';
import {ClearButton, DeleteButton} from '../../components/button';
import {Checkbox} from '../../components/form/Checkbox';
import {InlineP} from '../../components/page/InlineP';

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

    const handleOnClick = (e) => {
        e.preventDefault();
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
                <Checkbox
                    label={'Smazat pouze ze Solru'}
                    onChange={handleToggleDeleteFromSolrOnly}
                    checked={deleteFromSolrOnly}
                />
                {
                    deleteFromSolrOnly && (
                        <Checkbox
                            label={'Smazat rekurzivně'}
                            onChange={handleToggleDeleteRecursively}
                            checked={deleteRecursively}
                        />
                    )
                }
            </HorizontalDirectedGrid>
            <HorizontalDirectedGrid spacing={10}>
                <DeleteButton onClick={handleOnClick}>Vymazat</DeleteButton>
                <InlineP>Celkem: {uuids.length}</InlineP>
                <ClearButton onClick={clear}>Vyčistit</ClearButton>
            </HorizontalDirectedGrid>
        </VerticalDirectedGrid>
    );
}
