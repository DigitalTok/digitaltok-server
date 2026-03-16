package com.digital_tok.global.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

@Configuration
@EnableAsync
public class AsyncConfig { // 비동기 스레드 풀 설정

    @Bean(name = "imageTaskExecutor")
    public Executor imageTaskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();

        // 1. Core: vCPU가 2개이므로 2개로 고정
        executor.setCorePoolSize(2);

        // 2. Max: RAM 부족을 고려해 최대 4개로 제한
        executor.setMaxPoolSize(4);

        // 3. Queue: 대기열이 너무 길면 메모리가 터질 수 있음 -> 30개 제한
        executor.setQueueCapacity(30);

        // 4. Reject Policy: 큐가 꽉 차면 직접 수행하게 함
        // -> 전체적인 시스템 속도를 물리적으로 제어(Throttle)합니다.
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());

        executor.setThreadNamePrefix("ImgAsync-");
        executor.initialize();
        return executor;
    }
}
