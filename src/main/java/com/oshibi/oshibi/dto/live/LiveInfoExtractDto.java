package com.oshibi.oshibi.dto.live;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class LiveInfoExtractDto {

    @JsonProperty("live_name")
    private String liveName;

    @JsonProperty("live_type")
    private String liveType;

    @JsonProperty("live_date")
    private String liveDate;

    @JsonProperty("open_time")
    private String openTime;

    @JsonProperty("start_time")
    private String startTime;

    @JsonProperty("venue_name")
    private String venueName;

    @JsonProperty("venue_prefecture")
    private String venuePrefecture;

    @JsonProperty("performers")
    private List<String> performers;

    @JsonProperty("price_advance")
    private Integer priceAdvance;

    @JsonProperty("price_door")
    private Integer priceDoor;

    @JsonProperty("ticket_url")
    private String ticketUrl;
}
