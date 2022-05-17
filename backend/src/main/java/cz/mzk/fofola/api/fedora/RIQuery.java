package cz.mzk.fofola.api.fedora;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RIQuery {

    private String object;
    private String relation;
    private String subject;

    public static final String ANY = "*";
    public static final String PREFIX = "<info:fedora/";
    public static final String POSTFIX = ">";

    public String toString() {
        final StringBuilder builder = new StringBuilder();
        builder.append(object != null ? object : ANY);
        builder.append(" ");
        builder.append(relation != null ? relation : ANY);
        builder.append(" ");
        builder.append(subject != null ? subject : ANY);
        return builder.toString();
    }
}
