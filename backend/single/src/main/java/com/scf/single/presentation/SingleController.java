package com.scf.single.presentation;

import com.scf.single.application.service.SingleService;
import com.scf.single.domain.dto.ContentCompletionRequestDto;
import com.scf.single.domain.dto.ContentCreateRequestDto;
import com.scf.single.domain.dto.ContentDetailResponsesDto;
import com.scf.single.domain.dto.ContentListResponsesDto;
import com.scf.single.domain.dto.ScriptCreateRequestListDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("single")
@Slf4j
@RequiredArgsConstructor
public class SingleController {

    private final SingleService singleService;

    // 전체 목록 조회
    @GetMapping
    public ResponseEntity<?> getList(@RequestHeader("memberId") Long memberId) {
        // 서비스 단에서 조회
        ContentListResponsesDto listResponsesDto = singleService.getUserContentList(memberId);

        return ResponseEntity.ok(listResponsesDto);

    }

    // contents 상세내용 조회 -> 회원만 content내용을 열람할 수 있도록 RequestHeader에 memberId가 있어야 열람 가능.
    @GetMapping("/{contentId}")
    public ResponseEntity<?> getSingle(@PathVariable("contentId") int contentId,
        @RequestHeader("memberId") Long memberId) {
        // 서비스 단에서 조회
        ContentDetailResponsesDto contentScript = singleService.getScripts(contentId);

        return ResponseEntity.ok(contentScript);
    }

    // 수강 완료 등록
    @PostMapping("/{contentId}")
    public ResponseEntity<?> markContentAsCompleted(@RequestHeader("memberId") Long memberId,
        @PathVariable("contentId") int contentId) {
        ContentCompletionRequestDto requestDto = new ContentCompletionRequestDto(memberId,
            contentId);
        try {
            // 수강 완료 처리
            singleService.markContentAsCompleted(requestDto);
            return ResponseEntity.ok("수강을 완료하셨습니다.");
        } catch (IllegalArgumentException e) {
            // 콘텐츠를 찾을 수 없는 경우
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (IllegalStateException e) {
            // 이미 수강 완료된 콘텐츠인 경우
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        } catch (Exception e) {
            // 기타 예외 처리
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("서버 오류가 발생했습니다.");
        }
    }

    // 수강여부 초기화
    @PostMapping("/public/{memberId}")
    public ResponseEntity<?> requestInitializeContent(@PathVariable("memberId") Long memberId) {
        singleService.initializeCompletionStatus(memberId);
        return ResponseEntity.ok().build();
    }

    // content 생성 api (개별)
    @PostMapping("/public/createcontent")
    public ResponseEntity<?> requestCreateContent(
        @RequestBody ContentCreateRequestDto contentCreateRequestDto) {
        singleService.createContent(contentCreateRequestDto);
        return ResponseEntity.ok().build();
    }


    // script 생성 api
    @PostMapping("/public/createscript/{contentId}")
    public ResponseEntity<?> requestCreateScript(
        @RequestBody ScriptCreateRequestListDto scriptCreateRequestListDto,
        @PathVariable("contentId") Integer contentId) {
        singleService.createScript(scriptCreateRequestListDto, contentId);
        return ResponseEntity.ok().body(contentId + "에 script 등록이 완료되었습니다.");
    }

}
