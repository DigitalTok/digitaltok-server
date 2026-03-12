import http from 'k6/http';
import { check, sleep } from 'k6';

// 1. 테스트에 사용할 이미지 파일을 미리 로드합니다. (스크립트와 같은 경로에 이미지 파일 필요)
const binFile = open('./Redis.png', 'b');

export const options = {
    vus: 15,
    duration: '30s',
};

export function setup() {
    const loginUrl = 'https://www.diring.site/api/v1/auth/login';
    const payload = JSON.stringify({
        email: 'user@example.com',
        password: 'string',
    });

    const params = {
        headers: { 'Content-Type': 'application/json' },
    };

    const res = http.post(loginUrl, payload, params);

    if (res.status !== 200 || !res.body) {
        console.error('로그인 실패');
        return { token: null };
    }

    try {
        const responseBody = res.json();
        if (responseBody && responseBody.result && responseBody.result.accessToken) {
            return { token: responseBody.result.accessToken };
        }
    } catch (e) {
        console.error(`JSON 파싱 실패: ${e.message}`);
    }

    return { token: null };
}

export default function (data) {
    if (!data || !data.token) return;

    // 2. API 경로 설정 (imageName은 쿼리 파라미터로 전달)
    const url = 'https://www.diring.site/api/v1/images?imageName=k6_test_image';

    // 3. multipart/form-data 본문 구성
    const formData = {
        file: http.file(binFile, 'test-image.jpg', 'image/jpeg'),
    };

    const params = {
        headers: {
            'Authorization': `Bearer ${data.token}`,
            // k6는 본문 데이터가 객체일 경우 자동으로 boundary를 포함한 multipart/form-data를 설정합니다.
        },
    };

    // 4. POST 요청 전송
    const res = http.post(url, formData, params);

    // 5. 응답 확인
    check(res, {
        'status is 200': (r) => r.status === 200,
        'has result': (r) => r.json().result !== undefined,
    });

    sleep(1);
}