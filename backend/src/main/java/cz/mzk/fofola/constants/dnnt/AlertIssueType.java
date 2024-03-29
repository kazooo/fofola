package cz.mzk.fofola.constants.dnnt;

public enum AlertIssueType {
    SOLR_CHANGES_APPLYING_FAILURE,
    FEDORA_CHANGES_APPLYING_FAILURE,
    NO_CHANGE_REQUIRED,
    INTERNAL_NOT_FOUND,
    INTERNAL_NOT_FOUND_WHEN_ROLLBACK,
    FEDORA_CHANGES_APPLYING_FAILURE_WHEN_ROLLBACK,
    PREPARED_DOC_INCONSISTENCY,
    SOLR_IO_ERROR,
    FOXML_NOT_FOUND,
    FEDORA_IO_ERROR,
    FOXML_XPATHEXCEPTION,
    UNKNOWN_EXCEPTION,
    CANT_PREPARE_SESSION,
    SDNNT_ITEM_NO_UUID,
    SEARCH_CHILDREN_FAILURE,
    FLUSH_SOURCE_FAILURE,
    BATCH_PROCESSING_FAILURE,
}
