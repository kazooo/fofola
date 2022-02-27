import {accesses, ExtendedFieldValue, models} from '../constants';

export const columns = [
    {
        id: 'id',
        label: 'Id',
        width: 50,
        align: 'center',
    },
    {
        id: 'changeDateTime',
        label: 'Datum změny',
        width: 250,
        align: 'center',
    },
    {
        id: 'uuid',
        label: 'Interní UUID',
        width: 170,
        align: 'center',
    },
    {
        id: 'model',
        label: 'Model',
        width: 100,
        align: 'center',
    },
    {
        id: 'access',
        label: 'Dostupnost',
        width: 100,
        align: 'center',
    },
    {
        id: 'cnb',
        label: 'ČNB',
        width: 100,
        align: 'center',
    },
    {
        id: 'currentLabels',
        label: 'Staré labely',
        width: 170,
        align: 'center',
    },
    {
        id: 'newLabels',
        label: 'Nové labely',
        width: 170,
        align: 'center',
    },
    {
        id: 'notes',
        label: 'Poznámky',
        width: 170,
        align: 'center',
    },
    {
        id: 'sourceIdentifier',
        label: 'Zdrojový ID',
        width: 100,
        align: 'center',
    },
    {
        id: 'sourceUuid',
        label: 'Zdrojový UUID',
        width: 170,
        align: 'center',
    },
];

export const extendedAccesses = [...accesses, ExtendedFieldValue.ANY];
export const extendedModels = [...models, ExtendedFieldValue.ANY];
