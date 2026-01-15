package com.digital_tok.controller;

import com.digital_tok.dto.request.DeviceRequestDTO;
import com.digital_tok.dto.response.DeviceResponseDTO;
import com.digital_tok.global.apiPayload.ApiResponse;
import com.digital_tok.global.apiPayload.code.SuccessCode;
import com.digital_tok.global.DeviceStatus;

import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import jakarta.validation.Valid;
import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/devices")
@Tag(name = "Device", description = "기기 관련 API (상태 조회, 연결, 해제 등)")
public class DeviceController {

    /**
     * 기기 상태 조회 API
     */
    @GetMapping("/{deviceId}")
    @Operation(summary = "기기 상태 조회 API", description = "기기의 ID를 사용하여 연결 상태를 조회합니다.")
    public ApiResponse<DeviceResponseDTO.Result> getDeviceStatus(@PathVariable Long deviceId) {
        // TODO: 실제 서비스 로직 구현 필요 (더미 데이터 반환)
        DeviceResponseDTO.Result result = DeviceResponseDTO.Result.builder()
                .deviceId(deviceId)
                .status(DeviceStatus.ACTIVE.name()) // 실제 서비스에서는 DB에서 가져오거나 비즈니스 로직 적용
                .build();

        return ApiResponse.onSuccess(SuccessCode.DEVICE_STATUS_SUCCESS, result);
    }

    /**
     * 기기 연결 API
     */
    @PostMapping
    @Operation(summary = "기기 연결 API", description = "기기를 연결하며, 기기의 ID를 Request Body로 전달합니다.")
    public ApiResponse<DeviceResponseDTO.Result> connectDevice(@RequestBody @Valid DeviceRequestDTO request) {
        // TODO: 실제 서비스 로직 구현 필요 (더미 데이터 반환)
        DeviceResponseDTO.Result result = DeviceResponseDTO.Result.builder()
                .deviceId(request.getDeviceId())
                .registeredAt(LocalDateTime.now()) // 실시간 등록 시간 반영
                .status(DeviceStatus.ACTIVE.name())
                .build();

        return ApiResponse.onSuccess(SuccessCode.DEVICE_CONNECT_SUCCESS, result);
    }

    /**
     * 기기 연결 해제 API
     */
    @DeleteMapping("/{deviceId}")
    @Operation(summary = "기기 연결 해제 API", description = "기기의 ID를 사용하여 연결 상태를 INACTIVE로 변경합니다.")
    public ApiResponse<DeviceResponseDTO.Result> disconnectDevice(@PathVariable Long deviceId) {
        // TODO: 실제 서비스 로직 구현 필요 (더미 데이터 반환)
        DeviceResponseDTO.Result result = DeviceResponseDTO.Result.builder()
                .deviceId(deviceId)
                .unregisteredAt(LocalDateTime.now()) // 실시간 연결 해제 시간 반영
                .status(DeviceStatus.INACTIVE.name())
                .build();

        return ApiResponse.onSuccess(SuccessCode.DEVICE_DISCONNECT_SUCCESS, result);
    }
}
