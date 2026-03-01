package com.digital_tok.device.controller;

import com.digital_tok.device.dto.DeviceRequestDTO;
import com.digital_tok.device.dto.DeviceResponseDTO;
import com.digital_tok.global.apiPayload.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import jakarta.validation.Valid;

@Tag(name = "Device", description = "기기 관련 API (상태 조회, 연결, 해제)")
public interface DeviceControllerDocs {

    @Operation(summary = "기기 연결 API By 조성하(개발 완료)", description = "기기를 연결합니다 (NFC UID 기반). 사전에 등록된 기기만 등록할 수 있습니다.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "연결 성공",
                    content = @Content(schema = @Schema(implementation = DeviceResponseDTO.Result.class))
            )
    })
    ApiResponse<DeviceResponseDTO.Result> connectDevice(@RequestBody @Valid DeviceRequestDTO request);

    @Operation(summary = "기기 연결 해제 API By 조성하(개발 완료)", description = "기기와의 연결을 해제합니다 (NFC UID 기반).")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "연결 해제 성공",
                    content = @Content(schema = @Schema(implementation = DeviceResponseDTO.Result.class))
            )
    })
    ApiResponse<DeviceResponseDTO.Result> disconnectDevice(@PathVariable String nfcUid);

    @Operation(summary = "기기 상태 조회 API By 조성하(개발 완료)", description = "기기의 연결 상태를 조회합니다 (NFC UID 기반).")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "상태 조회 성공",
                    content = @Content(schema = @Schema(implementation = DeviceResponseDTO.Result.class))
            )
    })
    ApiResponse<DeviceResponseDTO.Result> getDeviceStatus(@PathVariable String nfcUid);
}
