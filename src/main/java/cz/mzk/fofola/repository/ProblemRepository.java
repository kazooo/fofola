package cz.mzk.fofola.repository;

import cz.mzk.fofola.model.UuidProblemRecord;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProblemRepository extends JpaRepository<UuidProblemRecord, Long> {

    List<UuidProblemRecord> findAllByProcessId(long id, Pageable pageable);
    List<UuidProblemRecord> findByProcessId(long id);
    Page<UuidProblemRecord> findAll(Pageable pageable);
    void deleteAll();
}
