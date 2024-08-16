package com.scf.user.profile.domain.repository;

import com.scf.user.profile.domain.entity.Solved;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SolvedRepository extends JpaRepository<Solved, Long> {


}
