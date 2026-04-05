package com.oshibi.oshibi.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Getter
@AllArgsConstructor
public class ComedianLiveDetailDto {
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

    private List<PerformerDto> performers;

    private String ticketMethod;

    private Boolean hasStreaming;

    private Integer streamingPrice;

    private LocalDate streamingStartDate;

    private LocalDate streamingEndDate;

    private String flyerUrl;

    private Long createdByAccountId;

    private  String createdByName;

    private String description;

    private String address;

    private String nearestStation;

    private String googleMapsUrl;

    private Long accountId;

    private String displayName;

    private String preComment;

    private Integer netaCount;

    private String netaType;

    private String status;
}
