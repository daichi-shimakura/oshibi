package com.oshibi.oshibi.controller;

import com.oshibi.oshibi.service.ComedianService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
@RequiredArgsConstructor
public class ComedianController {
    private final ComedianService comedianService;

    @GetMapping("/comedians")
    public String list(Model model) {
        var comedianlist = comedianService.findAll();
        model.addAttribute("comedians", comedianlist);
        return "comedians/list";
    }

    @GetMapping("/comedians/detail/{accountId}")
    public String detail(Model model, @PathVariable("accountId") Long accountId) {
        var comedianDetail = comedianService.findById(accountId).orElseThrow();
        model.addAttribute("comedian", comedianDetail);
        return "comedians/detail";
    }
}
