package com.nicico.evaluation.websocket;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.nicico.evaluation.dto.WebSocketDTO;
import com.nicico.evaluation.iservice.IBatchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.socket.*;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.List;

public class GreetingHandler extends TextWebSocketHandler {

    @Autowired
    private SessionsManager sessionsManager;

    @Autowired
    private IBatchService batchService;

    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message) throws IOException {

        System.out.println("uri=" + session.getUri());
        System.out.println("uri=" + session.getRemoteAddress());
        System.out.println("here=" + message.getPayload());

        for (WebSocketSession socketSession : sessionsManager.getSessions().values()) {
            if (!socketSession.equals(session)) {
                socketSession.sendMessage(new TextMessage(message.getPayload() + "-" + System.currentTimeMillis()));
            }
        }
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {

        String id = sessionsManager.getSessionId(session);
        List<WebSocketDTO> webSocketDTOList = batchService.getForNotificationPanel();
        System.out.println("connected");
        session.sendMessage(new TextMessage(new Gson().toJson(webSocketDTOList, new TypeToken<List<WebSocketDTO>>() {
        }.getType())));
        sessionsManager.add(id, session);
//        sessionsManager.getSessions();
//        session.close();
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        System.out.println("Connection Closed！" + status);
        sessionsManager.remove(sessionsManager.getSessionId(session));
//        sessionsManager.getSessions();
    }
}