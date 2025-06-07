package com.tvds.newtvdsbackend.utils;

import com.tvds.newtvdsbackend.exception.LoginException;
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
import java.util.Map;
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
        byte[] decodedKey = Base64.getEncoder().encode(SECRET_KEY.getBytes(StandardCharsets.UTF_8));
        this.key = Keys.hmacShaKeyFor(decodedKey);
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

    // 验证Token并存储用户ID
    public String validateToken(String token) {
        try {
            // 1. 解析异常
            Claims claims = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
            String userId = claims.getId();
            if (userId != null && !userId.isEmpty()) {
                String redisToken = redisTemplate.opsForValue().get(REDIS_KEY_PREFIX + userId);
                if (redisToken == null || !redisToken.equals(token)) {
                    // 3. 如果Redis中没有对应的token或过期
                    throw new JwtException("Token已失效或不匹配");
                }
                return userId; // 返回用户ID
            } else {
                // 2. 如果用户ID为空，抛出异常
                throw new JwtException("Token中不包含有效的用户ID");
            }
        } catch (JwtException e) {
            // 抛出异常
            throw new LoginException(Map.of("1", "Token验证失败"));
        }
    }

    // 使Token失效
    public boolean invalidateToken(String userId) {
        // 从Redis中删除token
        return redisTemplate.delete(REDIS_KEY_PREFIX + userId);
    }
}
