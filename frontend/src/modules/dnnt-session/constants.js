import i18n from "i18next";
import {SugoSessionDirectionExtended, SugoSessionOperation, SugoSessionRequestor, SugoSessionStatus} from '../constants';

export const columns = [
    {
        id: 'id',
        label: 'Id',
        width: 50,
        align: 'center',
    },
    {
        id: 'created',
        label: 'Z',
        width: 170,
        align: 'center',
    },
    {
        id: 'finished',
        label: 'Do',
        width: 170,
        align: 'center',
    },
    {
        id: 'direction',
        label: 'Směr',
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
        label: 'Requestor',
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
        label: 'Operace',
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
        id: 'status',
        label: 'Status',
        width: 100,
        align: 'center',
        format: value => i18n.t(
            Object
                .values(SugoSessionStatus)
                .find(status => status.value === value)
                .text
        ),
    },
];
