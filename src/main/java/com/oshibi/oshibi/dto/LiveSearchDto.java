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
    private String liveTitle;
    private LocalDate dateFrom;
    private LocalDate dateTo;
    private boolean hasPast;
}
