package cz.mzk.fofola.utils.solr;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.function.Function;

public class SolrQueryBuilder implements QueryExpr, QueryExprChain {
    private final StringBuilder expr = new StringBuilder();

    private SolrQueryBuilder() {}

    @Override
    public QueryExprChain is(String fieldName, Object fieldValue, boolean asString) {
        expr.append(asString ? fieldName + ":\"" + fieldValue + "\"" : fieldName + ":" + fieldValue);
        return this;
    }

    @Override
    public QueryExprChain isRegex(String fieldName, Object fieldValue) {
        expr.append(fieldName).append(":/.*").append(fieldValue).append(".*/");
        return this;
    }

    @Override
    public QueryExprChain not(String fieldName, Object fieldValue, boolean asString) {
        expr.append("-");
        return is(fieldName, fieldValue, asString);
    }

    @Override
    public QueryExprChain notRegex(String fieldName, Object fieldValue) {
        expr.append("-").append(fieldName).append(":/.*").append(fieldValue).append(".*/");
        return this;
    }

    @Override
    public QueryExprChain empty(String fieldName) {
        expr.append("-").append(fieldName).append(":*");
        return this;
    }

    @Override
    public QueryExprChain complex(Function<QueryExpr, QueryExprChain> exprConsumer) {
        expr.append("(").append(exprConsumer.apply(new SolrQueryBuilder()).build()).append(")");
        return this;
    }

    @Override
    public QueryExprChain andConcatenate(String... parts) {
        for (String part : parts) {
            if (part != null) {
                expr.append(" AND ").append("(").append(part).append(")");
            }
        }
        return this;
    }

    @Override
    public String build() {
        return expr.toString();
    }

    @Override
    public QueryExpr or() {
        expr.append(" OR ");
        return this;
    }

    @Override
    public QueryExpr and() {
        expr.append(" AND ");
        return this;
    }

    private static final DateTimeFormatter sdf = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
    public static final String ROOT_ONLY_FILTER_QUERY = String.format(
            "{!frange l=1 u=1 v=eq(%s,%s)}",
            SolrSearchField.UUID, SolrSearchField.ROOT_UUID
    );

    public static SolrQueryBuilder start() {
        return new SolrQueryBuilder();
    }

    public static String range(LocalDateTime from, LocalDateTime to) {
        String formattedFrom = (from == null) ? SolrCommonField.ALL : sdf.format(from);
        String formattedTo = (to == null) ? SolrCommonField.ALL : sdf.format(to);
        return "[" + formattedFrom + " TO " + formattedTo + "]";
    }
}
