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

export const dnntLabels = Object.keys(DnntLabel).map((key) => DnntLabel[key]);
