package com.scf.problem.presentation;

import com.scf.problem.application.ProblemService;
import com.scf.problem.domain.dto.ProblemRequest;
import com.scf.problem.domain.dto.ProblemResponse.ProblemInfoDTO;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/problem")
public class ProblemController {

    private final ProblemService problemService;

    @GetMapping("/{problemId}")
    public ResponseEntity<ProblemInfoDTO> selectProblem(@PathVariable Long problemId) {

        ProblemInfoDTO problem = problemService.findOneByProblemId(problemId);
        return new ResponseEntity<>(problem, HttpStatus.OK);
    }

    @GetMapping()
    public ResponseEntity<List<ProblemInfoDTO>> selectProblemsByLimit(@RequestParam int limit) {

        List<ProblemInfoDTO> randomProblems = problemService.getRandomProblems(limit);
        return new ResponseEntity<>(randomProblems, HttpStatus.OK);
    }

    @PostMapping()
    public ResponseEntity<?> addProblem(@RequestBody List<ProblemRequest.ProblemInfoDTO> problems) {

        problemService.addProblems(problems);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
