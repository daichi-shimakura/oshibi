package com.oshibi.oshibi.controller;

import com.oshibi.oshibi.dto.EmailChangeDto;
import com.oshibi.oshibi.dto.PasswordChangeDto;
import com.oshibi.oshibi.dto.RegisterDto;
import com.oshibi.oshibi.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final AuthenticationManager authenticationManager;

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
    public String register(RegisterDto dto,
                           HttpServletRequest request,
                           HttpServletResponse response) {
        authService.register(dto);

        UsernamePasswordAuthenticationToken authToken =
                new UsernamePasswordAuthenticationToken(dto.getEmail(), dto.getPassword());
        Authentication authentication = authenticationManager.authenticate(authToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // セッションに認証情報を保存する（Spring Security 6で必須）
        new HttpSessionSecurityContextRepository()
                .saveContext(SecurityContextHolder.getContext(), request, response);

        return "redirect:/profile/edit";
    }

    @PostMapping("/account/email")
    public String changeEmail(@ModelAttribute EmailChangeDto dto,
                              @AuthenticationPrincipal UserDetails userDetails,
                              HttpServletRequest request){
        authService.changeEmail(userDetails.getUsername(), dto.getNewEmail());
        // セッション無効化
        request.getSession().invalidate();
        return "redirect:/login";
    }

    // パスワード変更（users.password_hash）
    @PostMapping("/account/password")
    public String changePassword(@ModelAttribute PasswordChangeDto dto, @AuthenticationPrincipal UserDetails userDetails){
        authService.changePassword(userDetails.getUsername(), dto);
        return "redirect:/lives";
    }

}
