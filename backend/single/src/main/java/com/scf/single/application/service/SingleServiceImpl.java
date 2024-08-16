package com.scf.single.application.service;

import com.scf.single.application.Client.ProfileClient;
import com.scf.single.application.Client.UserClient;
import com.scf.single.domain.dto.ContentCompletionRequestDto;
import com.scf.single.domain.dto.ContentCreateRequestDto;
import com.scf.single.domain.dto.ContentDetailResponseDto;
import com.scf.single.domain.dto.ContentDetailResponsesDto;
import com.scf.single.domain.dto.ContentListResponseDto;
import com.scf.single.domain.dto.ContentListResponsesDto;
import com.scf.single.domain.dto.MemberDto;
import com.scf.single.domain.dto.ScriptCreateRequestListDto;
import com.scf.single.domain.dto.kafka.RenewExp;
import com.scf.single.domain.entity.Content;
import com.scf.single.domain.entity.ContentCheckUser;
import com.scf.single.domain.entity.Script;
import com.scf.single.domain.repository.ContentCheckUserRepository;
import com.scf.single.domain.repository.ContentRepository;
import com.scf.single.domain.repository.ScriptRepository;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class SingleServiceImpl implements SingleService {

    private final ContentCheckUserRepository contentCheckUserRepository;
    private final ScriptRepository scriptRepository;
    private final ContentRepository contentRepository;
    private final UserClient userClient;
    private final ProfileClient profileClient;
    private final KafkaMessageProducer kafkaMessageProducer;

    // content 목록 조회
    @Override
    public ContentListResponsesDto getUserContentList(Long memberId) {
        // memberId를 사용하여 content_check_user 테이블 조회
        List<ContentCheckUser> contentCheckUsers = contentCheckUserRepository.findByMemberId(
            memberId);

        // DTO로 변환
        List<ContentListResponseDto> contentList = contentCheckUsers.stream()
            .map(cu -> new ContentListResponseDto(cu.getContent().getContentId(), cu.getComplete()))
            .collect(Collectors.toList());
        return new ContentListResponsesDto((contentList));
    }

    @Override
    public ContentDetailResponsesDto getScripts(int contentId) {
        // contentId를 사용하여 script 테이블 조회
        List<Script> scripts = scriptRepository.findByContent_ContentId(contentId);

        // DTO로 변환
        List<ContentDetailResponseDto> contentScripts = scripts.stream()
            .map(script -> new ContentDetailResponseDto(script.getPageNo(),
                script.getScriptContent(), script.getAction(), script.getImageCount()))
            .collect(Collectors.toList());

        return new ContentDetailResponsesDto(contentScripts);
    }

    @Override
    @Transactional
    public void markContentAsCompleted(ContentCompletionRequestDto requestDto) {
        Long memberId = requestDto.getMemberId();
        int contentId = requestDto.getContentId();

        // 콘텐츠가 존재하는지 확인
        Content content = contentRepository.findById(contentId)
            .orElseThrow(() -> new IllegalArgumentException("콘텐츠를 찾을 수 없습니다."));

        // 사용자가 이미 수강 완료한 콘텐츠인지 확인
        Optional<ContentCheckUser> existingRecord = contentCheckUserRepository.findByMemberIdAndContent_ContentId(
            memberId, contentId);

        if (existingRecord.isPresent()) {
            // 이미 수강 완료된 콘텐츠인 경우
            ContentCheckUser record = existingRecord.get();
            if (record.getComplete() == 1) {
                throw new IllegalStateException("이미 수강 완료된 콘텐츠입니다.");
            } else {
                // 수강 미완료 상태인 경우 완료로 업데이트.
                // kafka user-exp 토픽으로 전송
                record.setComplete(1);

                // 경험치도 누적 시킵니다.
                profileClient.addExp(memberId);

                // 해당 유저의 전체 경험치를 조회해옵니다.
                Integer memberExp = profileClient.inquiryExp(memberId);

                // 유저 이름을 비동기적으로 가져오고 이후 작업 처리
                userClient.getUsername(memberId)
                    .doOnNext(userNameDto -> {
                        String memberName = userNameDto.getUsername();

                        // kafka 전송 (해당 코스)
                        RenewExp renewExp = new RenewExp(memberId, memberName, 100);
                        kafkaMessageProducer.sendProcessedSingleContents(renewExp);

                        // kafka 전송 (전체 exp)
                        RenewExp renewTotalExp = new RenewExp(memberId, memberName, memberExp);
                        kafkaMessageProducer.sendProcessedTotalExp(renewTotalExp);
                    })
                    .doOnError(e -> {
                        // 오류 처리
                        System.err.println(
                            "Error fetching username or sending Kafka message: " + e.getMessage());
                    })
                    .subscribe();

                contentCheckUserRepository.save(record);
            }
        }
        // 회원가입 이후로 새로 생긴 컨텐츠인 경우
        else {
            // 수강 완료 기록 생성
            ContentCheckUser newRecord = new ContentCheckUser();
            newRecord.setMemberId(memberId);
            newRecord.setComplete(1);  // 수강 완료 상태를 나타내는 값
            newRecord.setContent(content);

            profileClient.addExp(memberId); // 경험치 누적
            contentCheckUserRepository.save(newRecord);
        }
    }

    // 회원가입시 존재하는 컨텐츠에 대해 수강기록 0으로 초기화.
    @Override
    @Transactional
    public void initializeCompletionStatus(Long memberId) {
        List<Content> contents = contentRepository.findAll();
        for (Content content : contents) {
            ContentCheckUser contentCheckUser = new ContentCheckUser();
            contentCheckUser.setMemberId(memberId);
            contentCheckUser.setContent(content);
            contentCheckUser.setComplete(0);
            contentCheckUserRepository.save(contentCheckUser);
        }
    }

    // 개별 컨텐츠를 생성
    @Override
    public void createContent(ContentCreateRequestDto contentCreateRequestDto) {
        // 사용자가 입력한 컨텐츠를 생성합니다.
        Content content = new Content();
        content.setContentExp(contentCreateRequestDto.getContentExp());
        content.setTitle(contentCreateRequestDto.getTitle());
        content.setContentType(contentCreateRequestDto.getContentType());
        Content savedContent = contentRepository.save(content);

        // 회원 목록 조회 및 수강 상태 초기화
        userClient.getUserList().subscribe(userInfoListResponseDto -> {
            System.out.println(userInfoListResponseDto.getMembers());
            List<MemberDto> members = userInfoListResponseDto.getMembers();

            for (MemberDto member : members) {
                ContentCheckUser contentCheckUser = new ContentCheckUser();
                contentCheckUser.setMemberId(member.getMemberId());
                contentCheckUser.setContent(savedContent);
                contentCheckUser.setComplete(0);  // 초기 상태: 미수강
                contentCheckUserRepository.save(contentCheckUser);
            }
        });
    }

    // 스크립트를 생성
    @Override
    @Transactional
    public void createScript(ScriptCreateRequestListDto scriptCreateRequestListDto,
        int contentId) {
        // content 아이디를 찾습니다.
        Content content = contentRepository.findById(contentId)
            .orElseThrow(() -> new IllegalArgumentException("해당 콘텐츠를 찾을 수 없습니다."));

        // 해당 content에 script를 추가합니다.
        List<Script> scripts = scriptCreateRequestListDto.getScriptCreateList().stream()
            .map(scriptCreateRequestDto -> {
                Script script = new Script();
                script.setContent(content);
                script.setPageNo(scriptCreateRequestDto.getPageNo());
                script.setScriptContent(scriptCreateRequestDto.getScriptContent());
                script.setAction(script.getAction());
                script.setImageCount(scriptCreateRequestDto.getImageCount());
                return script;
            }).collect(Collectors.toList());

        // db에 저장
        scriptRepository.saveAll(scripts);
    }
}
