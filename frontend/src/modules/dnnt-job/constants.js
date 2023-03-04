export const JobField = Object.freeze({
    Id: 'id',
    Title: 'title',
    Description: 'description',
    CronExpression: 'cronExpression',
    CronExpressionExplanation: 'cronExpressionExplanation',
    Active: 'active',
    Type: 'operation.type',
    Direction: 'operation.direction',
    Operation: 'operation.operation',
    Labels: 'operation.labels',
    Recursive: 'operation.recursive',
    From: 'operation.from',
    FromRelative: 'operation.fromRelative',
    RelativeFrom: 'operation.relativeFrom',
    To: 'operation.to',
    SolrQuery: 'operation.solrQuery',
    SolrFilterQuery: 'operation.solrFilterQuery',
    Created: 'created',
    LastExecution: 'lastExecution',
    NextExecution: 'nextExecution',
});

export const JobFormType = Object.freeze({
    Create: 'createJobForm',
    Update: 'updateJobForm',
});
