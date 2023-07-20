package com.unitechfourd.security.jwt;

import com.unitechfourd.models.Teacher;
import com.unitechfourd.repositories.TeacherRepository;
import com.unitechfourd.security.services.UserDetailsImpl;
import io.jsonwebtoken.*;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;
import org.springframework.web.util.WebUtils;

import java.util.Arrays;
import java.util.Date;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class JWTUtils {
    //comment

    private static final Logger LOGGER = LoggerFactory.getLogger(JWTUtils.class);

    private final TeacherRepository teacherRepository;
    private final Environment environment;

    @Value("${api.app.jwtSecret}")
    private String jwtSecret;

    @Value("${api.app.jwtExpirationMs}")
    private int jwtExpirationMs;

    @Value("${api.app.jwtCookieName}")
    private String jwtCookie;

    public String getJwtFromCookies(HttpServletRequest request) {
        // Obtém o valor do token JWT do cookie na requisição
        Cookie cookie = WebUtils.getCookie(request, jwtCookie);
        if (cookie != null) {
            return cookie.getValue();
        } else {
            return null;
        }
    }

    public ResponseCookie generateJwtCookie(UserDetailsImpl userPrincipal) {
        // Verifica se o usuário está presente no banco de dados
        final Optional<Teacher> findedUser = teacherRepository.findByUsernameIgnoreCase(userPrincipal.getUsername());

        if (findedUser.isPresent()) {
            // Gera o token JWT a partir do nome de usuário do professor
            String jwt = generateTokenFromUsername(findedUser.get());
            final var builder = ResponseCookie.from(jwtCookie, jwt)
                    .path("/").maxAge(3600000L)
                    .secure(true)
                    .httpOnly(true);

            if (Arrays.asList(environment.getActiveProfiles()).contains("prod")) {
                builder.domain(".unitechfourd.com");
            }

            return builder.build();
        }

        return null;
    }

    public String getUserNameFromJwtToken(String token) {
        // Extrai o nome de usuário do token JWT
        return Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody().getSubject();
    }

    public boolean validateJwtToken(String authToken) {
        // Valida a integridade e a validade do token JWT
        try {
            Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(authToken);
            return true;
        } catch (SignatureException e) {
            LOGGER.error("Invalid JWT signature: {}", e.getMessage());
        } catch (MalformedJwtException e) {
            LOGGER.error("Invalid JWT token: {}", e.getMessage());
        } catch (ExpiredJwtException e) {
            LOGGER.error("JWT token is expired: {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            LOGGER.error("JWT token is unsupported: {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            LOGGER.error("JWT claims string is empty: {}", e.getMessage());
        }

        return false;
    }

    public String generateTokenFromUsername(Teacher user) {
        // Gera o token JWT a partir do objeto Teacher
        return Jwts.builder()
                .claim("id", user.getId())
                .setSubject(user.getName())
                .setIssuedAt(new Date())
                .setExpiration(new Date((new Date()).getTime() + jwtExpirationMs))
                .signWith(SignatureAlgorithm.HS512, jwtSecret)
                .compact();
    }
}
