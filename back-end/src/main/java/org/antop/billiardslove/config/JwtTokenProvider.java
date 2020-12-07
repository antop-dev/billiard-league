package org.antop.billiardslove.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import org.antop.billiardslove.jpa.entity.KakaoLogin;
import org.antop.billiardslove.jpa.properties.SecretProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.util.Base64;
import java.util.Date;

@RequiredArgsConstructor
@Component
@EnableConfigurationProperties(SecretProperties.class)
public class JwtTokenProvider {

    private String secretKey;

    private SecretProperties secretProperties;

    // 토큰 유효시간 30분
    private static final long tokenValidTime = 30 * 60 * 1000L;

    // 객체 초기화, secretKey를 Base64로 인코딩한다.
    @PostConstruct
    protected void init() {
        // TODO: setter 를 사용하지 않는 생성자 방식의 프로퍼티 활용
        this.secretKey = Base64.getEncoder().encodeToString(secretProperties.getSecretKey().getBytes());
    }

    /**
     * 토큰생성
     *
     * @param deviceId   장비 아이디
     * @param kakaoLogin 카카오 정보
     * @return JWT 토큰
     */
    public String createToken(String deviceId, KakaoLogin kakaoLogin) {
        Claims claims = Jwts.claims().setSubject(deviceId); // JWT payload 에 저장되는 정보단위
        claims.put("kakaoLogin", kakaoLogin); // 정보는 key / value 쌍으로 저장된다.
        Date now = new Date();
        return Jwts.builder()
                .setClaims(claims) // 정보 저장
                .setIssuedAt(now) // 토큰 발행 시간 정보
                .setExpiration(new Date(now.getTime() + tokenValidTime)) // set Expire Time
                .signWith(SignatureAlgorithm.HS256, secretKey)  // 사용할 암호화 알고리즘과
                // signature 에 들어갈 secret값 세팅
                .compact();
    }

    /**
     * 토큰에서 회원 정보 추출
     *
     * @param token 토큰
     * @return 회원 정보
     */
    public String getUserPk(String token) {
        return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody().getSubject();
    }

    /**
     * Header에서 AUTHORIZATION 추출
     *
     * @param request
     * @return AUTHORIZATION
     */
    public String resolveToken(HttpServletRequest request) {
        return request.getHeader(HttpHeaders.AUTHORIZATION);
    }

    /**
     * 토큰의 유효성 + 만료일자 확인
     *
     * @param jwtToken 토큰
     * @return 참일시 유효
     */
    public boolean validateToken(String jwtToken) {
        Jws<Claims> claims = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(jwtToken);
        return !claims.getBody().getExpiration().before(new Date());
    }
}