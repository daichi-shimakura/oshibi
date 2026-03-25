package com.oshibi.oshibi.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class VenueRequestDto {
    private String name;

    private String prefecture;

    private String address;

    private String nearestStation;

    private String googleMapsUrl;
}
