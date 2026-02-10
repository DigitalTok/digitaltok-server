package com.digital_tok.template.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class SubwayCreateRequestDTO {

    @NotBlank(message = "역 이름은 필수입니다.")
    @Size(max = 10, message = "역 이름은 50자를 초과할 수 없습니다.")
    @Pattern(regexp = "^(?!.*역$).*$", message = "역 이름 뒤에 '역'을 붙이지 말아주세요. (예: 강남역 -> 강남)")
    private String stationName;

    @NotBlank(message = "영어 역 이름은 필수입니다.")
    @Size(max = 20, message = "영어 역 이름은 50자를 초과할 수 없습니다.") // 추가
    @Pattern(regexp = "^[a-zA-Z\\s]+$", message = "영어 역 이름은 영문만 입력 가능합니다.")
    private String stationNameEng;

    @NotBlank(message = "호선 이름은 필수입니다.")
    @Size(max = 10, message = "호선 이름은 10자를 초과할 수 없습니다.")
    @Pattern(regexp = "^(?!.*호선$).*$", message = "호선 이름 뒤에 '호선'을 붙이지 말아주세요. (예: 2호선 -> 2)")
    private String lineName;

}
