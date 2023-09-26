package com.nicico.evaluation.websocket;

import lombok.Data;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.socket.WebSocketSession;

import java.util.*;

@Component
@Data
public class SessionsManager {

    private Map<String, WebSocketSession> sessions;

    public SessionsManager() {
        sessions = Collections.synchronizedMap(new HashMap<>());
    }

    public void add(String id, WebSocketSession webSocketSession) {
        sessions.put(id, webSocketSession);
    }

    public void remove(String id) {
        sessions.remove(id);
    }

    public Map<String, WebSocketSession> getSessions() {
        return sessions;
    }

    public String getSessionId(WebSocketSession session) {
        return new AntPathMatcher()
                .extractPathWithinPattern("/anonymous/evaluation-ws/{id}/**", session.getUri().getPath());
    }

    public String getAlarmSessionId(WebSocketSession session) {
        return new AntPathMatcher()
                .extractPathWithinPattern("/evaluation-ws-alarm/{id}/**", session.getUri().getPath());
    }
}