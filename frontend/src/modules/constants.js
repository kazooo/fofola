export const DnntLabel = Object.freeze({
    DNNTO: {
        text: 'DNNTO',
        value: 'dnnto',
    },
    DNNTT: {
        text: 'DNNTT',
        value: 'dnntt',
    },
    COVID: {
        text: 'COVID',
        value: 'covid',
    },
});

export const DnntLinkingMode = Object.freeze({
    Link: 'link',
    Unlink: 'unlink',
    Synchronize: 'sync',
    Clean: 'clean'
});

export const Model = Object.freeze({
    MONOGRAPH: {
        value: 'monograph',
        text: 'monograph',
    },
    PERIODICAL: {
        value: 'periodical',
        text: 'periodical',
    },
    PERIODICALVOLUME: {
        value: 'periodicalvolume',
        text: 'periodicalvolume',
    },
    PERIODICALITEM: {
        value: 'periodicalitem',
        text: 'periodicalitem',
    },
    ARTICLE: {
        value: 'article',
        text: 'article',
    },
    MAP: {
        value: 'map',
        text: 'map',
    },
    PAGE: {
        value: 'page',
        text: 'page',
    },
    GRAPHIC: {
        value: 'graphic',
        text: 'graphic',
    },
    MONOGRAPHUNIT: {
        value: 'monographunit',
        text: 'monographunit',
    },
});

export const Access = Object.freeze({
    PRIVATE: {
        text: 'private',
        value: 'private',
    },
    PUBLIC: {
        text: 'public',
        value: 'public',
    },
});

export const SolrField = Object.freeze({
    UUID: {
        text: 'uuid',
        value: 'PID',
    },
});

export const ExtendedFieldValue = Object.freeze({
    ANY: {
        text: 'jakýkoliv',
        value: 'any',
    },
    NONE: {
        text: 'žádný',
        value: 'none',
    },
});

export const SugoSessionDirection = Object.freeze({
    Rest2Dst: {
        text: 'požadavek uživatele',
        value: 'REST_2_DST',
    },
    Src2Dst: {
        text: 'synchronizace SDNNT záznamů',
        value: 'SRC_2_DST',
    },
    Dst2Src: {
        text: 'synchronizace interních záznamů',
        value: 'DST_2_SRC',
    },
    Any: ExtendedFieldValue.ANY,
});

export const SugoSessionRequestor = Object.freeze({
    Auto: {
        text: 'automatická labelizace',
        value: 'AUTO',
    },
    Rest: {
        text: 'uživatel',
        value: 'REST',
    },
    Any: ExtendedFieldValue.ANY,
});

export const SugoSessionOperation = Object.freeze({
    AddLabel: {
        text: 'přídání labelu',
        value: 'ADD_LABEL',
    },
    RemoveLabel: {
        text: 'odebirání labelu',
        value: 'REMOVE_LABEL',
    },
    Clean: {
        text: 'čištění labelů',
        value: 'CLEAN_LABELS',
    },
    Synchronize: {
        text: 'synchronizace',
        value: 'SYNCHRONIZE',
    },
    Any: ExtendedFieldValue.ANY,
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
    FinishedWithNegligibleIssues: {
        text: 'ukončený se zanedbatelnými chybami',
        value: 'FINISHED_WITH_NEGLIGIBLE_ISSUES',
    },
    FinishedWithSeriousIssues: {
        text: 'ukončený se závažnými chybami',
        value: 'FINISHED_WITH_SERIOUS_ISSUES',
    },
    TerminatedByException: {
        text: 'zrušeno výjimkou',
        value: 'TERMINATED_BY_EXCEPTION',
    },
    TerminatedByShutdown: {
        text: 'zrušeno vypínáním',
        value: 'TERMINATED_BY_SHUTDOWN',
    },
    Any: ExtendedFieldValue.ANY,
});

/* just arrays of objects from structures above */
export const dnntLabels = Object.keys(DnntLabel).map((key) => DnntLabel[key]);
export const models = Object.keys(Model).map((key) => Model[key]);
export const accesses = Object.keys(Access).map((key) => Access[key]);
export const solrFields = Object.keys(SolrField).map((key) => SolrField[key]);

export const sugoSessionDirections = Object.keys(SugoSessionDirection).map((key) => SugoSessionDirection[key]);
export const sugoSessionRequestors = Object.keys(SugoSessionRequestor).map((key) => SugoSessionRequestor[key]);
export const sugoSessionOperations = Object.keys(SugoSessionOperation).map((key) => SugoSessionOperation[key]);
export const sugoSessionStatuses = Object.keys(SugoSessionStatuses).map((key) => SugoSessionStatuses[key]);
