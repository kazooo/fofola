package cz.mzk.fofola.repository;

import cz.mzk.fofola.model.pdf.AsyncPDFGenLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AsyncPDFGenLogRepository extends JpaRepository<AsyncPDFGenLog, Long> { }
