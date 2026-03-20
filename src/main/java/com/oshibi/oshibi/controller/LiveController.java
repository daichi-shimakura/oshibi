package com.oshibi.oshibi.controller;

import com.oshibi.oshibi.dto.LiveRequestDto;
import com.oshibi.oshibi.service.LiveService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
public class LiveController {
    private final LiveService liveService;

    @GetMapping("/lives")
    public String list(Model model) {
        var liveList = liveService.findAll();
        model.addAttribute("lives", liveList);
        return "lives/list";
    }

    @GetMapping("/lives/{liveId}")
    public String detail(Model model, @PathVariable("liveId") Long liveId) {
        var liveDetail = liveService.findById(liveId).orElseThrow();
        model.addAttribute("live", liveDetail);
        return "lives/detail";
    }

    @GetMapping("/lives/{liveId}/comedians/{accountId}")
    public String comedianLiveDetail(@PathVariable("liveId") Long liveId, @PathVariable("accountId") Long accountId, Model model) {
        var comedianLiveDetail = liveService.findByLiveIdAndAccountId(liveId,accountId).orElseThrow();
        model.addAttribute("comedianLiveDetail", comedianLiveDetail);
        return "lives/comedianLiveDetail";
    }

    @GetMapping("/lives/new")
    public String showNewLiveForm(Model model) {
        model.addAttribute("dto", new LiveRequestDto());
        model.addAttribute("venues", liveService.findAllVenues());
        return "lives/edit";
    }

    @GetMapping("/lives/{liveId}/edit")
    public String editLive(Model model, @PathVariable Long liveId, @AuthenticationPrincipal UserDetails userDetails) {
        liveService.checkAuth(userDetails.getUsername(),liveId);
        var liveDetail = liveService.findLiveForEdit(liveId).orElseThrow();
        model.addAttribute("form", liveDetail);
        model.addAttribute("dto", new LiveRequestDto());
        model.addAttribute("venues", liveService.findAllVenues());
        return "lives/edit";
    }

    @PostMapping("/lives/new")
    public String saveNewLive(LiveRequestDto dto,@AuthenticationPrincipal UserDetails userDetails) {
        var email = userDetails.getUsername();
        var liveId = liveService.save(dto,email);
        return "redirect:/lives/" + liveId;
    }

    @PostMapping("/lives/{liveId}/edit")
    public String saveEditLive(LiveRequestDto dto, @AuthenticationPrincipal UserDetails userDetails, @PathVariable Long liveId) {
        liveService.checkAuth(userDetails.getUsername(),liveId);
        var email = userDetails.getUsername();
        var liveIdRedirect = liveService.save(dto,email);
        return "redirect:/lives/" + liveIdRedirect;
    }

}
