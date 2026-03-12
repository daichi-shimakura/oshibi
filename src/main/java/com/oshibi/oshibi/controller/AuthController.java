package com.oshibi.oshibi.controller;

import com.oshibi.oshibi.dto.RegisterDto;
import com.oshibi.oshibi.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @GetMapping("/login")
    public String login() {
        // ログイン処理の実装
        return "auth/login";
    }

    @GetMapping("/register")
    public String registerForm() {
        //登録処理の実装
        return "auth/register";
    }

    @PostMapping("/register")
    public String register(RegisterDto dto) {
        //登録処理の実装
        authService.register(dto);
        return "redirect:/lives";
    }
}
