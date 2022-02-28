export const columns = [
    {
        id: 'id',
        label: 'Id',
        width: 50,
        align: 'center',
    },
    {
        id: 'fromDateTime',
        label: 'Z',
        width: 170,
        align: 'center',
    },
    {
        id: 'toDateTime',
        label: 'Do',
        width: 170,
        align: 'center',
    },
    {
        id: 'direction',
        label: 'Směr',
        width: 100,
        align: 'center',
        format: value => Object.values(SugoSessionDirection).find(direction => direction.value === value).text,
    },
    {
        id: 'requestor',
        label: 'Requestor',
        width: 100,
        align: 'center',
        format: value => Object.values(SugoSessionRequestor).find(requestor => requestor.value === value).text,
    },
    {
        id: 'operation',
        label: 'Operace',
        width: 100,
        align: 'center',
        format: value => Object.values(SugoSessionOperation).find(operation => operation.value === value).text,
    },
    {
        id: 'status',
        label: 'Status',
        width: 100,
        align: 'center',
        format: value => Object.values(SugoSessionStatuses).find(status => status.value === value).text,
    },
];

export const SugoSessionDirection = Object.freeze({
    Rest2Dst: {
        text: 'Požadavek uživatele',
        value: 'REST_2_DST',
    },
    Src2Dst: {
        text: 'Synchronizace SDNNT záznamů',
        value: 'SRC_2_DST',
    },
    Dst2Src: {
        text: 'Synchronizace interních záznamů',
        value: 'DST_2_SRC',
    },
    Any: {
        text: 'jakýkoliv',
        value: 'any',
    },
});

export const SugoSessionRequestor = Object.freeze({
    Auto: {
        text: 'Automatická labelizace',
        value: 'AUTO',
    },
    Rest: {
        text: 'Uživatel',
        value: 'REST',
    },
    Any: {
        text: 'jakýkoliv',
        value: 'any',
    },
});

export const SugoSessionOperation = Object.freeze({
    AddLabel: {
        text: 'Přídání labelu',
        value: 'ADD_LABEL',
    },
    RemoveLabel: {
        text: 'Odebirání labelu',
        value: 'REMOVE_LABEL',
    },
    Clean: {
        text: 'Čištění labelů',
        value: 'CLEAN_LABELS',
    },
    Synchronize: {
        text: 'Synchronizace',
        value: 'SYNCHRONIZE',
    },
    Any: {
        text: 'jakýkoliv',
        value: 'any',
    },
});

export const SugoSessionStatuses = Object.freeze({
    Active: {
        text: 'aktivní',
        value: 'ACTIVE',
    },
    Finished: {
        text: 'ukončený',
        value: 'FINISHED',
    },
    Any: {
        text: 'jakýkoliv',
        value: 'any',
    },
});

export const sugoSessionDirections = Object.keys(SugoSessionDirection).map((key) => SugoSessionDirection[key]);
export const sugoSessionRequestors = Object.keys(SugoSessionRequestor).map((key) => SugoSessionRequestor[key]);
export const sugoSessionOperations = Object.keys(SugoSessionOperation).map((key) => SugoSessionOperation[key]);
export const sugoSessionStatuses = Object.keys(SugoSessionStatuses).map((key) => SugoSessionStatuses[key]);
