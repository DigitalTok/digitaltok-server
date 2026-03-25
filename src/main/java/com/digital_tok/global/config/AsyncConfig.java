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

        // 1. Core
        executor.setCorePoolSize(16);

        // 2. Max
        executor.setMaxPoolSize(32);

        // 3. Queue: 대기열이 너무 길면 메모리가 터질 수 있음 -> 100개
        executor.setQueueCapacity(100);

        // 4. Reject Policy: 큐가 꽉 차면 요청을 거부하고 예외를 발생시킴
        // -> 클라이언트는 HTTP 429 (Too Many Requests)를 받고, 재시도를 할 수 있음
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.AbortPolicy());

        executor.setThreadNamePrefix("ImgAsync-");
        executor.initialize();
        return executor;
    }
}
