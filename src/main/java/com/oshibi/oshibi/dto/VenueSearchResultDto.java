package com.oshibi.oshibi.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class VenueSearchResultDto {
    private Long venueId;
    private String name;
    private String prefecture;
    private String address;
    private String nearestStation;
}
