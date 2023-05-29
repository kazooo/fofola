import i18n from "i18next";
import {SugoSessionDirectionExtended, SugoSessionOperation, SugoSessionRequestor, SugoSessionStatus} from '../constants';

export const columns = [
    {
        id: 'id',
        label: 'feature.dnntSessions.table.columns.id',
        width: 50,
        align: 'center',
    },
    {
        id: 'created',
        label: 'feature.dnntSessions.table.columns.created',
        width: 170,
        align: 'center',
    },
    {
        id: 'finished',
        label: 'feature.dnntSessions.table.columns.finished',
        width: 170,
        align: 'center',
    },
    {
        id: 'direction',
        label: 'feature.dnntSessions.table.columns.direction',
        width: 100,
        align: 'center',
        format: value => i18n.t(
            Object
                .values(SugoSessionDirectionExtended)
                .find(direction => direction.value === value)
                .text
        ),
    },
    {
        id: 'requestor',
        label: 'feature.dnntSessions.table.columns.requestor',
        width: 100,
        align: 'center',
        format: value => i18n.t(
            Object
                .values(SugoSessionRequestor)
                .find(requestor => requestor.value === value)
                .text
        ),
    },
    {
        id: 'operation',
        label: 'feature.dnntSessions.table.columns.operation',
        width: 100,
        align: 'center',
        format: value => i18n.t(
            Object
                .values(SugoSessionOperation)
                .find(operation => operation.value === value)
                .text
        ),
    },
    {
        id: 'total',
        label: 'feature.dnntSessions.table.columns.total',
        maxWidth: 100,
        align: 'center',
    },
    {
        id: 'done',
        label: 'feature.dnntSessions.table.columns.done',
        maxWidth: 100,
        align: 'center',
    },
    {
        id: 'status',
        label: 'feature.dnntSessions.table.columns.status',
        width: 100,
        align: 'center',
        format: value => i18n.t(
            Object
                .values(SugoSessionStatus)
                .find(status => status.value === value)
                .text
        ),
    },
    {
        id: 'actions',
        label: 'feature.dnntSessions.table.columns.actions',
        maxWidth: 100,
        align: 'center',
    }
];
