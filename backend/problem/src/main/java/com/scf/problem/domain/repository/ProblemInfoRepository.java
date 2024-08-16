package com.scf.problem.domain.repository;

import com.scf.problem.domain.model.ProblemInfo;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProblemInfoRepository extends JpaRepository<ProblemInfo, Long> {

    ProblemInfo findByProblemId(Long problemId);

    List<ProblemInfo> findAll();
}
