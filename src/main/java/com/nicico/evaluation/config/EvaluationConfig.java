package com.nicico.evaluation.config;

import org.modelmapper.ModelMapper;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource(value = "classpath:messages.properties", encoding = "UTF-8", ignoreResourceNotFound = true)
public class EvaluationConfig {
    @Bean
    @LoadBalanced
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }
}
