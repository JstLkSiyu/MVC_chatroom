package chat.controller;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentSkipListMap;

@CrossOrigin(origins = "*", maxAge = 3600)
@Controller
@ServerEndpoint("/websocket/{token}")
public class WebsocketController {

    private enum msgType {
        MESSAGE, FRIEND_QUERY, SYSTEM
    }

    public static ConcurrentSkipListMap<String, WebsocketController> webSocketMap = new ConcurrentSkipListMap<>();

    private Session session;
    private String token;

    public static void sendGroupInfo(String message) {
        for(Map.Entry<String, WebsocketController> entry : webSocketMap.entrySet()) {
            WebsocketController value = entry.getValue();
            value.sendMessage(message);
        }
    }

    public static void sendGroupObject(Object obj) {
        for(Map.Entry<String, WebsocketController> entry : webSocketMap.entrySet()) {
            WebsocketController value = entry.getValue();
            value.sendObject(obj);
        }
    }

    public void sendObject(Object obj) {
        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        String s = null;
        try {
            s = mapper.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        sendMessage(s);
    }

    public void sendMessage(String message) {
        System.out.println("Send Message--------------------");
        try {
            session.getBasicRemote().sendText(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @OnOpen
    public void onOpen(Session session, @PathParam("token") String token) {
        System.out.println("Open----------------------------");
        this.session = session;
        this.token = token;
        webSocketMap.put(token, this);
        Map<String, Object> json = new HashMap<>();
        json.put("type", msgType.SYSTEM);
        json.put("msg", "当前在线人数" + webSocketMap.size());
        sendObject(json);
        json.clear();
        json.put("type", msgType.SYSTEM);
        json.put("msg", "欢迎" + token + "上线");
        sendGroupObject(json);
    }

    @OnError
    public void onError(Session session, Throwable error) {
        System.out.println("Error----------------------------");
        error.printStackTrace();
    }

    @OnMessage
    public void onMessage(String message, Session session) {
        System.out.println("onMessage------------------------");
        System.out.println(message);
    }

    @OnClose
    public void onClose(@PathParam("token") String token) {
        System.out.println("Close------------------------------");
        webSocketMap.remove(token);
    }

    public static ConcurrentSkipListMap<String, WebsocketController> getWebSocketMap() {
        return webSocketMap;
    }
}
