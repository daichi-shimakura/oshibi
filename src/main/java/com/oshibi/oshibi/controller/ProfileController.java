package com.oshibi.oshibi.controller;

import com.oshibi.oshibi.domain.entity.Account;
import com.oshibi.oshibi.dto.ProfileDto;
import com.oshibi.oshibi.repository.AccountRepository;
import com.oshibi.oshibi.service.ProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
public class ProfileController {

    private final ProfileService profileService;
    private final AccountRepository accountRepository;

    @GetMapping("/profile/edit")
    public String showProfileForm(@AuthenticationPrincipal UserDetails userDetails,Model model) {

        String email = userDetails.getUsername();
        Account account = accountRepository.findByUser_Email(email)
                .orElseThrow(() -> new RuntimeException("Account not found"));
        model.addAttribute("profileDto", new ProfileDto());
        model.addAttribute("accountType", account.getAccountType());

        return "profile/edit";
    }


    @PostMapping("/profile/edit")
    public String saveProfile(@AuthenticationPrincipal UserDetails userDetails,ProfileDto dto) {
        String email = userDetails.getUsername();
        // プロフィール保存処理の実装
        profileService.saveProfile(email,dto);
        return "redirect:/lives";
    }

}
