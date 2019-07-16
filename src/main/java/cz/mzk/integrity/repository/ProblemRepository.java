package cz.mzk.integrity.repository;

import cz.mzk.integrity.model.UuidProblemRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProblemRepository extends JpaRepository<UuidProblemRecord, Long> {

    public List<UuidProblemRecord> findByProcessId(long id);
    public List<UuidProblemRecord> findAll();
    public void deleteAll();
}
