package com.digital_tok.template.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class SubwayCreateRequest {

    @NotBlank(message = "역 이름은 필수입니다.")
    private String stationName;

    @NotBlank(message = "영어 역 이름은 필수입니다.")
    private String stationNameEng;

    @NotBlank(message = "호선 이름은 필수입니다.")
    private String lineName;

}
