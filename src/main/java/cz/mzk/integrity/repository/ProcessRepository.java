package cz.mzk.integrity.repository;

import cz.mzk.integrity.model.Process;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProcessRepository extends JpaRepository<Process, Long> {

    public List<Process> findByProcessType(String type);
}
