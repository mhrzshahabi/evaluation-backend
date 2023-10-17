package com.nicico.evaluation.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

@Configuration
@EnableAsync
public class AsyncConfig {
    @Value("${thread.core.poolSize:2}")
    private Integer corePoolSize;

    @Value("${thread.core.max.poolSize:2}")
    private Integer maxPoolSize;

    @Value("${thread.core.queue.capacity:5}")
    private Integer queueCapacity;

    @Lazy
    @Bean("threadPoolAsync")
    public Executor taskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(corePoolSize);
        executor.setMaxPoolSize(maxPoolSize);
        executor.setQueueCapacity(queueCapacity);
        executor.setThreadNamePrefix("AsyncMsgExecutor-");
        executor.initialize();
        return executor;
    }
}
