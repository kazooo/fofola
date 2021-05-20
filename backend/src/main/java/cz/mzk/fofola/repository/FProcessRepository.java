package cz.mzk.fofola.repository;

import cz.mzk.fofola.model.process.ProcessDTO;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FProcessRepository extends JpaRepository<ProcessDTO, String> { }
