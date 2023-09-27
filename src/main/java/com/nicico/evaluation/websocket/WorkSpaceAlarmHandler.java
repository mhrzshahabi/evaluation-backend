package com.nicico.evaluation.websocket;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.nicico.evaluation.dto.WorkSpaceDTO;
import com.nicico.evaluation.iservice.IWorkSpaceService;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.List;

public class WorkSpaceAlarmHandler extends TextWebSocketHandler {

    @Autowired
    private ObjectMapper mapper;

    @Autowired
    private SessionsManager sessionsManager;

    @Autowired
    private IWorkSpaceService workSpaceService;

    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        System.out.println("connected");
        String id = sessionsManager.getAlarmSessionId(session);
        sessionsManager.add(id, session);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        System.out.println("Connection Closed！" + status);
        sessionsManager.remove(sessionsManager.getAlarmSessionId(session));
    }

    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message) throws IOException {

        for (WebSocketSession socketSession : sessionsManager.getSessions().values()) {
            if (socketSession.equals(session)) {
                List<String> workSpaceCodeList = mapper.readValue(message.getPayload(), new TypeReference<>() {
                });
                Thread thread = new Thread() {
                    @SneakyThrows
                    @Override
                    public void run() {
                        while (session.isOpen()) {
                            try {
                                String token = session.getUri().getQuery().split("token=")[1];
                                List<WorkSpaceDTO.Info> workSpaceAlarmList = workSpaceService.workSpaceAlarmNotification(workSpaceCodeList, token);
                                session.sendMessage(new TextMessage(new Gson().toJson(workSpaceAlarmList, new TypeToken<List<WorkSpaceDTO.Info>>() {
                                }.getType())));
                                Thread.sleep(10000);
                            } catch (InterruptedException | IOException ie) {
                                session.sendMessage(new TextMessage("مشکلی پیش آمده است"));
                            }
                        }
                    }
                };
                thread.start();
            }
        }
    }

}