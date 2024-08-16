package com.scf.single.application.service;

import com.scf.single.domain.dto.ContentCompletionRequestDto;
import com.scf.single.domain.dto.ContentCreateRequestDto;
import com.scf.single.domain.dto.ContentDetailResponsesDto;
import com.scf.single.domain.dto.ContentListResponsesDto;
import com.scf.single.domain.dto.ScriptCreateRequestListDto;
import org.springframework.stereotype.Service;

@Service
public interface SingleService {

    // 각 유저의 content 목록 리스트 조회
    ContentListResponsesDto getUserContentList(Long memberId);

    // 각 content별 script 조회
    ContentDetailResponsesDto getScripts(int contentId);

    // 각 content 수강 등록
    void markContentAsCompleted(ContentCompletionRequestDto requestDto);

    // 수강 여부를 초기화
    void initializeCompletionStatus(Long memberId);

    // content 생성 -> 하나씩 추가
    void createContent(ContentCreateRequestDto contentCreateRequestDto);

    // script 생성
    void createScript(ScriptCreateRequestListDto scriptCreateRequestListDto, int contentId);

}
