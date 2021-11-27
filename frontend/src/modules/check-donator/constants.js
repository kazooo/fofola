export const CHECK_HAS_DONATOR = "CHECK_HAS_DONATOR";
export const CHECK_HASNT_DONATOR = "CHECK_HASN_DONATOR";

export const EODOPEN = "eodopen";
export const NORWAY = "norway";
export const ILNORWAY = "ilnorway";

export const donators = [
    {
        value: EODOPEN,
        text: "EODOPEN"
    },
    {
        value: NORWAY,
        text: "Norway Grants"
    },
    {
        value: ILNORWAY,
        text: "Iceland Liechtenstein Norway Grants"
    },
];

export const modes = [
    {
        value: CHECK_HAS_DONATOR,
        text: "má donátora"
    },
    {
        value: CHECK_HASNT_DONATOR,
        text: "nemá donátora"
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
