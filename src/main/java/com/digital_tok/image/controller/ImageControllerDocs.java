package com.digital_tok.image.controller;

import com.digital_tok.global.apiPayload.ApiResponse;
import com.digital_tok.global.security.PrincipalDetails;
import com.digital_tok.image.dto.ImageRequestDTO;
import com.digital_tok.image.dto.ImageResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

@Tag(name = "Image", description = "이미지 관련 API")
public interface ImageControllerDocs {

    @Operation(summary = "최근 사용한 사진 조회 API By 송정은 (개발 완료)", description = "사용자가 최근에 사용한 사진 목록을 조회합니다.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "조회 성공",
                    content = @Content(schema = @Schema(implementation = ImageResponseDTO.RecentImageListDto.class))
            )
    })
    ApiResponse<ImageResponseDTO.RecentImageListDto> getRecentImages(
            @Parameter(hidden = true)
            @AuthenticationPrincipal PrincipalDetails principal
    );

    @Operation(summary = "이미지 업로드 API By 송정은 (개발완료) ", description = "이미지 파일과 이름을 받아 서버에 업로드합니다.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "업로드 성공",
                    content = @Content(schema = @Schema(implementation = ImageResponseDTO.UploadResultDto.class))
            )
    })
    ApiResponse<ImageResponseDTO.UploadResultDto> uploadImage(
            @Parameter(hidden = true)
            @AuthenticationPrincipal PrincipalDetails principal,

            @Parameter(
                    description = "업로드할 이미지 파일",
                    content = @Content(mediaType = MediaType.MULTIPART_FORM_DATA_VALUE)
            )
            @RequestPart("file") MultipartFile file,

            @Parameter(description = "이미지 이름", example = "myphoto_001")
            @RequestParam("imageName") String imageName
    );

    @Operation(summary = "이미지 미리보기 조회 API By 송정은 (개발 완료)", description = "이미지 ID로 미리보기 URL을 조회합니다. previewUrl이 없으면 originalUrl로 fallback합니다.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "조회 성공",
                    content = @Content(schema = @Schema(implementation = ImageResponseDTO.PreviewResultDto.class))
            )
    })
    ApiResponse<ImageResponseDTO.PreviewResultDto> getImagePreview(
            @Parameter(description = "이미지 ID", example = "55")
            @PathVariable Long imageId
    );

    @Operation(summary = "이미지 바이너리 데이터 조회 API By 송정은 (개발 완료)", description = "기기로 전송할 변환된 E-ink 바이너리 파일(.bin)의 URL을 조회합니다.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "조회 성공",
                    content = @Content(schema = @Schema(implementation = ImageResponseDTO.BinaryResultDto.class))
            )
    })
    ApiResponse<ImageResponseDTO.BinaryResultDto> getImageBinary(
            @Parameter(hidden = true)
            @AuthenticationPrincipal PrincipalDetails principal,

            @Parameter(description = "이미지 ID", example = "55")
            @PathVariable Long imageId
    );

    @Operation(summary = "이미지 즐겨찾기 등록/해제 API By 송정은 (개발완료)", description = "이미지의 즐겨찾기 상태를 변경합니다.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "변경 성공",
                    content = @Content(schema = @Schema(implementation = ImageResponseDTO.FavoriteResultDto.class))
            )
    })
    ApiResponse<ImageResponseDTO.FavoriteResultDto> toggleFavorite(
            @Parameter(hidden = true)
            @AuthenticationPrincipal PrincipalDetails principal,

            @Parameter(description = "이미지 ID", example = "55")
            @PathVariable Long imageId,

            @RequestBody ImageRequestDTO.FavoriteDto request
    );
}
