package com.scf.single.domain.repository;

import com.scf.single.domain.entity.Script;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ScriptRepository extends JpaRepository<Script, Integer> {

    // 컨텐츠 아이디에 맞는 script들 찾기
    List<Script> findByContent_ContentId(int contentId);

}
