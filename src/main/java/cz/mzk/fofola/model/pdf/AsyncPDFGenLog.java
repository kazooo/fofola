package cz.mzk.fofola.model.pdf;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.Date;

@Entity
@Getter
@Setter
@ToString
@EqualsAndHashCode
public class AsyncPDFGenLog {
    @Id @GeneratedValue
    private Long id;
    private Date date;
    private String name;
    private String uuid;
    private String handle;
    private PDFGenState state;
}
