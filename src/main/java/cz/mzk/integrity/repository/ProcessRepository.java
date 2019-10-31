package cz.mzk.integrity.repository;

import cz.mzk.integrity.model.FofolaProcess;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProcessRepository extends JpaRepository<FofolaProcess, Long> {

    public List<FofolaProcess> findByProcessType(String type);

    public FofolaProcess findFirstByProcessType(String type);
}
