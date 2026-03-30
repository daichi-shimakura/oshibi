package com.oshibi.oshibi.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class VenueRequestDto {
    @NotBlank(message = "会場名は必須です")
    @Size(max = 200, message = "会場名は200文字以内で入力してください")
    private String name;

    @NotBlank(message = "都道府県は必須です")
    private String prefecture;

    @Size(max = 500, message = "住所は500文字以内で入力してください")
    private String address;

    @Size(max = 200, message = "最寄り駅は200文字以内で入力してください")
    private String nearestStation;

    @Size(max = 500, message = "Google Maps URLは500文字以内で入力してください")
    private String googleMapsUrl;
}
