export const columns = [
    {
        id: 'sessionId',
        label: 'Id procesu',
        width: 300,
        align: 'center',
    },
    {
        id: 'created',
        label: 'Datum změny',
        width: 150,
        align: 'center',
    },
    {
        id: 'uuid',
        label: 'Interní UUID',
        width: 400,
        align: 'center',
    },
    {
        id: 'previousState',
        label: 'Staré labely',
        width: 170,
        align: 'center',
        format: (value) => value?.labels?.join(', '),
    },
    {
        id: 'currentState',
        label: 'Nové labely',
        width: 170,
        align: 'center',
        format: (value) => value?.labels?.join(', '),
    },
];
