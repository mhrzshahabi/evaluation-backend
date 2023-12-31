package com.nicico.evaluation.websocket;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.nicico.evaluation.dto.WebSocketDTO;
import com.nicico.evaluation.iservice.IBatchService;
import lombok.SneakyThrows;
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
    public void afterConnectionEstablished(WebSocketSession session) {

        System.out.println("connected");
        String id = sessionsManager.getSessionId(session);
        sessionsManager.add(id, session);
        Thread thread = new Thread() {
            @SneakyThrows
            @Override
            public void run() {
                while (session.isOpen()) {
                    try {
                        List<WebSocketDTO> webSocketDTOList = batchService.getForNotificationPanel();
                        session.sendMessage(new TextMessage(new Gson().toJson(webSocketDTOList, new TypeToken<List<WebSocketDTO>>() {
                        }.getType())));
                        Thread.sleep(5000);
                    } catch (InterruptedException | IOException ie) {
                        session.sendMessage(new TextMessage("مشکلی پیش آمده است"));
                    }
                }
            }
        };
        thread.start();
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        System.out.println("Connection Closed！" + status);
        sessionsManager.remove(sessionsManager.getSessionId(session));
    }

}