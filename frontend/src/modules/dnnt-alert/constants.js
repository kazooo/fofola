export const AlertField = Object.freeze({
    Id: 'id',
    Type: 'type',
    IssueType: 'issueType',
    Parameters: 'parameters',
    Document: 'document',
    Session: 'session',
    Solved: 'solved',
    Created: 'created',
});

export const AlertParameter = Object.freeze({
    SessionId: 'sessionId',
    Uuid: 'uuid',
    Path: 'path',
    ErrorMessage: 'errorMessage',
    ErrorMessageStackTrace: 'errorMessageStackTrace',
    SddntIdentifier: 'sddntIdentifier',
});

export const AlertType = Object.freeze({
    Error: 'ERROR',
    Warning: 'WARNING',
});

export const AlertTypeTranslationKey = Object.freeze({
    [AlertType.Error]: 'constant.sugo.alert.type.error.title',
    [AlertType.Warning]: 'constant.sugo.alert.type.warning.title',
});

export const AlertIssueType = Object.freeze({
    FoxmlNotFound: 'FOXML_NOT_FOUND',
    FoxmlXpathException: 'FOXML_XPATHEXCEPTION',
    FedoraIoError: 'FEDORA_IO_ERROR',
    SolrIoError: 'SOLR_IO_ERROR',
    PreparedDocInconsistency: 'PREPARED_DOC_INCONSISTENCY',
    InternalNotFoundWhenRollback: 'INTERNAL_NOT_FOUND_WHEN_ROLLBACK',
    InternalNotFound: 'INTERNAL_NOT_FOUND',
    FedoraChangesApplyingFailureWhenRollback: 'FEDORA_CHANGES_APPLYING_FAILURE_WHEN_ROLLBACK',
    NoChangesRequired: 'NO_CHANGES_REQUIRED',
    FedoraChangesApplyingFailure: 'FEDORA_CHANGES_APPLYING_FAILURE',
    SolrChangesApplyingFailure: 'SOLR_CHANGES_APPLYING_FAILURE',
    UnknownException: 'UNKNOWN_EXCEPTION',
    CantPrepareSession: 'CANT_PREPARE_SESSION',
    SdnntItemNoUuid: 'SDNNT_ITEM_NO_UUID',
    SearchChildrenFailure: 'SEARCH_CHILDREN_FAILURE',
    FlushSourceFailure: 'FLUSH_SOURCE_FAILURE',
    BatchProcessingFailure: 'BATCH_PROCESSING_FAILURE',
});

export const AlertIssueTypeTranslationKey = Object.freeze({
    [AlertIssueType.FoxmlNotFound]: 'foxmlNotFound',
    [AlertIssueType.FoxmlXpathException]: 'foxmlXpathException',
    [AlertIssueType.FedoraIoError]: 'fedoraIoError',
    [AlertIssueType.SolrIoError]: 'solrIoError',
    [AlertIssueType.PreparedDocInconsistency]: 'preparedDocInconsistency',
    [AlertIssueType.InternalNotFoundWhenRollback]: 'internalNotFoundWhenRollback',
    [AlertIssueType.InternalNotFound]: 'internalNotFound',
    [AlertIssueType.FedoraChangesApplyingFailureWhenRollback]: 'fedoraChangesApplyingFailureWhenRollback',
    [AlertIssueType.NoChangesRequired]: 'noChangesRequired',
    [AlertIssueType.FedoraChangesApplyingFailure]: 'fedoraChangesApplyingFailure',
    [AlertIssueType.SolrChangesApplyingFailure]: 'solrChangesApplyingFailure',
    [AlertIssueType.UnknownException]: 'unknownException',
    [AlertIssueType.CantPrepareSession]: 'cantPrepareSession',
    [AlertIssueType.SdnntItemNoUuid]: 'sdnntItemNoUuid',
    [AlertIssueType.SearchChildrenFailure]: 'searchChildrenFailure',
    [AlertIssueType.FlushSourceFailure]: 'flushSourceFailure',
    [AlertIssueType.BatchProcessingFailure]: 'batchProcessingFailure',
});

export const DocumentField = Object.freeze({
    Uuid: 'uuid',
    Path: 'path',
    Title: 'title',
    Root: 'root',
    PreviousState: 'previousState',
    CurrentState: 'currentState',
});
