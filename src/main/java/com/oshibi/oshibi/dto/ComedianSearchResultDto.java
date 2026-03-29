package com.oshibi.oshibi.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ComedianSearchResultDto {

    private Long accountId;

    private String displayName;

    private String unitType;

    private String agency;
}
