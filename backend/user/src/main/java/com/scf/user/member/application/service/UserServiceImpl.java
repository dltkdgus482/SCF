package com.scf.user.member.application.service;


import static com.scf.user.member.application.service.GachaService.characterTypes;
import static com.scf.user.member.application.service.GachaService.clothingTypes;

import com.scf.user.member.application.client.ContentClient;
import com.scf.user.member.domain.dto.*;
import com.scf.user.member.domain.dto.charater.CharacterType;
import com.scf.user.member.domain.dto.charater.ClothingType;
import com.scf.user.member.global.exception.NotEnoughExperienceException;
import com.scf.user.profile.domain.repository.CharacterRepository;
import com.scf.user.member.domain.entity.Member;
import com.scf.user.profile.domain.entity.Character;
import com.scf.user.member.domain.repository.UserRepository;
import com.scf.user.member.infrastructure.security.AuthenticationProviderService;
import com.scf.user.member.infrastructure.security.JwtTokenProvider;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import java.time.Duration;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class UserServiceImpl implements UserService {

    private final AuthenticationProviderService authenticationProviderService;
    private final UserRepository userRepository;
    private final CharacterRepository characterRepository;
    private final RedisService redisService;
    private final JwtTokenProvider jwtTokenProvider;
    private final ContentClient contentClient;
    private final GachaService gachaService;
    @Autowired
    public UserServiceImpl(AuthenticationProviderService authenticationProviderService,
        UserRepository userRepository, CharacterRepository characterRepository,
        @Lazy JwtTokenProvider jwtTokenProvider,
        RedisService redisService, ContentClient contentClient, GachaService gachaService) {
        this.authenticationProviderService = authenticationProviderService;
        this.userRepository = userRepository;
        this.characterRepository = characterRepository;
        this.jwtTokenProvider = jwtTokenProvider;
        this.redisService = redisService;
        this.contentClient = contentClient;
        this.gachaService = gachaService;
    }

    @Override
    @Transactional
    public UserRegisterResponseDto register(UserRegisterRequestDto registerRequestDto) {
        Member member = Member.builder()
            .userId(registerRequestDto.getUserId())
            .password(authenticationProviderService.passwordEncoder()
                .encode(registerRequestDto.getPassword()))
            .name(registerRequestDto.getName())
            .email(registerRequestDto.getEmail())
            .schoolName(registerRequestDto.getSchoolName())
            .birth(registerRequestDto.getBirth())
            .build();

        // Member 엔티티 저장
        Member savedMember = userRepository.save(member);

        // 수강완료 여부 초기화 요청
        contentClient.initializeCompletionStatus(savedMember.getId())
            .block();

        // Character 생성
        Character character = new Character();
        character.setCharacterType(registerRequestDto.getCharacterType());
        character.setCharacterCloth(1); // defalut는 0
        character.setMember(savedMember);

        // Member 엔티티에 Character 설정
        characterRepository.save(character);

        savedMember.setCharacter(character);

        return new UserRegisterResponseDto(savedMember);
    }

    @Override
    public UserInfoResponseDto getUserInfo(String memberId) {
        Member member = userRepository.findById(Long.parseLong(memberId))
            .orElseThrow(() -> new EntityNotFoundException("User not found"));

        // User 엔티티를 UserInfoResponseDto로 변환
        return new UserInfoResponseDto(
            member.getUserId(),
            member.getName(),
            member.getSchoolName(),
            member.getBirth()
        );
    }

    @Override
    @Transactional
    public boolean quitMember(String memberId) {
        // 사용자 확인
        Member member = userRepository.findById(Long.parseLong(memberId))
            .orElseThrow(() -> new EntityNotFoundException("유저를 찾지 못하였습니다."));

        // 사용자 삭제
        userRepository.delete(member);

        return true;
    }

    @Override
    public boolean checkUserIdDuplicate(String userId) {
        return userRepository.existsByUserId(userId);
    }

    @Override
    public String extractRefreshTokenFromCookie(HttpServletRequest request) {

        String refreshToken = null;
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("refresh")) {
                    refreshToken = cookie.getValue();
                    log.info("쿠키다 !!!!!" + refreshToken);
                }
            }
        }
        return refreshToken;
    }


    @Override
    // token 재발급
    public TokenDto refreshToken(String refresh) {
        // Redis에서 refresh token 조회
        String memberId = jwtTokenProvider.extractMemberId(refresh);
        log.info("memberId-------- " + memberId);
        String storedRefreshToken = redisService.getValue(memberId);
        log.info("Stored Refresh Token-------- " + storedRefreshToken);

        // 리프레시 토큰이 유효한지 확인
        if (storedRefreshToken == null || !storedRefreshToken.equals(refresh)) {
            log.debug("Invalid refresh token: " + storedRefreshToken);
            throw new RuntimeException("Invalid refresh token.");
        }

        // 토큰 유효성 검사
        jwtTokenProvider.validateToken(refresh);

        // 새로운 액세스 토큰 생성
        String newAccessToken = jwtTokenProvider.generateAccessToken(Long.valueOf(memberId));
        String newRefreshToken = jwtTokenProvider.createRefreshToken(Long.valueOf(memberId));

        // Redis에 새로운 리프레시 토큰 저장
        Duration expiration = Duration.ofHours(24);
        redisService.setValues(memberId, newRefreshToken, expiration);

        // 클라이언트에 반환할 TokenDto
        TokenDto newToken = new TokenDto();
        newToken.setAccessToken(newAccessToken);
        newToken.setRefreshToken(newRefreshToken);

        return newToken; // 새로운 액세스 토큰과 refreshToken 반환
    }


    // username
    @Override
    public String getName(Long id) {
        return userRepository.findById(id).orElseThrow().getName();
    }


    @Override
    public UserInfoListResponseDto sendUserList() {
        // 모든 유저 정보 조회
        List<Member> members = userRepository.findAll();
        System.out.println(members.size());

        List<UserInfotoSingleResponseDto> userInfotoSingleList = members.stream()
            .map(member -> new UserInfotoSingleResponseDto(member.getId(), member.getName()))
            .collect(Collectors.toList());

        return new UserInfoListResponseDto(userInfotoSingleList);
    }

    @Override
    public String findUsername(Long memberId) {
        // 유저 정보 조회
        Optional<Member> memberOpt = userRepository.findById(memberId);

        // Optional의 값을 가져오는 방법
        if (memberOpt.isPresent()) {
            // Optional이 비어있지 않으면 내부의 Member 객체를 가져옴
            Member member = memberOpt.get();
            // Member 객체에서 이름을 가져와 반환
            return member.getUsername();
        } else {
            // Optional이 비어있다면 (즉, memberId에 해당하는 사용자가 없다면) 예외 처리 또는 기본값 반환
            throw new IllegalArgumentException("해당 memberId를 가진 유저가 존재하지 않습니다.");
        }

    }

    @Override
    public UserCharacterResponseDTO getUserCharaterType(Long memberId) {
        Member member = userRepository.getById(memberId);
        int characterType = member.getCharacter().getCharacterType();
        int characterCloth = member.getCharacter().getCharacterCloth();

        String characterRarity = determineRarity(characterType); // 캐릭터의 Rarity 결정
        String clothRarity = determineRarity(characterCloth);    // 의상의 Rarity 결정

        // UserCharaterTypeResponseDTO 생성시, 세 개의 파라미터를 전달
        return new UserCharacterResponseDTO(characterType*100 + characterCloth, characterRarity, clothRarity);
    }


    private String determineRarity(int characterType) {
        // clothingTypes에서 rarity를 찾습니다.
        for (ClothingType clothingType : clothingTypes) {
            if (clothingType.getType() == characterType) {
                return clothingType.getRarity().toString();
            }
        }

        // characterTypes에서 rarity를 찾습니다.
        for (CharacterType type : characterTypes) {
            if (type.getType() == characterType) {
                return type.getRarity().toString();
            }
        }

        // 찾지 못한 경우
        return "UNKNOWN";
    }


    @Transactional
    @Override
    public void updateCharacterCloth(Long memberId, int characterCloth) {
        Member member = userRepository.findById(memberId)
                .orElseThrow(() -> new UsernameNotFoundException("Member not found with id: " + memberId));

        Character character = member.getCharacter();

        if (character == null) {
            throw new IllegalStateException("Character not found for member with id: " + memberId);
        }

        if (character.getExp() < 500) {
            throw new NotEnoughExperienceException("Not enough experience to update character cloth for member with id: " + memberId);
        }

        character.setExp(character.getExp() - 500); // 경험치 갱신
        character.setCharacterCloth(characterCloth);
    }

    @Transactional
    @Override
    public void updateCharacterType(Long memberId, int characterType) {
        Member member = userRepository.findById(memberId)
                .orElseThrow(() -> new UsernameNotFoundException("Member not found with id: " + memberId));

        Character character = member.getCharacter();

        if (character == null) {
            throw new IllegalStateException("Character not found for member with id: " + memberId);
        }

        if (character.getExp() < 500) {
            throw new NotEnoughExperienceException("Not enough experience to update character type for member with id: " + memberId);
        }

        character.setExp(character.getExp() - 500); // 경험치 갱신
        character.setCharacterType(characterType);
    }
}
