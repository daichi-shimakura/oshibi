package com.oshibi.oshibi.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Getter
@AllArgsConstructor
public class LiveListItemDto {
    private Long liveId;

    private String liveTitle;

    private LocalDate liveDate;

    private LocalTime openTime;

    private LocalTime startTime;

    private String venue;

    private String prefecture;

    private String liveType;

    private Integer priceAdvance;

    private Integer priceDoor;

    private List<String> comedianNames;

    private Integer otherPerformerCount;
}
