package com.oshibi.oshibi.controller;

import com.oshibi.oshibi.dto.VenueSearchResultDto;
import com.oshibi.oshibi.repository.VenueRepository;
import com.oshibi.oshibi.service.VenueService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class VenueApiController {

    private final VenueService venueService;

    @GetMapping("/api/venues/search")
    public List<VenueSearchResultDto> searchVenues(@RequestParam String q) {
        return venueService.searchVenues(q);
    }
}
