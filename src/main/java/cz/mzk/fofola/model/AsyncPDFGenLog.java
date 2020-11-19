package cz.mzk.fofola.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@Getter
@Setter
@ToString
@EqualsAndHashCode
public class AsyncPDFGenLog {
    @Id
    private String id;
    private String name;
    private String uuid;
    private String handle;
}
