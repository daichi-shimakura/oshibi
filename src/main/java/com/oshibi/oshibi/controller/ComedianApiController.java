package com.oshibi.oshibi.controller;

import com.oshibi.oshibi.dto.ComedianSearchResultDto;
import com.oshibi.oshibi.service.ComedianService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ComedianApiController {

    private final ComedianService comedianService;

    @GetMapping("/api/comedians/search")
    public List<ComedianSearchResultDto> searchComedians(@RequestParam String q) {
        return comedianService.searchComedians(q);
    }
}
