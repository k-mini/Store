package com.kmini.store.global.config.security.jwt;

import com.kmini.store.global.config.security.auth.AccountContext;
import com.kmini.store.global.config.security.auth.CustomUserDetailsService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.Date;

import static io.jsonwebtoken.SignatureAlgorithm.HS256;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtTokenUtil {

    @Value("${token.secret}")
    private String secretKey;
    private final CustomUserDetailsService userDetailsService;

    // 30분
    private long tokenValidTime = 30 * 60 * 1000L;

    public String createToken(Claims claims) {
        // 저장할 key value
        Date now = new Date();
        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + tokenValidTime))
                .signWith(HS256, secretKey)
                .compact();
    }

    public boolean validateToken(String jwt) {
        try {
            Jwts.parser().setSigningKey(secretKey).parseClaimsJws(jwt);
            return true;
        } catch (Exception e) {
            log.error("msg : {}", e);
            return false;
        }
    }

    public Authentication getAuthentication(String jwt) {

        if (!validateToken(jwt)) {
            return UsernamePasswordAuthenticationToken
                    .unauthenticated(null, null);
        }

        Jws<Claims> jws = Jwts.parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(jwt);

        Claims claims = jws.getBody();
        String email = claims.get("email").toString();
        AccountContext accountContext = (AccountContext) userDetailsService.loadUserByUsername(email);

        return UsernamePasswordAuthenticationToken.authenticated(accountContext ,null, accountContext.getAuthorities());
    }
}
