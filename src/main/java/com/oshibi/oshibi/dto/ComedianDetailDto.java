package com.oshibi.oshibi.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class ComedianDetailDto {

    private String displayName;

    private String unitType;

    private String agency;

    private String memberNames;

    private String profileImageUrl;

    private Long accountId;

    private String description;

    private String xUrl;

    private String instagramUrl;

    private String youtubeUrl;

    private String tiktokUrl;

    private String noteUrl;

    private String podcastUrl;

    private List<ComedianScheduleItemDto> comedianLives;
}
