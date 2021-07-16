export const MONOGRAPH = "monograph";
export const PERIODICAL = "periodical";
export const PERIODICALVOLUME = "periodicalvolume";
export const PERIODICALITEM = "periodicalitem";
export const ARTICLE = "article";
export const GRAPHIC = "graphic";
export const MAP = "map";
export const PAGE = "page";

export const PUBLIC_ACCESS = "public";
export const PRIVATE_ACCESS = "private";

export const UUID = "PID";

export const models = [
    {
        value: MONOGRAPH,
        text: "monograph"
    },
    {
        value: PERIODICAL,
        text: "periodical"
    },
    {
        value: PERIODICALVOLUME,
        text: "periodicalvolume"
    },
    {
        value: PERIODICALITEM,
        text: "periodicalitem"
    },
    {
        value: ARTICLE,
        text: "article"
    },
    {
        value: MAP,
        text: "map"
    },
    {
        value: PAGE,
        text: "page"
    },
    {
        value: GRAPHIC,
        text: "graphic"
    }
];

export const accesses = [
    {
        value: PUBLIC_ACCESS,
        text: "public"
    },
    {
        value: PRIVATE_ACCESS,
        text: "private"
    }
];

export const fields = [
    {
        value: UUID,
        text: 'uuid',
    }
];

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
