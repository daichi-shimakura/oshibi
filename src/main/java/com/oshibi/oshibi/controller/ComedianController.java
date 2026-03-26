package com.oshibi.oshibi.controller;

import com.oshibi.oshibi.dto.ComedianSearchDto;
import com.oshibi.oshibi.service.ComedianService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
public class ComedianController {
    private final ComedianService comedianService;

    @GetMapping("/comedians")
    public String list(Model model, ComedianSearchDto comedianSearchDto, @RequestParam(defaultValue = "0") int page) {
        Pageable pageable = PageRequest.of(page,21);
        var comedianPage = comedianService.search(comedianSearchDto,pageable);
        model.addAttribute("comedianPage", comedianPage);
        model.addAttribute("comedianSearchDto", comedianSearchDto);
        return "comedians/list";
    }

    @GetMapping("/comedians/detail/{accountId}")
    public String detail(Model model, @PathVariable("accountId") Long accountId) {
        var comedianDetail = comedianService.findById(accountId).orElseThrow();
        model.addAttribute("comedian", comedianDetail);
        return "comedians/detail";
    }
}
