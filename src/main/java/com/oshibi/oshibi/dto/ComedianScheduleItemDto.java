package com.oshibi.oshibi.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalTime;

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
    private String badgeClass;
    private Integer priceAdvance;
    private Integer priceDoor;
}
