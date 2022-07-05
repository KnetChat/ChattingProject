package com.knet.chattingproject.domain.Chat;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.config.annotation.EnableWebSocket;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

@Slf4j
@Component
@ServerEndpoint("/chat")
public class Chat {
    public static Set<Session> sessionSet = new CopyOnWriteArraySet<>();

    @OnOpen
    public void onOpen(Session session){
        log.info("Open : " + session.toString());
        sessionSet.add(session);
    }

    @OnClose
    public void onClose(Session session){
        log.info("Close : " + session.toString());
        sessionSet.remove(session);
    }

    @OnError
    public void onError(Session session){
        log.warn("Error : " + session.toString());
        sessionSet.remove(session);
    }


    @OnMessage
    public void onMessage(String message, Session session){
        log.info("Send Message : " + message);
        try {
            sendMessage(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendMessage(String message) throws IOException {
        for(Session session : sessionSet){
            session.getBasicRemote().sendText(message);
        }
    }
}
