package com.oshibi.oshibi.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class PasswordChangeDto {
    @NotBlank(message = "現在のパスワードは必須です")
    private String currentPassword;

    @NotBlank(message = "新しいパスワードは必須です")
    @Size(min = 6, max = 255, message = "パスワードは6文字以上255文字以内で入力してください")
    @Pattern(regexp = "^[\\x20-\\x7E]+$", message = "パスワードは半角英数記号で入力してください")
    private String newPassword;

    @NotBlank(message = "パスワード確認は必須です")
    private String confirmPassword;
}
