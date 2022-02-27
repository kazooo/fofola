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
    },
    {
        id: 'requestor',
        label: 'Requestor',
        width: 100,
        align: 'center',
    },
    {
        id: 'operation',
        label: 'Operace',
        width: 100,
        align: 'center',
    },
    {
        id: 'status',
        label: 'Status',
        width: 100,
        align: 'center',
    },
];

export const SugoSessionDirection = Object.freeze({
    Rest2Dst: {
        text: 'REST_2_DST',
        value: 'REST_2_DST',
    },
    Src2Dst: {
        text: 'SRC_2_DST',
        value: 'SRC_2_DST',
    },
    Dst2Src: {
        text: 'DST_2_SRC',
        value: 'DST_2_SRC',
    },
    Any: {
        text: 'jakýkoliv',
        value: 'any',
    },
});

export const SugoSessionRequestor = Object.freeze({
    Auto: {
        text: 'AUTO',
        value: 'AUTO',
    },
    Rest: {
        text: 'REST',
        value: 'REST',
    },
    Any: {
        text: 'jakýkoliv',
        value: 'any',
    },
});

export const SugoSessionOperation = Object.freeze({
    Search: {
        text: 'SEARCH',
        value: 'SEARCH',
    },
    AddLabel: {
        text: 'ADD_LABEL',
        value: 'ADD_LABEL',
    },
    RemoveLabel: {
        text: 'REMOVE_LABEL',
        value: 'REMOVE_LABEL',
    },
    Clean: {
        text: 'CLEAN_LABELS',
        value: 'CLEAN_LABELS',
    },
    Synchronize: {
        text: 'SYNCHRONIZE',
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
