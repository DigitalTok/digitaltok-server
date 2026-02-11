package com.digital_tok.template.service.makeSubwayImage;

import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

@Service
public class Eink4ColorService {

    // 4색 전자잉크 기본 색상 정의
    private static final Color C_BLACK = Color.BLACK;
    private static final Color C_WHITE = Color.WHITE;
    private static final Color C_RED = Color.RED;
    private static final Color C_YELLOW = Color.YELLOW;

    // 캔버스 설정
    private static final int CANVAS_SIZE = 200;
    private static final int PADDING = 10; // 테두리 여백
    private static final int BORDER_THICKNESS = 9; // 테두리 두께
    private static final int CIRCLE_DIAMETER = 48; // 동그라미 크기

    public byte[] generatePatternImage(String nameKor, String nameEng, String lineName) throws IOException {

        BufferedImage image = new BufferedImage(CANVAS_SIZE, CANVAS_SIZE, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = image.createGraphics();

        // 1. 그래픽 품질 설정 (안티앨리어싱)
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        // 2. 배경 채우기 (흰색)
        g2d.setColor(C_WHITE);
        g2d.fillRect(0, 0, CANVAS_SIZE, CANVAS_SIZE);

        // 3. 호선별 색상(Paint) 결정 로직
        Paint linePaint = getPaintByLineName(lineName);

        // 4. 테두리 그리기 (정중앙 정렬)
        g2d.setPaint(linePaint);
        g2d.setStroke(new BasicStroke(BORDER_THICKNESS));

        // x, y, w, h
        float borderSize = CANVAS_SIZE - (PADDING * 2);
        g2d.draw(new RoundRectangle2D.Float(PADDING, PADDING, borderSize, borderSize, 25, 25));

        // 5. 레이아웃 좌표 계산 (시각적 중앙 정렬)
        int centerX = CANVAS_SIZE / 2;

        // 동그라미 위치 (상단에서 적당히 떨어뜨림)
        int circleY = 30;
        int circleX = centerX - (CIRCLE_DIAMETER / 2);

        // 6. 동그라미 그리기
        Ellipse2D.Double circle = new Ellipse2D.Double(circleX, circleY, CIRCLE_DIAMETER, CIRCLE_DIAMETER);
        g2d.fill(circle);

        // 7. 호선 번호 그리기 (동그라미 안)
        g2d.setColor(C_WHITE); // 숫자는 흰색
        g2d.setFont(new Font("Arial", Font.BOLD, 22));
        // 숫자를 동그라미 정중앙에 위치시키기 위한 보정값
        drawCenteredText(g2d, lineName, centerX, circleY + (CIRCLE_DIAMETER / 2) + 8);

        // 8. 한글 역명 그리기
        g2d.setColor(C_BLACK); // 글씨는 검정색
        try {
            // 리소스 폴더에서 폰트 파일 스트림 읽기 (경로 주의: /fonts/파일이름.ttf)
            InputStream fontStream = getClass().getResourceAsStream("/fonts/Pretendard-Bold.otf");

            Font korFont;
            if (fontStream != null) {
                // 1. 폰트 생성 (TRUETYPE_FONT)
                korFont = Font.createFont(Font.TRUETYPE_FONT, fontStream).deriveFont(Font.BOLD, 35f);
            } else {
                // 파일 로드 실패 시
                korFont = new Font("SansSerif", Font.BOLD, 35);
            }
            g2d.setFont(korFont);

        } catch (FontFormatException e) {
            // 폰트 형식이 잘못되었을 경우 안전장치
            g2d.setFont(new Font("SansSerif", Font.BOLD, 35));
        }
        int korY = circleY + CIRCLE_DIAMETER + 45; // 동그라미 아래로 50px 띄움
        drawCenteredText(g2d, nameKor, centerX, korY);

        // 9. 영어 역명 그리기
        g2d.setFont(new Font("Arial", Font.PLAIN, 18));
        int engY = korY + 30; // 한글 아래로 30px 띄움
        drawCenteredText(g2d, nameEng, centerX, engY);

        g2d.dispose();

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(image, "png", baos);
        return baos.toByteArray();
    }

    /**
     * 호선 이름에 따라 4색 전자잉크에 최적화된 Paint(단색 또는 패턴)를 반환
     */
    private Paint getPaintByLineName(String lineName) {
        if (lineName == null) return C_BLACK;
        String line = lineName.trim();

        // 2호선 & 7호선 (초록/국방색 계열) -> 검정 + 노랑 패턴 (올리브색 효과)
        if (line.contains("2") || line.contains("7")) {
            return createPattern(C_BLACK, C_YELLOW);
        }

        // 3호선 (주황색) -> 빨강 + 노랑 패턴
        if (line.contains("3")) {
            return createPattern(C_RED, C_YELLOW);
        }

        // 4호선 (하늘색) -> 파랑 표현, C_BLACK + C_WHITE 패턴
        if (line.contains("4")) {
            return createPattern(C_BLACK, C_WHITE);
        }

        // 6호선 (황토색/갈색) -> 빨강 + 노랑 + 검정 섞으면 좋지만 2색 패턴만 가능하므로 빨강+노랑(주황) 사용
        if (line.contains("6")) {
            return createPattern(C_RED, C_YELLOW);
        }

        // 8호선 (분홍색) -> 빨강 + 흰색 패턴
        if (line.contains("8")) {
            return createPattern(C_RED, C_WHITE);
        }

        // 9호선 (황금색/베이지) -> 노랑 단색 (가장 비슷함)
        if (line.contains("9")) {
            return C_YELLOW;
        }

        // 1, 5호선 포함, 그리고 그 외 -> 검정
        return C_BLACK;
    }

    /**
     * 2x2 픽셀의 체크무늬 패턴 생성 (2가지 색 섞어서 다른 색 만들기)
     */
    private TexturePaint createPattern(Color c1, Color c2) {
        BufferedImage patternImg = new BufferedImage(2, 2, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = patternImg.createGraphics();

        // (0,0) 색상1
        g.setColor(c1);
        g.fillRect(0, 0, 1, 1);

        // (1,1) 색상1
        g.fillRect(1, 1, 1, 1);

        // (1,0) 색상2
        g.setColor(c2);
        g.fillRect(1, 0, 1, 1);

        // (0,1) 색상2
        g.fillRect(0, 1, 1, 1);

        g.dispose();

        return new TexturePaint(patternImg, new Rectangle(0, 0, 2, 2));
    }

    private void drawCenteredText(Graphics g, String text, int x, int y) {
        if (text == null) return;
        FontMetrics metrics = g.getFontMetrics(g.getFont());
        int textWidth = metrics.stringWidth(text);
        // x좌표를 중심으로 텍스트 시작점 계산
        g.drawString(text, x - (textWidth / 2), y);
    }
}