package com.scf.user.application.presentation.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.scf.user.member.application.service.RedisService;
import com.scf.user.member.application.service.UserService;
import com.scf.user.member.domain.dto.LoginDto;
import com.scf.user.member.domain.dto.TokenDto;
import com.scf.user.member.domain.dto.UserInfoResponseDto;
import com.scf.user.member.domain.dto.UserRegisterRequestDto;
import com.scf.user.member.domain.repository.UserRepository;
import com.scf.user.member.infrastructure.security.JwtCookieUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockCookie;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;

import java.time.LocalDate;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class MemberControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    // LocalDate 객체를 사용하여 날짜 설정
    LocalDate birthDate = LocalDate.of(2006, 1, 31);

    @Autowired
    private UserService userService;

    @Autowired
    private RedisService redisService;

    @Autowired
    private JwtCookieUtil jwtCookieUtil;

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

//    @Test
//    @DisplayName("회원가입 후 유저 정보를 조회할 수 있어야 합니다.")
//    void testUserInfo() throws Exception {
//        // 회원가입
//        UserRegisterRequestDto registerRequestDto = new UserRegisterRequestDto("test1", "hihi", "테스트용", "싸피고등학교", birthDate);
//        mockMvc.perform(post("/user/join")
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(objectMapper.writeValueAsString(registerRequestDto)))
//            .andExpect(status().isOk())
//            .andDo(print());
//
//        // 유저 정보 조회를 위한 Mock 설정
//        UserInfoResponseDto userInfoDto = new UserInfoResponseDto("test1", "테스트용", "싸피고등학교", birthDate);
//        when(userService.getUserInfo("27")).thenReturn(userInfoDto);
//
//        // 유저 정보 조회 API 호출
//        mockMvc.perform(get("/user")
//                .header("memberId", "27")) // 실제 ID를 사용하거나 설정한 ID를 사용
//            .andExpect(status().isOk())
//            .andExpect(jsonPath("$.userId").value("test1"))
//            .andExpect(jsonPath("$.name").value("테스트용"))
//            .andExpect(jsonPath("$.schoolName").value("싸피고등학교"))
//            .andExpect(jsonPath("$.birth").value("2006-01-31"))
//            .andDo(print());
//
//        verify(userService, times(1)).getUserInfo("27");
//    }

    // 로그인 테스트
    @Test
    @DisplayName("유저가 정상적으로 로그인이 되어야 합니다.")
    void testLoginUser() throws Exception {
        LoginDto loginDto = new LoginDto("test1", "hihi");
        ResultActions resultActions = mockMvc.perform(post("/user/login")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(loginDto)));

        resultActions.andExpect(status().isOk())
            .andDo(print());

        // MvcResult를 이용하여 응답 헤더에서 토큰 추출
        MvcResult mvcResult = resultActions.andReturn();
        String token = mvcResult.getResponse().getHeader("Authorization");

        // 토큰 콘솔 출력
        System.out.println("토큰: " + token);
    }



    // 로그아웃 테스트
    @Test
    @DisplayName("로그아웃이 정상적으로 되어야 합니다.")
    void testLogout() throws Exception {
        mockMvc.perform(post("/user/logout")
                .header("memberId", "27"))
            .andExpect(status().isOk())
            .andExpect(content().string("로그아웃이 성공적으로 되었습니다."))
            .andDo(print());

        verify(redisService, times(1)).deleteValue("27");
    }

    // 아이디 중복 확인 테스트 (중복)
    @Test
    @DisplayName("아이디 중복 확인 - 중복된 아이디")
    void testCheckUserIdDuplicate() throws Exception {
        String userId = "test1";
        when(userService.checkUserIdDuplicate(userId)).thenReturn(true);

        mockMvc.perform(get("/user/validate/{userId}", userId))
            .andExpect(status().isConflict())
            .andExpect(content().string("이미 사용하고 있는 아이디입니다."))
            .andDo(print());

        verify(userService, times(1)).checkUserIdDuplicate(userId);
    }

    // 아이디 중복 확인 테스트 (사용 가능)
    @Test
    @DisplayName("아이디 중복 확인 - 사용 가능한 아이디")
    void testCheckUserIdAvailable() throws Exception {
        String userId = "newUser";
        when(userService.checkUserIdDuplicate(userId)).thenReturn(false);

        mockMvc.perform(get("/user/validate/{userId}", userId))
            .andExpect(status().isOk())
            .andExpect(content().string("사용가능한 아이디입니다."))
            .andDo(print());

        verify(userService, times(1)).checkUserIdDuplicate(userId);
    }

    // 토큰 재발급 테스트
    @Test
    @DisplayName("토큰 재발급이 정상적으로 되어야 합니다.")
    void testRefreshAccessToken() throws Exception {
        String refreshToken = "existingRefreshToken";
        TokenDto newTokenDto = new TokenDto("newAccessToken", "newRefreshToken");

        when(userService.extractRefreshTokenFromCookie(any())).thenReturn(refreshToken);
        when(userService.refreshToken(refreshToken)).thenReturn(newTokenDto);

        mockMvc.perform(post("/user/reissue")
                .cookie(new MockCookie("refresh", refreshToken)))
            .andExpect(status().isOk())
            .andExpect(header().string(HttpHeaders.AUTHORIZATION, "Bearer newAccessToken"))
            .andExpect(cookie().value("refresh", "newRefreshToken"))
            .andDo(print());

        verify(userService, times(1)).extractRefreshTokenFromCookie(any());
        verify(userService, times(1)).refreshToken(refreshToken);
    }

    // 회원 탈퇴 테스트
    @Test
    @DisplayName("회원 탈퇴가 정상적으로 되어야 합니다.")
    void testQuitUser() throws Exception {
        when(userService.quitMember("27")).thenReturn(true);

        mockMvc.perform(delete("/user/quit")
                .header("memberId", "27"))
            .andExpect(status().isOk())
            .andExpect(content().string("회원 탈퇴가 완료되었습니다."))
            .andDo(print());

        verify(userService, times(1)).quitMember(anyString());
        verify(redisService, times(1)).deleteValue("27");
    }
}
