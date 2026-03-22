package com.oshibi.oshibi.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
public class PasswordChangeDto {
    private String currentPassword;

    private String newPassword;

    private String confirmPassword;

}
