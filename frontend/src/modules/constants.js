export const DnntLabel = Object.freeze({
    DNNTO: {
        text: 'constant.sugo.label.dnnto.title',
        value: 'DNNTO',
    },
    DNNTT: {
        text: 'constant.sugo.label.dnntt.title',
        value: 'DNNTT',
    },
    COVID: {
        text: 'constant.sugo.label.covid.title',
        value: 'COVID',
    },
    LICENSE: {
        text: 'constant.sugo.label.license.title',
        value: 'LICENSE',
    }
});

export const DnntLinkingMode = Object.freeze({
    Link: 'link',
    Unlink: 'unlink',
    Synchronize: 'sync',
    Clean: 'clean'
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
    MONOGRAPHUNIT: {
        value: 'monographunit',
        text: 'monographunit',
    },
    CONVOLUTE: {
        value: 'convolute',
        text: 'convolute',
    },
    SUPPLEMENT: {
        value: 'supplement',
        text: 'supplement',
    },
    TRACK: {
        value: 'track',
        text: 'track',
    },
    SHEETMUSIC: {
        value: 'sheetmusic',
        text: 'sheetmusic',
    },
    SOUNDUNIT: {
        value: 'soundunit',
        text: 'soundunit',
    },
    INTERNALPART: {
        value: 'internalpart',
        text: 'internalpart',
    },
    SOUNDRECORDING: {
        value: 'soundrecording',
        text: 'soundrecording',
    },
    ARCHIVE: {
        value: 'archive',
        text: 'archive',
    },
    MANUSCRIPT: {
        value: 'manuscript',
        text: 'manuscript',
    },
    OLDPRINTSHEETMUSIC: {
        value: 'oldprintsheetmusic',
        text: 'oldprintsheetmusic',
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

export const ExtendedFieldValue = Object.freeze({
    ANY: {
        text: 'common.any',
        value: 'any',
    },
    NONE: {
        text: 'common.none',
        value: 'none',
    },
});

export const SugoSessionDirection = Object.freeze({
    Rest2Dst: {
        text: 'constant.sugo.direction.selectedInKramerius.title',
        value: 'SELECTED_IN_KRAMERIUS',
    },
    Src2Dst: {
        text: 'constant.sugo.direction.sdnntToKramerius.title',
        value: 'SDNNT_TO_KRAMERIUS',
    },
    Dst2Src: {
        text: 'constant.sugo.direction.krameriusToSdnnt.title',
        value: 'KRAMERIUS_TO_SDNNT',
    },
});

export const SugoSessionDirectionExtended = Object.freeze({
    ...SugoSessionDirection,
    Any: ExtendedFieldValue.ANY,
});

export const SugoSessionRequestor = Object.freeze({
    Auto: {
        text: 'constant.sugo.requestor.auto.title',
        value: 'AUTO',
    },
    Rest: {
        text: 'constant.sugo.requestor.rest.title',
        value: 'REST',
    },
    Any: ExtendedFieldValue.ANY,
});

export const SugoSessionOperation = Object.freeze({
    AddLabel: {
        text: 'constant.sugo.operation.add.title',
        value: 'ADD_LABELS',
    },
    RemoveLabel: {
        text: 'constant.sugo.operation.remove.title',
        value: 'REMOVE_LABELS',
    },
    Clean: {
        text: 'constant.sugo.operation.clean.title',
        value: 'CLEAN_LABELS',
    },
    Synchronize: {
        text: 'constant.sugo.operation.synchronize.title',
        value: 'SYNCHRONIZE',
    },
    Any: ExtendedFieldValue.ANY,
});

export const SugoSessionStatus = Object.freeze({
    NotReady: {
        text: 'constant.sugo.session.status.notReady.title',
        value: 'NOT_READY',
    },
    Active: {
        text: 'constant.sugo.session.status.active.title',
        value: 'ACTIVE',
    },
    Paused: {
        text: 'constant.sugo.session.status.paused.title',
        value: 'PAUSED',
    },
    Finished: {
        text: 'constant.sugo.session.status.finished.title',
        value: 'FINISHED',
    },
    CantPrepare: {
        text: 'constant.sugo.session.status.cantPrepare.title',
        value: 'CANT_PREPARE',
    },
    FinishedWithNegligibleIssues: {
        text: 'constant.sugo.session.status.finishedWithNegligibleIssues.title',
        value: 'FINISHED_WITH_NEGLIGIBLE_ISSUES',
    },
    FinishedWithSeriousIssues: {
        text: 'constant.sugo.session.status.finishedWithSeriousIssues.title',
        value: 'FINISHED_WITH_SERIOUS_ISSUES',
    },
    TerminatedByException: {
        text: 'constant.sugo.session.status.terminatedByException.title',
        value: 'TERMINATED_BY_EXCEPTION',
    },
    TerminatedByShutdown: {
        text: 'constant.sugo.session.status.terminatedByShutdown.title',
        value: 'TERMINATED_BY_SHUTDOWN',
    },
    Any: ExtendedFieldValue.ANY,
});

export const SugoJobType = Object.freeze({
    Full: {
       text: 'constant.sugo.jobType.full.title',
       value: 'FULL'
    },
    Changes: {
       text: 'constant.sugo.jobType.changes.title',
       value: 'CHANGES'
    },
    Custom: {
       text: 'constant.sugo.jobType.custom.title',
       value: 'CUSTOM'
    }
});

/* just arrays of objects from structures above */
export const dnntLabels = Object.keys(DnntLabel).map((key) => DnntLabel[key]);
export const validDnntLabels = dnntLabels.filter((label) => ![DnntLabel.COVID].includes(label));
export const models = Object.keys(Model).map((key) => Model[key]);
export const accesses = Object.keys(Access).map((key) => Access[key]);
export const solrFields = Object.keys(SolrField).map((key) => SolrField[key]);

export const sugoSessionDirections = Object.keys(SugoSessionDirection).map((key) => SugoSessionDirectionExtended[key]);
export const sugoSessionDirectionsExtended = Object.keys(SugoSessionDirectionExtended).map((key) => SugoSessionDirectionExtended[key]);
export const sugoSessionRequestors = Object.keys(SugoSessionRequestor).map((key) => SugoSessionRequestor[key]);
export const sugoSessionOperations = Object.keys(SugoSessionOperation).map((key) => SugoSessionOperation[key]);
export const sugoSessionStatuses = Object.keys(SugoSessionStatus).map((key) => SugoSessionStatus[key]);
export const sugoJobTypes = Object.keys(SugoJobType).map((key) => SugoJobType[key]);
