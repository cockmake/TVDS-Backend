package com.tvds.newtvdsbackend.controller;


import com.tvds.newtvdsbackend.domain.dto.UserDTO;
import com.tvds.newtvdsbackend.domain.vo.BaseResponseVO;
import com.tvds.newtvdsbackend.exception.LoginException;
import com.tvds.newtvdsbackend.exception.ServiceException;
import com.tvds.newtvdsbackend.utils.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/jwt")
@RequiredArgsConstructor
public class JwtController {
    private final JwtUtil jwtUtil;

    @PostMapping("/login")
    BaseResponseVO login(
            @RequestBody @Validated UserDTO userDTO
    ) {
        // 登录逻辑
        // 假设登录成功，生成JWT
        if (userDTO.getPassword().equals("password")) {
            // 登录成功
            String token = jwtUtil.generateToken(userDTO.getUsername());
            // 返回响应
            return BaseResponseVO.success(Map.of("token", token, "message", "登录成功"));
        } else {
            // 登录失败，返回错误信息
            throw new LoginException(Map.of("1", "用户名或密码错误"));
        }
    }

    @GetMapping("/logout")
    BaseResponseVO logout(
            @AuthenticationPrincipal String username
    ) {
        jwtUtil.invalidateToken(username);
        return BaseResponseVO.success("登出成功");
    }

    @GetMapping("/info")
    BaseResponseVO getUserInfo(
            @AuthenticationPrincipal String username
    ) {
        // 验证JWT并获取用户信息
        return BaseResponseVO.success(Map.of(
                "username", username, "message", "访问成功")
        );
    }
}
