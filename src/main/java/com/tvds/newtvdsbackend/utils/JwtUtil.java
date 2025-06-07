package com.tvds.newtvdsbackend.utils;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Base64;
import java.util.Date;
import java.util.concurrent.TimeUnit;

@Data
@Component
@RequiredArgsConstructor
@ConfigurationProperties(prefix = "jwt")
public class JwtUtil {
    private String SECRET_KEY;
    private long EXPIRATION_MS;
    private String ISSUER;
    private String REDIS_KEY_PREFIX;

    private Key key;

    @PostConstruct
    public void init() {
        // 初始化密钥
        // base64
        this.key = Keys.hmacShaKeyFor(Base64.getEncoder().encode(SECRET_KEY.getBytes(StandardCharsets.UTF_8)));
    }

    private final RedisTemplate<String, String> redisTemplate;

    // 生成Token
    public String generateToken(String userId) {
        Date now = new Date();
        Date expireDate = new Date(now.getTime() + EXPIRATION_MS);
        String token = Jwts.builder()
                .setId(userId) // 设置JWT ID为用户ID
                .setSubject("user")
                .setIssuer(ISSUER)
                .setIssuedAt(now)
                .setExpiration(expireDate)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
        // 将 token 存入 Redis，过期自动删除
        redisTemplate.opsForValue().set(REDIS_KEY_PREFIX + userId, token, EXPIRATION_MS, TimeUnit.MILLISECONDS);
        return token;
    }

    // 获取Token中的用户ID
    public String getUserIdFromToken(String token) {
        try {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
            return claims.getId();
        } catch (JwtException e) {
            return null;
        }
    }

    // 验证Token
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            // 检查Redis中是否存在该token（可选，用于强制下线等场景）
            String userId = getUserIdFromToken(token);
            if (userId != null) {
                String redisToken = redisTemplate.opsForValue().get(REDIS_KEY_PREFIX + userId);
                return token.equals(redisToken);
            }
            return false;
        } catch (JwtException e) {
            return false;
        }
    }

    // 使Token失效
    public void invalidateToken(String userId) {
        // 从Redis中删除token
        redisTemplate.delete(REDIS_KEY_PREFIX + userId);
    }
}
