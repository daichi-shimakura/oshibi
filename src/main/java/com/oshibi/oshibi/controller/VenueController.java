package com.oshibi.oshibi.controller;

import com.oshibi.oshibi.dto.VenueRequestDto;
import com.oshibi.oshibi.service.VenueService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
public class VenueController {

    private final VenueService venueService;

    @GetMapping("venue/new")
    public String showVenueForm(Model model) {
        model.addAttribute("venueRequestDto", new VenueRequestDto());
        return "venue/new";
    }

    @PostMapping("/venue/new")
    public String saveVenue(@ModelAttribute VenueRequestDto dto) {
        venueService.saveVenue(dto);
        return "redirect:/venue/new?success=true"; // or 閉じるページ
    }
}
