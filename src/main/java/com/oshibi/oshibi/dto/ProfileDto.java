package com.oshibi.oshibi.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProfileDto {

    //ライブ関係者

    private String unitType;

    private String agency;

    private String memberNames;

    //共通

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
