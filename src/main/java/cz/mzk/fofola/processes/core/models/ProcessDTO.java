package cz.mzk.fofola.processes.core.models;

import cz.mzk.fofola.processes.core.constants.TerminationReason;
import cz.mzk.fofola.processes.core.constants.ProcessState;
import cz.mzk.fofola.processes.core.constants.ProcessType;
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
    private String processId;
    private ProcessType processType;
    private ProcessState processState;
    private Date startDate;
    private Date finishDate;
    private TerminationReason terminationReason;
    private String notes;
}
