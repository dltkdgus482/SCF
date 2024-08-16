package com.scf.user.profile.domain.repository;

import com.scf.user.profile.domain.entity.Record;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RecordReposiotry extends JpaRepository<Record, Long> {

}
