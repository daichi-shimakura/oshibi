package com.oshibi.oshibi.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ComedianSearchDto {
    private String keyWord;
    private String unitType;
    private String agency;
}
