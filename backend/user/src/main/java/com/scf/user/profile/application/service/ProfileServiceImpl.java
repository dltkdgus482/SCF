package com.scf.user.profile.application.service;

import com.scf.user.member.domain.dto.UserCharacterResponseDTO;
import com.scf.user.member.domain.entity.Member;
import com.scf.user.member.domain.repository.UserRepository;
import com.scf.user.profile.application.client.ProblemClient;
import com.scf.user.profile.domain.dto.ChoiceTextConverter;
import com.scf.user.profile.domain.dto.DjangoResponseDto;
import com.scf.user.profile.domain.dto.HistoryListResponseDto;
import com.scf.user.profile.domain.dto.HistoryResponseDto;
import com.scf.user.profile.domain.dto.ProblemResponseDto;
import com.scf.user.profile.domain.dto.ProblemType;
import com.scf.user.profile.domain.dto.ProfileResponseDto;
import com.scf.user.profile.domain.dto.kafka.BattleGameResult;
import com.scf.user.profile.domain.dto.kafka.MultiGameResult;
import com.scf.user.profile.domain.dto.kafka.Rank;
import com.scf.user.profile.domain.dto.kafka.SolvedProblemKafkaRequestDto;
import com.scf.user.profile.domain.dto.SolvedProblemResponseDto;
import com.scf.user.profile.domain.dto.SolvedProblemsListDto;
import com.scf.user.profile.domain.entity.Character;
import com.scf.user.profile.domain.entity.Record;
import com.scf.user.profile.domain.entity.Solved;
import com.scf.user.profile.domain.repository.CharacterRepository;
import com.scf.user.profile.domain.repository.SolvedRepository;
import com.scf.user.profile.domain.repository.RecordReposiotry;
import com.scf.user.profile.global.exception.ProblemNotFoundException;
import jakarta.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import static com.scf.user.member.application.service.GachaService.characterTypes;
import static com.scf.user.member.application.service.GachaService.clothingTypes;

@Service
@RequiredArgsConstructor
public class ProfileServiceImpl implements ProfileService {

    private final UserRepository userRepository;
    private final SolvedRepository solvedRepository;
    private final RecordReposiotry recordReposiotry;
    private final CharacterRepository characterRepository;
    private final ProblemClient problemClient;

    // 멤버 조회 메소드 추가
    private Member getMemberById(Long memberId) {
        return userRepository.findById(memberId)
            .orElseThrow(() -> new RuntimeException("유저를 찾을 수 없습니다. ID: " + memberId));
    }

    // 프로필 정보 조회
    @Override
    public ProfileResponseDto getProfileInfo(Long memberId) {
        // 멤버 정보 조회
        Member member = getMemberById(memberId);

        Character character = member.getCharacter();

        // characterType * 100 + characterCloth 값을 계산
        int characterValue = character.getCharacterType() * 100 + character.getCharacterCloth();
        String characterType = getRarityFromCharacterType(character.getCharacterType());
        String characterClothType = getRarityFromClothType(character.getCharacterCloth());

        // 프로필 DTO 생성
        return ProfileResponseDto.builder()
            .name(member.getName())
            .birth(member.getBirth())
            .exp(character.getExp())
            .UserCharacter(new UserCharacterResponseDTO(characterValue, characterType, characterClothType)) // 계산된 값 설정 및
            .school(member.getSchoolName())
            .build();
    }

    // Rarity를 가져오는 헬퍼 메소드들 추가
    private String getRarityFromCharacterType(int type) {
        return characterTypes.stream()
                .filter(ct -> ct.getType() == type)
                .map(ct -> ct.getRarity().name())
                .findFirst()
                .orElse("UNKNOWN");
    }

    private String getRarityFromClothType(int type) {
        return clothingTypes.stream()
                .filter(ct -> ct.getType() == type)
                .map(ct -> ct.getRarity().name())
                .findFirst()
                .orElse("UNKNOWN");
    }

    // 전체 전적 조회
    @Override
    public HistoryListResponseDto getHistoryList(String memberId) {
        // 멤버 정보 조회
        Member member = getMemberById(Long.parseLong(memberId));

        // 회원이 가지고 있는 게임 결과기록들 중 상위 10개만 listDTO에 담아서 return시킨다.
        List<HistoryResponseDto> historyList = member.getRecords().stream()
            .sorted((r1, r2) -> Integer.compare(r2.getScore(), r1.getScore())) // 우선 점수로 정렬
            .limit(10) // 상위 10개
            .map(record -> HistoryResponseDto.from(
                record.getTime(), // Record의 시간 필드를 LocalDateTime으로 변환
                record.getRanking(),
                record.getScore(),
                record.getGameType(),
                record.getPartCnt()))
            .collect(Collectors.toList());
        return new HistoryListResponseDto(historyList);
    }

    // 푼 문제 리스트 조회
    // 이때, 문제 정보가 필요하므로 문제 서버에서 데이터를 받아서 dto에 넣어준다.
    @Override
    public SolvedProblemsListDto getSolvedProblemsList(String memberId) {
        // 멤버 정보 조회
        Member member = getMemberById(Long.parseLong(memberId));

        // 유저가 푼 문제 리스트를 가져옴
        List<Solved> solvedProblems = member.getSolvedProblems();

        // 문제 정보를 가져오고 DTO로 변환
        List<SolvedProblemResponseDto> solvedProblemResponses = solvedProblems.stream()
            .map(solvedProblem -> {
                // 문제 서버에서 문제 정보를 가져옴
                ProblemResponseDto problemInfo = problemClient.getProblemById(
                        (long) solvedProblem.getProblemId())
                    .block(); // Mono를 동기식으로 처리

                if (problemInfo != null) {
                    return SolvedProblemResponseDto.builder()
                        .solvedId(solvedProblem.getSolvedId())
                        .isCorrect(solvedProblem.isCorrect())
                        .choice(solvedProblem.getChoice())
                        .title(problemInfo.getTitle())
                        .type(problemInfo.getProblemType())
                        .category(problemInfo.getCategory())
                        .difficulty(problemInfo.getDifficulty())
                        .problemContent(problemInfo.getProblemContent())
                        .problemChoices(problemInfo.getProblemChoices())
                        .problemAnswers(problemInfo.getProblemAnswers())
                        .build();
                } else {
                    // 문제 정보를 가져오지 못했을 때 예외 발생
                    throw new ProblemNotFoundException(
                        "문제 정보를 가져올 수 없습니다. 문제 ID: " + solvedProblem.getProblemId());
                }
            })
            .collect(Collectors.toList());

        return new SolvedProblemsListDto(solvedProblemResponses);
    }

    // django 보고서 데이터 요청
    @Override
    public DjangoResponseDto getDjangoInfo(String memberId) {
        // 멤버 정보 조회
        Member member = getMemberById(Long.parseLong(memberId));

        // 멤버가 푼 문제들 리스트를 가져오기
        SolvedProblemsListDto solvedList = getSolvedProblemsList(memberId);

        // 멤버의 게임 기록에서 등수 정보 가져오기
        List<Record> records = member.getRecords();

        // 평균 랭킹 계산
        int totalRank = records.stream()
            .mapToInt(Record::getRanking)
            .sum();
        int averageRank = records.isEmpty() ? 0 : totalRank / records.size();

        // solvedProblemDto 리스트 생성
        List<DjangoResponseDto.solvedProblemDto> solvedProblemDtoList = solvedList.getSolvedList()
            .stream()
            .map(solved -> DjangoResponseDto.solvedProblemDto.builder()
                .isCorrect(solved.isCorrect())
                .category(solved.getCategory())
                .difficulty(solved.getDifficulty())
                .build())
            .collect(Collectors.toList());

        // DjangoResponseDto 빌드하여 반환
        return DjangoResponseDto.builder()
            .averageRank(averageRank)
            .solvedCount(solvedProblemDtoList.size())
            .list(solvedProblemDtoList)
            .build();
    }

    // 경험치 업데이트
    @Override
    public void updateExp(Long memberId, Integer newExp) {
        // 멤버 정보 조회
        Member member = getMemberById(memberId);

        // 새로운 경험치로 업데이트
        member.getCharacter().setExp(newExp);

        // 변경사항을 저장
        userRepository.save(member);

        System.out.println("경험치가 업데이트 되었습니다. " + memberId + ": " + newExp);
    }

    @Override
    @Transactional
    public void submitSolved(Long memberId, SolvedProblemKafkaRequestDto problemRequestDto) {
        // 멤버 정보 조회
        Member member = getMemberById(memberId);

        // 문제 아이디로 문제 조회해서 type 추출
        // 문제 정보
        ProblemResponseDto problemInfo = problemClient.getProblemById(
                problemRequestDto.getProblemId())
            .block(); // Mono를 동기식으로 처리

        // 문제의 type
        ProblemType problemType = problemInfo.getProblemType();

        String convertedChoiceText = null;

        // 문제의 타입에 따라서 세분화.
        // 단답식일 경우
        if (problemType.equals(ProblemType.SHORT_ANSWER_QUESTION)) {
            convertedChoiceText = ChoiceTextConverter.ShortAnswer(problemRequestDto.getChoice());
        }
        // 빈칸일 경우
        if (problemType.equals(ProblemType.FILL_IN_THE_BLANK)) {
            convertedChoiceText = ChoiceTextConverter.FillInBlank(
                problemRequestDto.getChoiceText());
        }
        // 객관식인 경우
        if (problemType.equals(ProblemType.MULTIPLE_CHOICE)) {
            convertedChoiceText = ChoiceTextConverter.MultipleChoice(
                problemRequestDto.getChoiceText());
        }

        //Solved 엔티티 생성
        Solved solved = new Solved();
        solved.setChoice(convertedChoiceText);
        solved.setCorrect(problemRequestDto.isCorrect());
        solved.setProblemId(
            problemRequestDto.getProblemId());
        solved.setMember(member);

        // Solved 엔티티 저장
        solvedRepository.save(solved);

    }

    @Override
    @Transactional
    public void submitMultiGameResultList(MultiGameResult multiGameResult) {
        // 게임결과를 저장합니다.
        // 게임 진행 시간
        LocalDateTime localDateTime = multiGameResult.getLocalDateTime();
        // 게임 참여 인원
        Integer partCnt = multiGameResult.getGameRank().size();
        // 각 유저의 랭킹
        List<Rank> gameRank = multiGameResult.getGameRank();

        // 랭킹 리스트의 길이만큼 돌면서 각 유저의 게임 결과를 저장합니다.
        for (Rank rank : gameRank) {
            // 멤버 정보 조회
            Member member = getMemberById(rank.getUserId());
            // 게임에서 얻은 경험치를 조회합니다.
            Integer gameExp = calculateMultiExp(partCnt, rank.getScore(), rank.getRank());

            Record record = new Record();
            record.setGameType(0); // multi
            record.setRanking(rank.getRank());
            record.setPartCnt(partCnt);
            record.setTime(localDateTime);
            record.setMember(member);
            record.setScore(gameExp);

            recordReposiotry.save(record);
        }

    }

    @Override
    public void submitBattleGameResultList(BattleGameResult battleGameResult) {
        // 게임 진행 시간
        LocalDateTime localDateTime = battleGameResult.getGameDateTime();
        // playerAId
        Long playerAId = battleGameResult.getPlayerAId();
        // playerBId
        Long playerBId = battleGameResult.getPlayerBId();
        // playerA 조회
        Member playerA = getMemberById(playerAId);
        // playerB 조회
        Member playerB = getMemberById(playerBId);
        // 게임 결과
        Integer result = battleGameResult.getResult();
        // 보너스 결과
        List<Integer> players = calculateBattleExp(result);

        Integer ARank = 0;
        Integer BRank = 0;

        //  무승부 -> 0, A이김 -> 1, B이김 -> 2
        if (result == 0) {
            ARank = 1;
            BRank = 1;
        } else if (result == 1) {
            ARank = 1;
            BRank = 2;
        } else if (result == 2) {
            ARank = 2;
            BRank = 1;
        }

        // playerA
        Record Arecord = new Record();
        Arecord.setGameType(1);
        Arecord.setRanking(ARank);
        Arecord.setTime(localDateTime);
        Arecord.setPartCnt(2);
        Arecord.setMember(playerA);
        Arecord.setScore(players.get(0));
        recordReposiotry.save(Arecord);

        // playerB
        Record Brecord = new Record();
        Brecord.setGameType(1);
        Brecord.setRanking(BRank);
        Brecord.setTime(localDateTime);
        Brecord.setPartCnt(2);
        Brecord.setMember(playerB);
        Brecord.setScore(players.get(1));

        recordReposiotry.save(Brecord);
    }

    // multi 모드 경험치 계산
    @Override
    public Integer calculateMultiExp(int partCnt, int score, int rank) {
        // 기본 경험치
        int baseExp = 500 * partCnt;

        // 순위 보너스: 1등에게는 30%, 2등에게는 20%, 3등에게는 10% 보너스
        // 4등부터는 보너스 없음
        int rankBonusPercentage;
        if (rank == 1) {
            rankBonusPercentage = 30;
        } else if (rank == 2) {
            rankBonusPercentage = 20;
        } else if (rank == 3) {
            rankBonusPercentage = 10;
        } else {
            rankBonusPercentage = 0;
        }

        // 순위 보너스 계산
        int rankBonus = (baseExp * rankBonusPercentage) / 200;

        int expGain = baseExp + rankBonus;

        return Math.max(expGain, 0);
    }

    // 사용자의 경험치를 누적하여 db에 저장
    @Override
    public void updateExpoint(Long memberId, int addExp) {
        // 사용자를 조회합니다.
        Integer currentExp = getProfileInfo(memberId).getExp();
        Integer newExp = currentExp + addExp;

        updateExp(memberId, newExp);
    }

    // battle 모드 경험치 계산
    @Override
    public List<Integer> calculateBattleExp(int win) {
        List<Integer> returnExp = new ArrayList<>();

        // 기본 경험치
        int baseExp = 1000; // 기본경험치

        // 순위 보너스: 1등에게는 50%, 2등에게는 10% 보너스
        int aRankBonusPercentage;
        int bRankBonusPercentage;

        // a win
        if (win == 1) {
            aRankBonusPercentage = 50;
            bRankBonusPercentage = 10;

        }
        // b win
        else if (win == 2) {
            aRankBonusPercentage = 10;
            bRankBonusPercentage = 50;
        }
        // 무승부 (rank == 0)
        else {
            aRankBonusPercentage = 20;
            bRankBonusPercentage = 20;
        }

        // 순위 보너스 계산
        int aRankBonus = (baseExp * aRankBonusPercentage) / 100;
        int bRankBonus = (baseExp * bRankBonusPercentage) / 100;

        int aExpGain = baseExp + aRankBonus;
        int bExpGain = baseExp + bRankBonus;

        returnExp.add(aExpGain);
        returnExp.add(bExpGain);
        return returnExp;
    }

    @Override
    @Transactional
    public void addSingleExp(Long memberId) {
        // 해당 요청이 들어오면 유저의 기존 경험치에서 500씩 더해줍니다.

        // memberId로 Character 조회
        Character character = characterRepository.findByMemberId(memberId);

        if (character != null) {
            // 기존 경험치에 100을 더함
            character.setExp(character.getExp() + 500);
            // 변경된 값 저장
            characterRepository.save(character);
        } else {
            throw new IllegalArgumentException("해당 memberId를 가진 캐릭터가 존재하지 않습니다.");
        }

    }

    @Override
    public Integer getTotalExp(Long memberId) {
        // 해당 유저의 전체 exp 조회
        Character character = characterRepository.findByMemberId(memberId);
        Integer memberExp = character.getExp();
        return memberExp;
    }

}
