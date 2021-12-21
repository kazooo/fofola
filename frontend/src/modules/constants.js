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

export const dnntLabels = Object.keys(DnntLabel).map((key) => DnntLabel[key]);
export const models = Object.keys(Model).map((key) => Model[key]);
export const accesses = Object.keys(Access).map((key) => Access[key]);
export const solrFields = Object.keys(SolrField).map((key) => SolrField[key]);
