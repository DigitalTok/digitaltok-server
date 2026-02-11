package com.digital_tok.template.service.makeSubwayImage;

import com.digital_tok.global.apiPayload.code.ErrorCode;
import com.digital_tok.global.apiPayload.exception.GeneralException;
import com.digital_tok.global.event.S3ImageRollbackEvent;
import com.digital_tok.image.service.processing.EinkBinaryEncoder;
import com.digital_tok.image.service.processing.EinkEncodingOption;
import com.digital_tok.image.service.processing.EinkQuantizer;
import com.digital_tok.template.dto.SubwayCreateRequestDTO;
import com.digital_tok.template.repository.SubwayTemplateRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;

@Slf4j
@Service
@RequiredArgsConstructor
public class SubwayTemplateUploadService { // 이미지 생성 후 S3에 업로드

    private final Eink4ColorService imageGenerator;
    private final S3UploadService s3Uploader;
    private final SubwayTemplateService subwayTemplateService;
    private final SubwayTemplateRepository subwayTemplateRepository;
    private final ApplicationEventPublisher eventPublisher;

    // 이미지 처리용 객체 생성
    private final EinkEncodingOption encodingOption = new EinkEncodingOption();
    private final EinkQuantizer quantizer = new EinkQuantizer();
    private final EinkBinaryEncoder binaryEncoder = new EinkBinaryEncoder(encodingOption);

    public Long createAndSaveSubwayTemplate(SubwayCreateRequestDTO request) {

        String nameKor = request.getStationName().trim();
        String nameEng = request.getStationNameEng().trim();
        String lineName = request.getLineName().trim();

        if (subwayTemplateRepository.existsByStationNameAndLineName(nameKor, lineName + "호선")) {
            throw new GeneralException(ErrorCode.TEMPLATE_ALREADY_EXISTS);
        }

        byte[] imageBytes = generateImage(nameKor, nameEng, lineName); // 1. 이미지 생성
        byte[] binaryBytes = generateBinaryData(imageBytes); // 2. 바이너리 데이터 변환

        // 3. S3 업로드
        String uploadedImageUrl = s3Uploader.upload(imageBytes, "template/subway", "png", "image/png");
        String uploadedDataUrl = s3Uploader.upload(binaryBytes, "template/subway/binary", "bin", "application/octet-stream");

        // 4. DB 저장 (DB 작업 -> 트랜잭션 -> 별도 호출)
        try {
            return subwayTemplateService.saveToDatabase(nameKor, nameEng, lineName, uploadedImageUrl, uploadedDataUrl);
        } catch (Exception e) {
            log.error("DB 저장 실패. S3 롤백 이벤트 발행: {}", uploadedImageUrl);
            eventPublisher.publishEvent(new S3ImageRollbackEvent(uploadedImageUrl));
            eventPublisher.publishEvent(new S3ImageRollbackEvent(uploadedDataUrl));

            throw e;
        }
    }

    private byte[] generateImage(String nameKor, String nameEng, String lineName) {

        try {
            return imageGenerator.generatePatternImage(nameKor, nameEng, lineName);
        } catch (IOException e) {
            log.error("지하철 템플릿 이미지 생성 중 오류 발생: {}", e.getMessage(), e);
            throw new GeneralException(ErrorCode.IMAGE_UPLOAD_FAIL);
        }
    }
    private byte[] generateBinaryData(byte[] imageBytes) {
        byte[] binaryBytes;
        try {
            // 2-1. byte[] -> BufferedImage 변환
            ByteArrayInputStream bais = new ByteArrayInputStream(imageBytes);
            BufferedImage originalImage = ImageIO.read(bais);

            // 2-2. 4색 양자화 (Quantization)
            // 텍스트/도형 위주의 템플릿이므로 Dithering은 OFF로 설정하여 깔끔하게 처리
            BufferedImage quantizedImage = quantizer.quantizeTo4Colors(originalImage, EinkQuantizer.DitherMode.OFF);

            // 2-3. 바이너리 인코딩 (.bin)
            binaryBytes = binaryEncoder.encode(quantizedImage);

        } catch (IOException e) {
            log.error("바이너리 데이터 변환 중 오류 발생: {}", e.getMessage(), e);
            throw new GeneralException(ErrorCode.IMAGE_TO_BINARY_ERROR);
        }
        return binaryBytes;
    }
}
