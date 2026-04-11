package com.oshibi.oshibi.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PerformerDto {
    private Long accountId;
    private String displayName;
    private Integer displayOrder;
    private String status;
}
