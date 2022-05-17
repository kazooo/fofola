package cz.mzk.fofola.api.fedora;

import lombok.Data;

@Data
public class RITriple {
    private String object;
    private String relation;
    private String subject;

    public RITriple(final String o, final String r, final String s) {
        object = unwrap(o);
        relation = r;
        subject = unwrap(s);
    }

    private String unwrap(final String s) {
        String copy = s;
        copy = copy.replace(RIQuery.PREFIX, "");
        copy = copy.replace(RIQuery.POSTFIX, "");
        return copy;
    }
}
