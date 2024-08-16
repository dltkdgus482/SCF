package com.scf.user.profile.application.service;

import com.scf.user.profile.domain.dto.DjangoResponseDto;
import com.scf.user.profile.domain.dto.HistoryListResponseDto;
import com.scf.user.profile.domain.dto.ProfileResponseDto;
import com.scf.user.profile.domain.dto.SubmitGameResultDto;
import com.scf.user.profile.domain.dto.SubmitGameResultListDto;
import com.scf.user.profile.domain.dto.kafka.BattleGameResult;
import com.scf.user.profile.domain.dto.kafka.MultiGameResult;
import com.scf.user.profile.domain.dto.kafka.SolvedProblemKafkaRequestDto;
import com.scf.user.profile.domain.dto.SolvedProblemsListDto;
import java.util.List;
import com.scf.user.profile.domain.dto.kafka.SolvedProblemKafkaRequestDto;
import com.scf.user.profile.domain.dto.SolvedProblemsListDto;

public interface ProfileService {

    // 프로필 정보 조회
    public ProfileResponseDto getProfileInfo(Long memberId);

    // 전체 전적 조회
    public HistoryListResponseDto getHistoryList(String memberId);

    // 푼 문제 리스트 조회
    public SolvedProblemsListDto getSolvedProblemsList(String memberId);

    // django에 보고서 생성 시 필요한 data 조회
    public DjangoResponseDto getDjangoInfo(String memberId);

    // 경험치를 update
    public void updateExp(Long memberId, Integer newExp);

    // 푼 문제를 등록
    void submitSolved(Long memberId, SolvedProblemKafkaRequestDto problemRequestDto);

    // 멀티 게임 결과를 저장
    void submitMultiGameResultList(MultiGameResult multiGameResult);

    // 배틀 게임 결과를 저장
    void submitBattleGameResultList(BattleGameResult battleGameResult);

    // multi게임 후 반영해줄 경험치 계산
    public Integer calculateMultiExp(int partCnt, int score, int rank);

    // 경험치를 update
    void updateExpoint(Long memberId, int addExp);

    // battle게임 후 반영해줄 경험치 계산
    List<Integer> calculateBattleExp(int win);

    // single 컨텐츠 학습 완료 후 경험치 갱신 -> 한 코스당 100씩 더해준다.
    void addSingleExp(Long memberId);

    // 경험치 조회
    public Integer getTotalExp(Long memberId);
}
