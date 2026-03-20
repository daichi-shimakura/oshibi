package com.oshibi.oshibi.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ComedianLiveRequestDto {
    private String status;

    private Integer netaCount;

    private String netaType;

    private String preComment;
}
