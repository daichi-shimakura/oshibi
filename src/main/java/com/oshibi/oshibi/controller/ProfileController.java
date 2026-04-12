package com.oshibi.oshibi.controller;

import com.oshibi.oshibi.domain.entity.Account;
import com.oshibi.oshibi.dto.EmailChangeDto;
import com.oshibi.oshibi.dto.PasswordChangeDto;
import com.oshibi.oshibi.dto.ProfileFormDto;
import com.oshibi.oshibi.repository.AccountRepository;
import com.oshibi.oshibi.service.ProfileService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
public class ProfileController {
    private final ProfileService profileService;
    private final AccountRepository accountRepository;

    @GetMapping("/profile/edit")
    public String showProfileForm(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        String email = userDetails.getUsername();
        var profileFormDto = profileService.showProfileEditForm(email);
        model.addAttribute("profileFormDto", profileFormDto);
        return "profile/edit";
    }

    @PostMapping("/profile/edit")
    public String saveProfile(@AuthenticationPrincipal UserDetails userDetails,
                              @Valid ProfileFormDto dto,
                              BindingResult bindingResult,
                              Model model) {
        String email = userDetails.getUsername();
        if (bindingResult.hasErrors()) {
            Account account = accountRepository.findByUser_Email(email).orElseThrow();
            model.addAttribute("accountType", account.getAccountType().name());
            return "profile/edit";
        }
        try {
            profileService.saveProfile(email, dto);
        } catch (IllegalArgumentException e) {
            bindingResult.reject("error.global", e.getMessage());
            Account account = accountRepository.findByUser_Email(email).orElseThrow();
            model.addAttribute("accountType", account.getAccountType().name());
            return "profile/edit";
        }
        return "redirect:/lives";
    }

    @GetMapping("/account/edit")
    public String showAccountEditForm(Model model, @AuthenticationPrincipal UserDetails userDetails) {
        String email = userDetails.getUsername();
        ProfileFormDto form = profileService.showProfileEditForm(email);
        model.addAttribute("profileFormDto", form);
        model.addAttribute("emailChangeDto", new EmailChangeDto());
        model.addAttribute("passwordChangeDto", new PasswordChangeDto());
        model.addAttribute("email", email);
        return "account/edit";
    }

    // プロフィール保存（accounts / comedian_profiles）
    @PostMapping("/account/edit")
    public String saveAccountEdit(@Valid @ModelAttribute ProfileFormDto form,
                                  BindingResult bindingResult,
                                  @AuthenticationPrincipal UserDetails userDetails,
                                  Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("passwordChangeDto", new PasswordChangeDto());
            model.addAttribute("emailChangeDto", new EmailChangeDto());
            model.addAttribute("email", userDetails.getUsername());
            return "account/edit";
        }

        try {
            profileService.saveProfile(userDetails.getUsername(), form);
        } catch (IllegalArgumentException e) {
            bindingResult.reject("error.global", e.getMessage());
            model.addAttribute("passwordChangeDto", new PasswordChangeDto());
            model.addAttribute("emailChangeDto", new EmailChangeDto());
            model.addAttribute("email", userDetails.getUsername());
            return "account/edit";
        }
        return "redirect:/lives";
    }
}
