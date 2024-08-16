package com.scf.user.member.application.service;

import com.scf.user.member.domain.dto.UserPasswordRequestDto;
import com.scf.user.member.domain.entity.Member;
import com.scf.user.member.domain.repository.UserRepository;
import com.scf.user.member.infrastructure.security.AuthenticationProviderService;
import java.security.SecureRandom;
import java.time.Duration;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class ResetServiceImpl implements ResetService {

    private final UserRepository userRepository;
    private final RedisService redisService;
    private final EmailService emailService;
    private final AuthenticationProviderService authenticationProviderService;

    @Override
    public UserPasswordRequestDto requestUserId(String userId) {
        // 사용자 정보 조회
        Member member = userRepository.findByUserId(userId)
            .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        UserPasswordRequestDto dto = new UserPasswordRequestDto();
        dto.setUserId(member.getUserId());
        return dto;

    }

    // 임의로 생성된 uuid 문자열을 유저의 이메일로 전송
    @Override
    public void sendRestRandomNumber(String userId) {
        // 사용자 정보 조회
        Member member = userRepository.findByUserId(userId)
            .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        // 랜덤 6자리 숫자 생성
        String resetCode = generateRandomCode(6);

        // 생성한 인증정보를 redis에 설정하여 나중에 검증할 수 있도록 한다. (redis에서 유효시간도 함께 설정)
        Duration duration = Duration.ofMinutes(3);
        redisService.setValues(userId, resetCode, duration);

        // 이메일 전송
        String emailContent = "street coding fighter !!! \n 인증번호는 아래와 같습니다. \n" + resetCode;
        emailService.sendEmail(member.getEmail(), "street coding fighter 인증번호 입니다.", emailContent);
    }

    // 비밀번호 재설정
    @Override
    public void resetPassword(String userId, String newPassword) {
        // 사용자 조회
        Member member = userRepository.findByUserId(userId)
            .orElseThrow(() -> new RuntimeException("User not found"));

        // 비밀번호 암호화
        String encodedPassword = authenticationProviderService.passwordEncoder()
            .encode(newPassword);

        // 비밀번호 업데이트
        member.setPassword(encodedPassword);
        userRepository.save(member);

    }

    @Override
    public boolean validateAuthCode(String userId, String inputCode) {
        // Redis에서 인증 코드를 가져온다.
        String storedCode = redisService.getValue(userId);

        // 저장된 인증 코드와 입력한 인증 코드 비교
        // 인증코드가 expired 안 됐거나 존재하는 경우
        if (storedCode != null && storedCode.equals(inputCode)) {
            // 인증 코드가 일치하는 경우
            return true;
        } else {
            // 인증 코드가 일치하지 않는 경우
            return false;
        }
    }

    @Override
    public void sendRegistrationCode(String email) {
        // 해당 이메일로 랜덤한 번호를 보냅니다.
        // 랜덤 6자리 숫자 생성
        String resetCode = generateRandomCode(6);

        // 생성한 인증정보를 redis에 설정하여 나중에 검증할 수 있도록 한다. (redis에서 유효시간도 함께 설정)
        Duration duration = Duration.ofMinutes(3);
        redisService.setValues(email, resetCode, duration);

        // 이메일 전송
        String emailContent = "street coding fighter !!! 회원가입시 인증번호는 아래와 같습니다. \n" + resetCode;
        emailService.sendEmail(email, "street coding fighter 인증번호 입니다.", emailContent);

    }

    @Override
    public boolean isEqualNumber(String email, String code) {
        // Redis에서 가져옵니다.
        String storedCode = redisService.getValue(email);

        // 저장된 인증 코드와 입력한 인증 코드 비교
        // 인증코드가 expired 안 됐거나 존재하는 경우
        if (storedCode != null && storedCode.equals(code)) {
            // 인증 코드가 일치하는 경우
            return true;
        } else {
            // 인증 코드가 일치하지 않는 경우
            return false;
        }

    }

    // 랜덤한 번호 6자리를 보냄.
    private String generateRandomCode(int length) {
        SecureRandom random = new SecureRandom();
        return IntStream.range(0, length)
            .map(i -> random.nextInt(10)) // 0-9 범위의 숫자 생성
            .mapToObj(String::valueOf)
            .collect(Collectors.joining());
    }

}
