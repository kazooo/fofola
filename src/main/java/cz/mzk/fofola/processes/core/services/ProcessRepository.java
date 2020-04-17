package cz.mzk.fofola.processes.core.services;

import cz.mzk.fofola.processes.core.models.ProcessDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProcessRepository extends JpaRepository<ProcessDTO, String> { }
