package cz.mzk.fofola.utils.solr;

import java.util.function.Function;

public interface QueryExpr {
    QueryExprChain is(String fieldName, Object fieldValue, boolean asString);
    QueryExprChain isRegex(String fieldName, Object fieldValue);
    QueryExprChain not(String fieldName, Object fieldValue, boolean asString);
    QueryExprChain notRegex(String fieldName, Object fieldValue);
    QueryExprChain empty(String fieldName);
    QueryExprChain complex(Function<QueryExpr, QueryExprChain> exprConsumer);
    QueryExprChain andConcatenate(String... parts);
}
