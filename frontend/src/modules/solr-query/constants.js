import {accesses, dnntLabels, models} from "../constants";

export const ExtendedSolrFieldValue = Object.freeze({
    ANY: {
        text: 'jakýkoliv',
        value: 'any',
    },
    NONE: {
        text: 'žádný',
        value: 'none',
    },
});

export const dnntFlags = [ExtendedSolrFieldValue.ANY, ExtendedSolrFieldValue.NONE];
export const extendedDnntLabels = [...dnntLabels, ExtendedSolrFieldValue.ANY, ExtendedSolrFieldValue.NONE];
export const extendedAccesses = [...accesses, ExtendedSolrFieldValue.ANY, ExtendedSolrFieldValue.NONE];
export const extendedModels = [...models,ExtendedSolrFieldValue.ANY, ExtendedSolrFieldValue.NONE];

export const columns = [
    {
        id: 'filename',
        label: 'Název souboru',
        maxWidth: 303,
        align: 'center',
    },
    {
        id: 'action',
        label: 'Akce',
        maxWidth: 100,
        align: 'center',
    },
];
