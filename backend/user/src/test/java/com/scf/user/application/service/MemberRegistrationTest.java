package com.scf.user.application.service;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.scf.user.member.domain.dto.UserRegisterRequestDto;
import com.scf.user.member.domain.repository.UserRepository;
import java.time.LocalDate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

@SpringBootTest
@AutoConfigureMockMvc
public class MemberRegistrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    // LocalDate 객체를 사용하여 날짜 설정
    LocalDate birthDate = LocalDate.of(2006, 1, 31);

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        userRepository.deleteAll(); // 테스트 전에 모든 데이터를 삭제하여 초기화
    }

    @Test
    @DisplayName("유저가 정상적으로 회원가입이 되어야 합니다.")
    void testRegisterUser() throws Exception {
        UserRegisterRequestDto userRegisterRequestDto = new UserRegisterRequestDto("test1", "hihi", "테스트용", "싸피고등학교", birthDate);

        ResultActions resultActions = mockMvc.perform(post("/user/join")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(userRegisterRequestDto)));

        resultActions.andExpect(status().isOk())
            .andDo(print());
    }




}
