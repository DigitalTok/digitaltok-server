package com.digital_tok.service.image;

import com.digital_tok.service.image.processing.*;
import com.digital_tok.service.storage.StorageService;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

@Service
public class ImageDerivationService {

    private final StorageService storageService;

    private final EinkEncodingOption option = new EinkEncodingOption();
    private final EinkImagePreprocessor preprocessor = new EinkImagePreprocessor(option);
    private final EinkQuantizer quantizer = new EinkQuantizer();
    private final EinkPreviewRenderer previewRenderer = new EinkPreviewRenderer();
    private final EinkBinaryEncoder binaryEncoder = new EinkBinaryEncoder(option);

    public ImageDerivationService(StorageService storageService) {
        this.storageService = storageService;
    }

    /**
     * 원본 이미지 스트림을 받아
     * - e-ink preview PNG
     * - e-ink binary (.bin)
     * 를 생성하고 각각 업로드한 URL을 반환한다.
     */
    public Result derive(InputStream originalImageStream) {
        System.out.println("### storageService = " + storageService.getClass().getName());
        try {
            // 1) 원본 로드
            BufferedImage original = ImageIO.read(originalImageStream);
            if (original == null) {
                throw new IllegalArgumentException("Unsupported image format");
            }

            // 2) 200x200 맞추기
            BufferedImage resized = preprocessor.to200x200(original);

            // 3) 4색 양자화 (일단 디더링 OFF 권장)
            BufferedImage quantized =
                    quantizer.quantizeTo4Colors(resized, EinkQuantizer.DitherMode.FLOYD_STEINBERG);


            // 4) Preview PNG 생성
            byte[] previewPng = previewRenderer.renderPng(quantized);

            /*
            //디버그용)png 파일 확인
            java.nio.file.Path outDir = java.nio.file.Paths.get("debug-output");
            java.nio.file.Files.createDirectories(outDir);
            java.nio.file.Path outFile = outDir.resolve("preview_" + java.util.UUID.randomUUID() + ".png");
            java.nio.file.Files.write(outFile, previewPng);
            System.out.println("### PREVIEW PNG SAVED: " + outFile.toAbsolutePath());*/

            // 5) Eink binary 생성 (10000 bytes)
            byte[] einkBinary = binaryEncoder.encode(quantized);
            System.out.println("### BIN LEN=" + einkBinary.length);

            /*
            //debug용)binary파일 로컬에 저장
            java.nio.file.Path outBin = outDir.resolve("eink_" + java.util.UUID.randomUUID() + ".bin");
            java.nio.file.Files.write(outBin, einkBinary);
            System.out.println("### BIN SAVED: " + outBin.toAbsolutePath());

             */



            // 6) 업로드
            String baseKey = "images/eink/" + UUID.randomUUID();

            String previewUrl = storageService.uploadPreview(
                    previewPng,
                    baseKey + "_preview.png",
                    "image/png"
            );

            String einkDataUrl = storageService.uploadEink(
                    einkBinary,
                    baseKey + ".bin",
                    "application/octet-stream"
            );

            return new Result(previewUrl, einkDataUrl);

        } catch (IOException e) {
            throw new IllegalStateException("Failed to derive eink images", e);
        }
    }

    /**
     * 파생 결과 DTO
     */
    public record Result(
            String previewUrl,
            String einkDataUrl
    ) {}
}
