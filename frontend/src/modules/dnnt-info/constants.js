export const columns = [
    {
        id: 'tableAction',
        maxWidth: 30,
        align: 'center',
    },
    {
        id: 'uuid',
        label: 'Interní UUID',
        width: 300,
        align: 'center',
    },
    {
        id: 'name',
        label: 'Název',
        width: 70,
        align: 'center',
    },
    {
        id: 'model',
        label: 'Model',
        width: 70,
        align: 'center',
    },
    {
        id: 'access',
        label: 'Dostupnost',
        width: 70,
        align: 'center',
    },
    {
        id: 'cnb',
        label: 'ČNB',
        width: 70,
        align: 'center',
    },
    {
        id: 'labels',
        label: 'Labely',
        width: 70,
        align: 'center',
        format: (value) => value?.join(', '),
    },
    {
        id: 'sourceIdentifier',
        label: 'ID zdroje',
        width: 100,
        align: 'center',
    },
    {
        id: 'sourceUuid',
        label: 'Zdrojový UUID',
        width: 300,
        align: 'center',
    },
    {
        id: 'sourceLicence',
        label: 'Zdrojová licence',
        width: 70,
        align: 'center',
    },
    {
        id: 'action',
        label: 'Akce',
        maxWidth: 100,
        align: 'center',
    },
];
