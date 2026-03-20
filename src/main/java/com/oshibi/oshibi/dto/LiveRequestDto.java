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
@NoArgsConstructor
public class LiveRequestDto {
    private Long liveId;

    private Long venueId;

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

    private List<Long> performerAccountIds;

}


