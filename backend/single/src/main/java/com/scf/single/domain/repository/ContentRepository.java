package com.scf.single.domain.repository;

import com.scf.single.domain.entity.Content;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ContentRepository extends JpaRepository<Content, Integer> {

    // 주어진 ID로 콘텐츠가 존재하는지 확인
    Optional<Content> findById(int contentId);
}
