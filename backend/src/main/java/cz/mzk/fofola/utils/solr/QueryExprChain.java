package cz.mzk.fofola.utils.solr;

public interface QueryExprChain {
    QueryExpr or();
    QueryExpr and();
    String build();
}
