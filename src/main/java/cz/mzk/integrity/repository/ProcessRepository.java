package cz.mzk.integrity.repository;

import cz.mzk.integrity.model.CheckProcess;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProcessRepository extends JpaRepository<CheckProcess, Long> {

    public List<CheckProcess> findByProcessType(String type);

    public CheckProcess findFirstByProcessType(String type);
}
