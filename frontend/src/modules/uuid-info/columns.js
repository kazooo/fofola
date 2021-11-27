import {Link} from "@material-ui/core";

export const columns = [
    {
        id: 'tableAction',
        maxWidth: 30,
        align: 'center',
    },
    {
        id: 'uuid',
        label: 'Uuid',
        maxWidth: 303,
        align: 'center',
    },
    {
        id: 'model',
        label: 'Model',
        maxWidth: 50,
        align: 'center',
    },
    {
        id: 'indexed',
        label: 'Indexovano do Solru',
        maxWidth: 70,
        align: 'center',
        format: (value) => value.toString(),
    },
    {
        id: 'stored',
        label: 'Uloženo do Fedory',
        maxWidth: 70,
        align: 'center',
        format: (value) => value.toString(),
    },
    {
        id: 'accessibilityInSolr',
        label: 'Přistup v Solru',
        maxWidth: 70,
        align: 'center',
    },
    {
        id: 'accessibilityInFedora',
        label: 'Přistup ve Fedoře',
        maxWidth: 100,
        align: 'center',
    },
    {
        id: 'imgUrl',
        label: 'URL obrázku',
        maxWidth: 170,
        align: 'center',
        format: (value) => value !== 'no image' ? <Link href={value} target={"_blank"}>link</Link> : value,
    },
    {
        id: 'rootTitle',
        label: 'Název',
        maxWidth: 170,
        align: 'center',
    },
    {
        id: 'solrModifiedDate',
        label: 'Datum modifikace v Solru',
        maxWidth: 100,
        align: 'center',
    },
    {
        id: 'fedoraModifiedDate',
        label: 'Datum modifikace ve Fedoře',
        maxWidth: 100,
        align: 'center',
    },
    {
        id: 'action',
        label: 'Akce',
        maxWidth: 100,
        align: 'center',
    },
];
