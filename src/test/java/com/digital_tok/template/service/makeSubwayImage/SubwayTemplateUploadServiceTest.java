package com.digital_tok.template.service.makeSubwayImage;

import com.digital_tok.template.dto.SubwayCreateRequestDTO;
import com.digital_tok.template.repository.SubwayTemplateRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@SpringBootTest
class SubwayTemplateUploadServiceTest {

    @Autowired
    private SubwayTemplateUploadService subwayTemplateUploadService;

    @MockitoBean
    private S3UploadService s3UploadService;
    @MockitoBean
    private Eink4ColorService eink4ColorService;
    @MockitoBean
    private SubwayTemplateRepository subwayTemplateRepository;
    @MockitoBean
    private SubwayTemplateService subwayTemplateService;

    @Test
    @DisplayName("이미지 생성 후 DB 저장 실패 시 S3 파일 삭제(롤백) 메서드가 호출되어야 한다")
    void rollbackTest() throws Exception {
        // given
        String fakeUrl = "https://s3.aws.com/test.png";

        // 1. 이미지 데이터 생성
        BufferedImage mockImage = new BufferedImage(200, 200, BufferedImage.TYPE_INT_ARGB);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(mockImage, "png", baos);
        byte[] validImageBytes = baos.toByteArray(); // 유효한 PNG 바이트 배열

        given(eink4ColorService.generatePatternImage(any(), any(), any()))
                .willReturn(validImageBytes);

        given(s3UploadService.upload(any(byte[].class), any(), any(), any()))
                .willReturn(fakeUrl);

        doThrow(new RuntimeException("DB 저장 실패"))
                .when(subwayTemplateService)
                .saveToDatabase(any(), any(), any(), any(), any());

        // when
        try {
            SubwayCreateRequestDTO request = SubwayCreateRequestDTO.builder()
                    .stationName("강남")
                    .stationNameEng("gangnam")
                    .lineName("2")
                    .build();

            subwayTemplateUploadService.createAndSaveSubwayTemplate(request);
        } catch (Exception e) {
            System.out.println("예상된 에러 발생: " + e.getMessage());
        }

        // then
        verify(s3UploadService, timeout(2000).times(2)).deleteFile(fakeUrl);
    }
}