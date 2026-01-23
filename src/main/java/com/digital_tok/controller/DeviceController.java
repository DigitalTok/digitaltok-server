package com.digital_tok.controller;

import com.digital_tok.dto.request.DeviceRequestDTO;
import com.digital_tok.dto.response.DeviceResponseDTO;
import com.digital_tok.global.apiPayload.ApiResponse;
import com.digital_tok.global.apiPayload.code.SuccessCode;
import com.digital_tok.service.DeviceService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import jakarta.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/devices")
@Tag(name = "Device", description = "기기 관련 API (상태 조회, 연결, 해제 등)")
public class DeviceController {

    private final DeviceService deviceService;

    /**
     * 기기 연결 API
     */
    @PostMapping("/connect")
    @Operation(summary = "기기 연결 API", description = "기기를 연결합니다 (임시).")
    public ApiResponse<DeviceResponseDTO.Result> connectDevice(
            @RequestBody @Valid DeviceRequestDTO request
    ) {
        DeviceResponseDTO.Result result = deviceService.connectDevice(request);
        return ApiResponse.onSuccess(SuccessCode.DEVICE_CONNECT_SUCCESS, result);
    }

    /**
     * 기기 연결 해제 API
     */
    @DeleteMapping("/{deviceId}/disconnect")
    @Operation(summary = "기기 연결 해제 API", description = "기기와의 연결을 해제합니다 (임시).")
    public ApiResponse<DeviceResponseDTO.Result> disconnectDevice(
            @PathVariable Long deviceId
    ) {
        DeviceResponseDTO.Result result = deviceService.disconnectDevice(deviceId);
        return ApiResponse.onSuccess(SuccessCode.DEVICE_DISCONNECT_SUCCESS, result);
    }

    /**
     * 기기 상태 조회 API
     */
    @GetMapping("/{deviceId}/status")
    @Operation(summary = "기기 상태 조회 API", description = "기기의 연결 상태를 조회합니다 (임시).")
    public ApiResponse<DeviceResponseDTO.Result> getDeviceStatus(
            @PathVariable Long deviceId
    ) {
        DeviceResponseDTO.Result result = deviceService.getDeviceStatus(deviceId);
        return ApiResponse.onSuccess(SuccessCode.DEVICE_STATUS_SUCCESS, result);
    }
}
