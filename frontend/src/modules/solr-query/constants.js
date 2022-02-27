import {accesses, dnntLabels, ExtendedFieldValue, models} from "../constants";

export const dnntFlags = [ExtendedFieldValue.ANY, ExtendedFieldValue.NONE];
export const extendedDnntLabels = [...dnntLabels, ExtendedFieldValue.ANY, ExtendedFieldValue.NONE];
export const extendedAccesses = [...accesses, ExtendedFieldValue.ANY, ExtendedFieldValue.NONE];
export const extendedModels = [...models, ExtendedFieldValue.ANY, ExtendedFieldValue.NONE];

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
