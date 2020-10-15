package cz.mzk.fofola.process.management;

import org.springframework.data.jpa.repository.JpaRepository;

public interface FProcessRepository extends JpaRepository<ProcessDTO, String> { }
