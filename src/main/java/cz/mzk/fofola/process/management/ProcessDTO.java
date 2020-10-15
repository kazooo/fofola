package cz.mzk.fofola.process.management;

import cz.mzk.fofola.process.constants.ProcessState;
import cz.mzk.fofola.process.constants.ProcessType;
import cz.mzk.fofola.process.constants.TerminationReason;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Date;


@Entity
@Getter
@Setter
@ToString
@EqualsAndHashCode
public class ProcessDTO {

    @Id
    private String id;
    private ProcessType type;
    private ProcessState state;
    private Date start;
    private Date finish;
    private TerminationReason terminationReason;
    private String notes;
}
