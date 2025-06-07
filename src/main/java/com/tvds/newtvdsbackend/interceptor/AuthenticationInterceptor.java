package com.tvds.newtvdsbackend.interceptor;

import com.tvds.newtvdsbackend.exception.ServiceException;
import com.tvds.newtvdsbackend.utils.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class AuthenticationInterceptor implements HandlerInterceptor {

    private final JwtUtil jwtUtil;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String token = request.getHeader("Token");
        if (token == null || !jwtUtil.validateToken(token)) {
            Map<String, String> errors = new HashMap<>();
            // 使用 "auth_error" 作为键，以便在 GlobalExceptionHandler 中识别此类异常
            errors.put("1", "用户未登录或认证已失效");
            throw new ServiceException(errors);
        }
        // 将用户信息存储在请求属性中，以便后续处理
        // String userId = jwtUtil.getUserIdFromToken(token);
        // request.setAttribute("userId", userId);
        return true;
    }
}