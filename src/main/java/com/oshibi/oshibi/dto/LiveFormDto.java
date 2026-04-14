package com.oshibi.oshibi.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class LiveFormDto {
    private Long createdById;

    private Long venueId;

    private String venueName;

    private String prefecture;

    private String address;

    private String nearestStation;

    private String googleMapsUrl;

    private Long liveId;

    @NotBlank(message = "ライブ名は必須です")
    @Size(max = 200, message = "ライブ名は200文字以内で入力してください")
    private String title;

    @NotBlank(message = "ライブ種別は必須です")
    private String liveType;

    @Size(max = 1000, message = "備考・説明は1000文字以内で入力してください")
    private String description;

    @NotNull(message = "開催日は必須です")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate date;

    @DateTimeFormat(iso = DateTimeFormat.ISO.TIME)
    private LocalTime openTime;

    @NotNull(message = "開演時間は必須です")
    @DateTimeFormat(iso = DateTimeFormat.ISO.TIME)
    private LocalTime startTime;

    @Min(value = 0, message = "料金は0以上で入力してください")
    private Integer priceAdvance;

    @Min(value = 0, message = "料金は0以上で入力してください")
    private Integer priceDoor;

    private String ticketMethod;

    private Boolean hasStreaming;

    @Min(value = 0, message = "配信料金は0以上で入力してください")
    private Integer streamingPrice;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate streamingStartDate;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate streamingEndDate;

    @Size(max = 500, message = "フライヤーURLは500文字以内で入力してください")
    private String flyerUrl;

    @Size(max = 500)
    private String ticketUrl;

    private List<PerformerDto> performers;

    private List<PerformerAddDto> performerAdds;
}