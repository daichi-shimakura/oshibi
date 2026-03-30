package com.oshibi.oshibi.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Setter  // Springがフォームの値をDTOに詰める（書き込み）
@Getter  // サービス層がDTOから値を取り出す（読み込み）
public class RegisterDto {

    @NotBlank(message = "メールアドレスは必須です")
    @Email(message = "メールアドレスの形式が正しくありません")
    @Size(max = 255, message = "メールアドレスは255文字以内で入力してください")
    private String email;

    @NotBlank(message = "パスワードは必須です")
    @Size(min = 6, max = 255, message = "パスワードは6文字以上255文字以内で入力してください")
    @Pattern(regexp = "^[\\x20-\\x7E]+$", message = "パスワードは半角英数記号で入力してください")
    private String password;

    @NotBlank(message = "パスワード確認は必須です")
    private String passwordConfirm;

    private String accountType;
}
