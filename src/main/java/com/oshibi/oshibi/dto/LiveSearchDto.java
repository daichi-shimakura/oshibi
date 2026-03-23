package com.oshibi.oshibi.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LiveSearchDto {
    private String keyWord;
    private LocalDate dateFrom;
    private LocalDate dateTo;
    private boolean hasPast;
    private String prefecture;
    private String liveType;
    private Integer maxPrice;
    private String startTimeSlot;
}
