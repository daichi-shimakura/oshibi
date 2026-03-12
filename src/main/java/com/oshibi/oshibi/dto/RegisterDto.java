package com.oshibi.oshibi.dto;

import lombok.Getter;
import lombok.Setter;

@Setter  // Springがフォームの値をDTOに詰める（書き込み）
@Getter  // サービス層がDTOから値を取り出す（読み込み）
public class RegisterDto {

    private String email;

    private String password;

    private String passwordConfirm;

    private String accountType;

}
