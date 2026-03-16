import http from 'k6/http';
import { check, sleep } from 'k6';

export const options = {
    vus: 100,
    duration: '30s',
};

export function setup() {
    const loginUrl = 'https://www.diring.site/api/v1/auth/login';
    const payload = JSON.stringify({
        email: 'user@example.com',  // 1. 반드시 DB에 있는 계정이어야 함
        password: 'string',     // 2. 비밀번호가 맞는지 확인
    });

    const params = {
        headers: { 'Content-Type': 'application/json' },
    };

    const res = http.post(loginUrl, payload, params);

    // [디버깅 코드 추가] 응답이 비어있거나 에러가 났는지 확인
    console.log(`[Setup] Status: ${res.status}`);
    console.log(`[Setup] Body: ${res.body}`);

    if (res.status !== 200 || !res.body) {
        console.error('로그인 실패! 서버 로그(IntelliJ)를 확인하세요.');
        return { token: null };
    }

    try {
        const responseBody = res.json();
        // ApiResponse 구조에 맞게 접근: responseBody.result.accessToken
        if (responseBody && responseBody.result && responseBody.result.accessToken) {
            return { token: responseBody.result.accessToken };
        }
    } catch (e) {
        console.error(`JSON 파싱 실패: ${e.message}. 응답 내용: ${res.body}`);
    }

    return { token: null };
}

export default function (data) {
    if (!data || !data.token) {
        // 토큰이 없으면 테스트를 수행하지 않음
        return;
    }

    const url = 'https://www.diring.site/api/v1/templates/subway';
    const params = {
        headers: {
            'Authorization': `Bearer ${data.token}`,
            'Content-Type': 'application/json',
        },
    };

    const res = http.get(url, params);
    check(res, { 'status is 200': (r) => r.status === 200 });
    sleep(0.5);
}