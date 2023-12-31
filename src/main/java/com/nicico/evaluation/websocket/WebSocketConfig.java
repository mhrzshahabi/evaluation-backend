package com.nicico.evaluation.websocket;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@Lazy
@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(greetingHandler(), "/anonymous/evaluation-ws/{id}").setAllowedOrigins("*");
        registry.addHandler(workSpaceAlarmHandler(), "/anonymous/evaluation-ws-alarm/{id}").setAllowedOrigins("*");
    }

    @Bean
    public WebSocketHandler greetingHandler() {
        return new GreetingHandler();
    }

    @Bean
    public WebSocketHandler workSpaceAlarmHandler() {
        return new WorkSpaceAlarmHandler();
    }
}