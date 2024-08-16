package com.scf.user.profile.domain.repository;

import com.scf.user.profile.domain.entity.Character;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CharacterRepository extends JpaRepository<Character, Long> {
    // memberId로 exp를 조회합니다.
    Character findByMemberId(Long memberId);

}
