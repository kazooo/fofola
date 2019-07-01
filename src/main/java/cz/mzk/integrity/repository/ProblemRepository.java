package cz.mzk.integrity.repository;

import cz.mzk.integrity.model.UuidProblem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProblemRepository extends JpaRepository<UuidProblem, Long> {

    public List<UuidProblem> findByProcessId(long id);
}
