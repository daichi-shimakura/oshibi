package com.oshibi.oshibi.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class LiveFormDto {
    private Long createdById;

    private Long venueId;

    private String venueName;

    private String prefecture;

    private String address;

    private String nearestStation;

    private String googleMapsUrl;

    private Long liveId;

    private String title;

    private String liveType;

    private String description;

    private LocalDate date;

    private LocalTime openTime;

    private LocalTime startTime;

    private Integer priceAdvance;

    private Integer priceDoor;

    private String ticketMethod;

    private Boolean hasStreaming;

    private Integer streamingPrice;

    private LocalDate streamingStartDate;

    private LocalDate streamingEndDate;

    private String flyerUrl;

    private List<PerformerDto> performers;

    private List<PerformerAddDto> performerAdds;
}
