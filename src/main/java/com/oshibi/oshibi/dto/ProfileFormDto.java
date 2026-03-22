package com.oshibi.oshibi.dto;

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

    private String agency;

    private String memberNames;

    //共通
    private String accountType;

    private String displayName;

    private String profileImageUrl;

    private String description;

    private String xUrl;

    private String instagramUrl;

    private String youtubeUrl;

    private String tiktokUrl;

    private String noteUrl;

    private String podcastUrl;
}
