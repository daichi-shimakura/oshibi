package com.oshibi.oshibi.controller;

import com.oshibi.oshibi.dto.EmailChangeDto;
import com.oshibi.oshibi.dto.PasswordChangeDto;
import com.oshibi.oshibi.dto.RegisterDto;
import com.oshibi.oshibi.service.AuthService;
import com.oshibi.oshibi.service.ProfileService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;
    private final AuthenticationManager authenticationManager;
    private final ProfileService profileService;

    @GetMapping("/login")
    public String login() {
        return "auth/login";
    }

    @GetMapping("/register")
    public String registerForm(Model model) {
        model.addAttribute("registerDto", new RegisterDto());
        return "auth/register";
    }

    @PostMapping("/register")
    public String register(@Valid RegisterDto dto,
                           BindingResult bindingResult,
                           HttpServletRequest request,
                           HttpServletResponse response) {
        if (bindingResult.hasErrors()) {
            return "auth/register";
        }

        try {
            authService.register(dto);
        } catch (IllegalArgumentException e) {
            bindingResult.reject("error.global", e.getMessage());
            return "auth/register";
        }

        UsernamePasswordAuthenticationToken authToken =
                new UsernamePasswordAuthenticationToken(dto.getEmail(), dto.getPassword());
        Authentication authentication = authenticationManager.authenticate(authToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        new HttpSessionSecurityContextRepository()
                .saveContext(SecurityContextHolder.getContext(), request, response);

        return "redirect:/profile/edit";

    }

    @PostMapping("/account/email")
    public String changeEmail(@Valid @ModelAttribute EmailChangeDto dto,
                              BindingResult bindingResult,
                              @AuthenticationPrincipal UserDetails userDetails,
                              HttpServletRequest request,
                              Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("passwordChangeDto", new PasswordChangeDto());
            model.addAttribute("profileFormDto", profileService.showProfileEditForm(userDetails.getUsername()));
            model.addAttribute("email", userDetails.getUsername());
            return "account/edit";
        }

        try {
            authService.changeEmail(userDetails.getUsername(), dto.getNewEmail());
        } catch (IllegalArgumentException e) {
            bindingResult.reject("error.global", e.getMessage());
            model.addAttribute("passwordChangeDto", new PasswordChangeDto());
            model.addAttribute("profileFormDto", profileService.showProfileEditForm(userDetails.getUsername()));
            model.addAttribute("email", userDetails.getUsername());
            return "account/edit";
        }
        // セッション無効化
        request.getSession().invalidate();
        return "redirect:/login";
    }

    // パスワード変更（users.password_hash）
    @PostMapping("/account/password")
    public String changePassword(@Valid @ModelAttribute PasswordChangeDto dto,
                                 BindingResult bindingResult,
                                 @AuthenticationPrincipal UserDetails userDetails,
                                 Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("emailChangeDto", new EmailChangeDto());
            model.addAttribute("profileFormDto", profileService.showProfileEditForm(userDetails.getUsername()));
            model.addAttribute("email", userDetails.getUsername());
            return "account/edit";
        }
        try {
            authService.changePassword(userDetails.getUsername(), dto);
        } catch (IllegalArgumentException e) {
            bindingResult.reject("error.global", e.getMessage());
            model.addAttribute("emailChangeDto", new EmailChangeDto());
            model.addAttribute("profileFormDto", profileService.showProfileEditForm(userDetails.getUsername()));
            model.addAttribute("email", userDetails.getUsername());
            return "account/edit";
        }
        return "redirect:/lives";
    }

    @GetMapping("/403")
    public String accessDenied() {
        return "error/403";
    }
}
