package com.oshibi.oshibi.dto;

import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PerformerAddDto {

    private Long accountId;

    private String displayName;

    @Size(max = 100, message = "ゲスト名は100文字以内で入力してください")
    private String guestName;
}
