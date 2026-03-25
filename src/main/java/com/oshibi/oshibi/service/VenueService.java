package com.oshibi.oshibi.service;

import com.oshibi.oshibi.domain.entity.Venue;
import com.oshibi.oshibi.dto.VenueRequestDto;
import com.oshibi.oshibi.repository.VenueRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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
}
