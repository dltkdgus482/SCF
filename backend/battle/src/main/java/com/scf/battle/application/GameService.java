package com.scf.battle.application;

import com.scf.battle.domain.dto.Problem.Problem;
import com.scf.battle.domain.dto.Problem.ProblemAnswer;
import com.scf.battle.domain.dto.Problem.ProblemChoice;
import com.scf.battle.domain.dto.Problem.ProblemRequestDTO;
import com.scf.battle.domain.dto.User.FightDTO;
import com.scf.battle.domain.dto.User.Solved;
import com.scf.battle.domain.enums.ProblemType;
import com.scf.battle.domain.model.BattleGameRoom;
import com.scf.battle.domain.repository.BattleGameRepository;
import com.scf.battle.global.error.ErrorCode;
import com.scf.battle.global.error.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class GameService {

    private final BattleGameRepository battleGameRepository;
    private final ProblemService problemService;
    public List<Problem> startGame(Long userId, String roomId) {
        BattleGameRoom room = battleGameRepository.findById(roomId);
        if (room == null) {
            throw new BusinessException(roomId, "roomId", ErrorCode.ROOM_NOT_FOUND);
        }
        if (room.getIsStart()) {
            throw new BusinessException(roomId, "roomId", ErrorCode.GAME_ALREADY_STARTED);
        }
        List<Problem> problems = problemService.getProblems(room.getFinalRound() * 3);
        if (problems.isEmpty()) {
            throw new BusinessException(room, "roomId", ErrorCode.PROBLEM_NOT_FOUND);
        }
        room.startGame(problems, userId);
        return problems;
    }

    public FightDTO markSolution(String roomId, Long userId, ProblemRequestDTO problemRequestDTO) {
        BattleGameRoom room = battleGameRepository.findById(roomId);
        if (room == null) {
            throw new BusinessException(roomId, "roomId", ErrorCode.ROOM_NOT_FOUND);
        }
        List<Problem> problems = room.getProblemsForRound(problemRequestDTO.getRound());
        if (problems == null || problems.isEmpty()) {
            throw new BusinessException(String.valueOf(problemRequestDTO.getRound()), "round", ErrorCode.NO_PROBLEM_FOR_ROUND);
        }
        Optional<Problem> problemOpt = problems.stream()
                .filter(p -> p.getProblemId().equals(problemRequestDTO.getProblemId()))
                .findFirst();
        if (!problemOpt.isPresent()) {
            throw new BusinessException(problemRequestDTO.getProblemId().toString(), "problemId", ErrorCode.PROBLEM_NOT_FOUND);
        }
        Problem problem = problemOpt.get();
        List<ProblemAnswer> answers = problem.getProblemAnswers();
        ProblemType problemType = problem.getProblemType();

        boolean isCorrect = compareAnswers(problemType, problemRequestDTO, answers);
        Solved solved = buildSolved(problemRequestDTO, isCorrect);
        updatePlayerSolvedList(room, solved);
        int score = isCorrect ? calculateScore(solved.getSubmitTime()) : 0;
        return room.updateHp(userId, score);
    }

    public void updatePlayerSolvedList(BattleGameRoom room, Solved solved) {
        Long curruntUserId = solved.getUserId();
        if (room.getPlayerA().getUserId().equals(curruntUserId)) {
            room.getPlayerA().addSolved(solved);
        } else if (room.getPlayerB().getUserId().equals(curruntUserId)) {
            room.getPlayerB().addSolved(solved);
        }
    }
    public Solved buildSolved(ProblemRequestDTO problemRequestDTO, boolean isCorrect) {
        return Solved.builder()
            .problemId(problemRequestDTO.getProblemId())
            .userId(problemRequestDTO.getUserId())
            .solve(problemRequestDTO.getSolve())
            .solveText(problemRequestDTO.getSolveText())
            .submitTime(problemRequestDTO.getSubmitTime())
            .round(problemRequestDTO.getRound())
            .isCorrect(isCorrect)
            .build();
    }

    private boolean compareAnswers(ProblemType problemType, ProblemRequestDTO problemRequestDTO, List<ProblemAnswer> answers) {
        switch (problemType) {
            case MULTIPLE_CHOICE:
                return compareMultipleChoiceAnswers(problemRequestDTO, answers);
            case SHORT_ANSWER_QUESTION:
                return compareShortAnswer(problemRequestDTO, answers);
            case FILL_IN_THE_BLANK:
                return compareFillInTheBlank(problemRequestDTO, answers);
            default:
                throw new IllegalArgumentException("Unknown ProblemType: " + problemType);
        }
    }

    private boolean compareMultipleChoiceAnswers(ProblemRequestDTO problemRequestDTO, List<ProblemAnswer> answers) {
        Map<Integer, Integer> solve = problemRequestDTO.getSolve();
        ProblemChoice correctChoice = answers.get(0).getCorrectChoice();
        return correctChoice.getChoiceId().equals(solve.get(1));
    }

    private boolean compareShortAnswer(ProblemRequestDTO problemRequestDTO, List<ProblemAnswer> answers) {
        String correctAnswerText = answers.get(0).getCorrectAnswerText();
        return correctAnswerText.equals(problemRequestDTO.getSolveText());
    }

    private boolean compareFillInTheBlank(ProblemRequestDTO problemRequestDTO, List<ProblemAnswer> answers) {
        Map<Integer, Integer> solve = problemRequestDTO.getSolve();
        for (ProblemAnswer answer : answers) {
            int blankNum = answer.getBlankPosition();
            if (!solve.containsKey(blankNum) || !solve.get(blankNum).equals(answer.getCorrectChoice().getChoiceId())) {
                return false;
            }
        }
        return true;
    }

    public boolean isRoundOver(String roomId) {
        BattleGameRoom room = battleGameRepository.findById(roomId);
        return room.getHasPlayerASubmitted() && room.getHasPlayerBSubmitted() && room.nextRound() != null;
    }

    public List<Problem> getCurrentRoundProblem(String roomId, Integer currentRound) {
        BattleGameRoom room = battleGameRepository.findById(roomId);
        return room.getProblemsForRound(currentRound);
    }

    public int calculateScore(Integer submitTime) {
        if (submitTime > 30) {
            throw new BusinessException(String.valueOf(submitTime), "submitTime", ErrorCode.SUBMIT_TIME_EXCEEDED);
        }
        int initialScore = 30;
        double decrementPerSecond = 3;
        if(submitTime >= 0) {
            return Math.max((int) (initialScore - (submitTime * decrementPerSecond)), 10);
        }
        else if(submitTime == -1){
            return 100;
        }
        else if(submitTime<0){
            submitTime *= -1;
            return Math.max((int) (initialScore - (submitTime * decrementPerSecond)), 10) * 2;
        }
        return 0;
    }
}
