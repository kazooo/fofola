import i18n from 'i18next';
import {AlertTypeTranslationKey} from './constants';
import {
    DnntLabel,
    SugoSessionDirectionExtended,
    SugoSessionOperation,
    SugoSessionRequestor,
    SugoSessionStatus
} from '../constants';

export const alertTableColumns = Object.freeze([
    {
        id: 'id',
        label: 'feature.dnntAlerts.table.columns.id',
        width: 100,
        align: 'center'
    },
    {
        id: 'type',
        label: 'feature.dnntAlerts.table.columns.type',
        width: 100,
        align: 'center',
        format: (value) => i18n.t(AlertTypeTranslationKey[value])
    },
    {
        id: 'issueType',
        label: 'feature.dnntAlerts.table.columns.issueType',
        width: 200,
        align: 'center',
    },
    {
        id: 'shortDescription',
        label: 'feature.dnntAlerts.table.columns.shortDescription',
        width: 400,
        align: 'center',
    },
    {
        id: 'created',
        label: 'feature.dnntAlerts.table.columns.created',
        width: 200,
        align: 'center',
    },
    {
        id: 'actions',
        label: 'feature.dnntAlerts.table.columns.actions',
        width: 100,
        align: 'center',
    }
]);

export const sessionDetailsColumns = Object.freeze([
    {
        id: 'id',
        label: 'feature.dnntSessions.table.columns.id',
        header: {
            width: 60,
            align: 'right',
            padding: '5px 16px 5px 5px',
        },
        value: {
            width: 'auto',
            align: 'center',
            padding: '5px 0px 5px 0px',
        },
    },
    {
        id: 'created',
        label: 'feature.dnntSessions.table.columns.created',
        header: {
            width: 60,
            align: 'right',
            padding: '5px 16px 5px 5px',
        },
        value: {
            width: 'auto',
            align: 'center',
            padding: '5px 0px 5px 0px',
        },
    },
    {
        id: 'finished',
        label: 'feature.dnntSessions.table.columns.finished',
        header: {
            width: 60,
            align: 'right',
            padding: '5px 16px 5px 5px',
        },
        value: {
            width: 'auto',
            align: 'center',
            padding: '5px 0px 5px 0px',
        },
    },
    {
        id: 'direction',
        label: 'feature.dnntSessions.table.columns.direction',
        header: {
            width: 60,
            align: 'right',
            padding: '5px 16px 5px 5px',
        },
        value: {
            width: 'auto',
            align: 'center',
            padding: '5px 0px 5px 0px',
        },
        format: value => i18n.t(
            Object
                .values(SugoSessionDirectionExtended)
                .find(direction => direction.value === value)
                ?.text
        ),
    },
    {
        id: 'requestor',
        label: 'feature.dnntSessions.table.columns.requestor',
        header: {
            width: 60,
            align: 'right',
            padding: '5px 16px 5px 5px',
        },
        value: {
            width: 'auto',
            align: 'center',
            padding: '5px 0px 5px 0px',
        },
        format: value => i18n.t(
            Object
                .values(SugoSessionRequestor)
                .find(requestor => requestor.value === value)
                ?.text
        ),
    },
    {
        id: 'operation',
        label: 'feature.dnntSessions.table.columns.operation',
        header: {
            width: 60,
            align: 'right',
            padding: '5px 16px 5px 5px',
        },
        value: {
            width: 'auto',
            align: 'center',
            padding: '5px 0px 5px 0px',
        },
        format: value => i18n.t(
            Object
                .values(SugoSessionOperation)
                .find(operation => operation.value === value)
                ?.text
        ),
    },
    {
        id: 'label',
        label: 'feature.dnntSessions.table.columns.label',
        header: {
            width: 60,
            align: 'right',
            padding: '5px 16px 5px 5px',
        },
        value: {
            width: 'auto',
            align: 'center',
            padding: '5px 0px 5px 0px',
        },
        format: value => i18n.t(
            Object
                .values(DnntLabel)
                .find(operation => operation.value === value)
                ?.text
        ),
    },
    {
        id: 'status',
        label: 'feature.dnntSessions.table.columns.status',
        header: {
            width: 60,
            align: 'right',
            padding: '5px 16px 5px 5px',
        },
        value: {
            width: 'auto',
            align: 'center',
            padding: '5px 0px 5px 0px',
        },
        format: value => i18n.t(
            Object
                .values(SugoSessionStatus)
                .find(status => status.value === value)
                ?.text
        ),
    },
]);

export const documentDetailsColumns = Object.freeze([
    {
        id: 'uuid',
        label: 'feature.dnntAlerts.alert.section.documentDetails.columns.uuid',
        header: {
            width: 60,
            align: 'right',
            padding: '5px 16px 5px 5px',
        },
        value: {
            width: 'auto',
            align: 'center',
            padding: '5px 0px 5px 0px',
        },
    },
    {
        id: 'title',
        label: 'feature.dnntAlerts.alert.section.documentDetails.columns.title',
        header: {
            width: 60,
            align: 'right',
            padding: '5px 16px 5px 5px',
        },
        value: {
            width: 'auto',
            align: 'left',
            padding: '5px 0px 5px 0px',
        }
    },
    {
        id: 'root',
        label: 'feature.dnntAlerts.alert.section.documentDetails.columns.root',
        header: {
            width: 60,
            align: 'right',
            padding: '5px 16px 5px 5px',
        },
        value: {
            width: 'auto',
            align: 'left',
            padding: '5px 0px 5px 0px',
        }
    },
    {
        id: 'path',
        label: 'feature.dnntAlerts.alert.section.documentDetails.columns.path',
        header: {
            width: 60,
            align: 'right',
            padding: '5px 16px 5px 5px',
        },
        value: {
            width: 'auto',
            align: 'left',
            padding: '5px 0px 5px 0px',
        }
    },
    {
        id: 'currentLabels',
        label: 'feature.dnntAlerts.alert.section.documentDetails.columns.currentLabels',
        header: {
            width: 60,
            align: 'right',
            padding: '5px 16px 5px 5px',
        },
        value: {
            width: 'auto',
            align: 'left',
            padding: '5px 0px 5px 0px',
        }
    },
    {
        id: 'nextLabels',
        label: 'feature.dnntAlerts.alert.section.documentDetails.columns.nextLabels',
        header: {
            width: 60,
            align: 'right',
            padding: '5px 16px 5px 5px',
        },
        value: {
            width: 'auto',
            align: 'left',
            padding: '5px 0px 5px 0px',
        }
    }
]);
