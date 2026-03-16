package com.oshibi.oshibi.dto;

import java.time.LocalDate;
import java.time.LocalTime;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ComedianScheduleItemDto {
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
}
