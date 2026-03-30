package com.oshibi.oshibi.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProfileFormDto {
    //ライブ関係者

    private String unitType;

    @Size(max = 100, message = "所属事務所は100文字以内で入力してください")
    private String agency;

    @Size(max = 1000, message = "メンバー名は1000文字以内で入力してください")
    private String memberNames;

    //共通
    private String accountType;

    @NotBlank(message = "表示名は必須です")
    private String displayName;

    @Size(max = 500, message = "URLは500文字以内で入力してください")
    private String profileImageUrl;

    @Size(max = 1000, message = "自己紹介は1000文字以内で入力してください")
    private String description;

    @Size(max = 500, message = "URLは500文字以内で入力してください")
    private String xUrl;

    @Size(max = 500, message = "URLは500文字以内で入力してください")
    private String instagramUrl;

    @Size(max = 500, message = "URLは500文字以内で入力してください")
    private String youtubeUrl;

    @Size(max = 500, message = "URLは500文字以内で入力してください")
    private String tiktokUrl;

    @Size(max = 500, message = "URLは500文字以内で入力してください")
    private String noteUrl;

    @Size(max = 500, message = "URLは500文字以内で入力してください")
    private String podcastUrl;
}
