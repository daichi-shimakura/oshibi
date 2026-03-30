package com.oshibi.oshibi.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ComedianLiveRequestDto {
    private String status;

    @Min(value = 0, message = "ネタ本数は0以上で入力してください")
    private Integer netaCount;

    @Size(max = 200, message = "ネタ種類は200文字以内で入力してください")
    private String netaType;

    @Size(max = 1000, message = "コメントは1000文字以内で入力してください")
    private String preComment;
}
