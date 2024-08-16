package com.scf.user.member.global.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.scf.user.member.application.service.MemberDetailService;
import com.scf.user.member.application.service.RedisService;
import com.scf.user.member.infrastructure.security.JwtAuthenticationFilter;
import com.scf.user.member.infrastructure.security.JwtCookieUtil;
import com.scf.user.member.infrastructure.security.JwtTokenProvider;
import com.scf.user.member.infrastructure.security.LoginFilter;
import jakarta.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.Collections;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final AuthenticationConfiguration authenticationConfiguration;
    private final JwtTokenProvider jwtTokenProvider;
    private final RedisService redisService;
    private final MemberDetailService memberDetailService;
    private final JwtCookieUtil jwtCookieUtil;

    @Bean
    public AuthenticationManager authenticationManager() throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        LoginFilter loginFilter = new LoginFilter(authenticationManager(), jwtTokenProvider,
            objectMapper(), redisService, memberDetailService, jwtCookieUtil);
        loginFilter.setFilterProcessesUrl("/user/public/login"); // 로그인 엔드포인트 설정.

        httpSecurity
            .cors((corsCustomizer -> corsCustomizer.configurationSource(
                new CorsConfigurationSource() {

                    @Override
                    public CorsConfiguration getCorsConfiguration(HttpServletRequest request) {

                        CorsConfiguration configuration = new CorsConfiguration();

                        configuration.setAllowedOrigins(
                            Arrays.asList("http://localhost:5173",
                                "https://ssafy11s.com", "https://www.ssafy11s.com"));
                        configuration.setAllowedMethods(Collections.singletonList("*"));
                        configuration.setAllowCredentials(true);
                        configuration.setAllowedHeaders(Collections.singletonList("*"));
                        configuration.setExposedHeaders(Collections.singletonList("Authorization"));
                        configuration.setMaxAge(3600L);

                        return configuration;
                    }
                })));
        httpSecurity
            .csrf(AbstractHttpConfigurer::disable)
            .formLogin(AbstractHttpConfigurer::disable)
            .httpBasic(AbstractHttpConfigurer::disable)
            .authorizeHttpRequests(user -> user
                .requestMatchers("/user/public/login", "/user/public/join",
                    "/user/public/validate/**", "/user/public/request-verification-code",
                    "/user/public/request-verification", "/user/public/change-password",
                    "/user/public/reissue", "user/public/list","/user/public/charaterType",
                    "/user/public/name/**")
                .permitAll()

                .anyRequest().authenticated())
            .addFilterBefore(new JwtAuthenticationFilter(jwtTokenProvider),
                UsernamePasswordAuthenticationFilter.class)
            .addFilterAt(loginFilter, UsernamePasswordAuthenticationFilter.class) // 로그인 필터 추가
            .sessionManagement(session -> session
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS));
        return httpSecurity.build();
    }

}
