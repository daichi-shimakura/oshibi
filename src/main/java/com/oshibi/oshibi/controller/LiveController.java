package com.oshibi.oshibi.controller;

import com.oshibi.oshibi.service.LiveService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

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
}
