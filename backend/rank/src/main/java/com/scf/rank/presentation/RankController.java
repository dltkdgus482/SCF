package com.scf.rank.presentation;

import com.scf.rank.application.RankService;
import com.scf.rank.domain.model.UserExp;
import com.scf.rank.produceTest.UserExpProducer;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.common.errors.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/rank")
public class RankController {

    private final RankService rankService;
    private final UserExpProducer userExpProducer;

    @GetMapping("/total")
    public ResponseEntity<List<UserExp>> getTotalRankings() {
        List<UserExp> ranks = rankService.getAllTimeRankings();
        return new ResponseEntity<>(ranks, HttpStatus.OK);
    }

    @GetMapping("/weekly")
    public ResponseEntity<List<UserExp>> getWeeklyRankings() {
        List<UserExp> ranks = rankService.getWeeklyRankings();
        return new ResponseEntity<>(ranks, HttpStatus.OK);
    }

    @GetMapping("/daily")
    public ResponseEntity<List<UserExp>> getDailyRankings() {
        List<UserExp> ranks = rankService.getDailyRankings();
        return new ResponseEntity<>(ranks, HttpStatus.OK);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<UserExp> getRank(@PathVariable Long userId) {
        UserExp userExp = rankService.getUserRank(userId);
        if (userExp == null) {
            throw new ResourceNotFoundException("유저에 해당되는 랭킹이 없습니다: " + userId);
        }
        return new ResponseEntity<>(userExp, HttpStatus.OK);
    }

    // 테스트용 produce 코드
    @PostMapping("/produce")
    public String produceUserExp(@RequestBody UserExp userExp) {
        userExpProducer.sendUserExpMessage(userExp);
        return "Message produced successfully";
    }
}
