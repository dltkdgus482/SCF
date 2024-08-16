package com.scf.single.domain.repository;

import com.scf.single.domain.entity.ContentCheckUser;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ContentCheckUserRepository extends JpaRepository<ContentCheckUser, Long> {

    // 수강 여부 조회
    List<ContentCheckUser> findByMemberId(Long memberId);

    // 사용자가 특정 콘텐츠를 완료했는지 확인
    Optional<ContentCheckUser> findByMemberIdAndContent_ContentId(Long memberId, Integer contentId);
}
