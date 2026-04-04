package com.oshibi.oshibi.service;

import com.oshibi.oshibi.domain.entity.Venue;
import com.oshibi.oshibi.dto.VenueRequestDto;
import com.oshibi.oshibi.dto.VenueSearchResultDto;
import com.oshibi.oshibi.repository.VenueRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class VenueService {
    private final VenueRepository venueRepository;

    public void saveVenue(VenueRequestDto venueRequestDto) {
        Venue venue = new Venue();
        venue.setName(venueRequestDto.getName());
        venue.setAddress(venueRequestDto.getAddress());
        venue.setPrefecture(venueRequestDto.getPrefecture());
        venue.setNearestStation(venueRequestDto.getNearestStation());
        venue.setGoogleMapsUrl(venueRequestDto.getGoogleMapsUrl());
        venueRepository.save(venue);
    }

    public List<VenueSearchResultDto> searchVenues(String keyword) {
        return venueRepository.findByNameContainingIgnoreCase(keyword).stream()
                .map(v -> new VenueSearchResultDto(
                        v.getVenueId(),
                        v.getName(),
                        v.getPrefecture(),
                        v.getAddress(),
                        v.getNearestStation()))
                .toList();
    }
}
