package com.oshibi.oshibi.controller;

import com.oshibi.oshibi.service.LiveService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

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
}
