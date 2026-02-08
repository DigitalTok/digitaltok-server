package com.digital_tok.device.controller;

import com.digital_tok.device.dto.DeviceRequestDTO;
import com.digital_tok.device.dto.DeviceResponseDTO;
import com.digital_tok.global.apiPayload.ApiResponse;
import com.digital_tok.global.apiPayload.code.SuccessCode;
import com.digital_tok.device.service.DeviceService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import jakarta.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/devices")
@Tag(name = "Device", description = "기기 관련 API (상태 조회, 연결, 해제")
public class DeviceController {

    private final DeviceService deviceService;

    /**
     * 기기 연결 API
     */
    @PostMapping
    @Operation(summary = "기기 연결 API", description = "기기를 연결합니다(NFC UID 기반).")
    public ApiResponse<DeviceResponseDTO.Result> connectDevice(
            @RequestBody @Valid DeviceRequestDTO request
    ) {
        DeviceResponseDTO.Result result = deviceService.connectDevice(request);
        return ApiResponse.onSuccess(SuccessCode.DEVICE_CONNECT_SUCCESS, result);
    }

    /**
     * 기기 연결 해제 API
     */
    @DeleteMapping("/{nfcUid}")
    @Operation(summary = "기기 연결 해제 API", description = "기기와의 연결을 해제합니다 (NFC UID 기반).")
    public ApiResponse<DeviceResponseDTO.Result> disconnectDevice(
            @PathVariable String nfcUid
    ) {
        DeviceResponseDTO.Result result = deviceService.disconnectDevice(nfcUid);
        return ApiResponse.onSuccess(SuccessCode.DEVICE_DISCONNECT_SUCCESS, result);
    }

    /**
     * 기기 상태 조회 API
     */
    @GetMapping("/{nfcUid}")
    @Operation(summary = "기기 상태 조회 API", description = "기기의 연결 상태를 조회합니다 (NFC UID 기반).")
    public ApiResponse<DeviceResponseDTO.Result> getDeviceStatus(
            @PathVariable String nfcUid
    ) {
        DeviceResponseDTO.Result result = deviceService.getDeviceStatus(nfcUid);
        return ApiResponse.onSuccess(SuccessCode.DEVICE_STATUS_SUCCESS, result);
    }
}
