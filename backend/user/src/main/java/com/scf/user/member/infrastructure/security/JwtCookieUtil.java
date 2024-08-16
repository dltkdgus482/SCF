package com.scf.user.member.infrastructure.security;

import jakarta.servlet.http.Cookie;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import org.springframework.stereotype.Component;

@Component
public class JwtCookieUtil {

    public Cookie createCookie(String key, String value) {

        value = URLEncoder.encode(value, StandardCharsets.UTF_8);
        Cookie cookie = new Cookie(key, value);
        cookie.setMaxAge(24 * 60 * 60); // 24시간
        //cookie.setSecure(true);
        cookie.setPath("/");
        cookie.setHttpOnly(true);

        return cookie;
    }
}