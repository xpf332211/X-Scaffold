package com.meiya.websocket;

import com.meiya.WebSocketServerConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author xiaopf
 * ServerEndpoint注解使该类注册成websocket的一个端点
 */
@Slf4j
@Component
@ServerEndpoint(value = "/user/socket",configurator = WebSocketServerConfig.class)
public class UserWebSocket {

    /**
     * 记录当前在线连接数
     */
    private static final AtomicInteger ONLINE_COUNT = new AtomicInteger(0);

    /**
     * 存放所有的在线客户端 key为erp
     */
    private static final Map<String,UserWebSocket> CLIENTS = new ConcurrentHashMap<>();

    /**
     * 某个客户端连接的session会话
     */
    private Session session;

    /**
     * 表示客户端的key
     */
    private String erp;

    /**
     * 使用@OnOpen标注的方法会在WebSocket连接建立时被调用。
     * 可以在该方法中执行一些初始化操作，如记录连接日志、向用户发送欢迎消息等。
     */
    @OnOpen
    public void onOpen(Session session, EndpointConfig config) throws IOException {
        Map<String, Object> userProperties = config.getUserProperties();
        String erpName = (String) userProperties.get("erp");
        this.session = session;
        this.erp = erpName;
        //建立连接时，如果其他端有登录，应该退出
        if (CLIENTS.containsKey(this.erp)){
            //关闭其他端的会话
            CLIENTS.get(this.erp).session.close();
            //清除缓存
            CLIENTS.remove(this.erp);
            //连接数减一
            ONLINE_COUNT.decrementAndGet();
        }
        //连接
        CLIENTS.put(this.erp,this);
        ONLINE_COUNT.incrementAndGet();
        log.info("有新连接加入！当前在线人数【{}】",ONLINE_COUNT.get());
        sendMessage(session,"欢迎用户" + this.erp + "登录！");

    }

    /**
     * 使用@OnClose标注的方法会在WebSocket连接关闭时被调用。
     * 可以在该方法中进行一些收尾工作，如资源释放、清理操作等。
     */
    @OnClose
    public void onClone() throws IOException {
        if (CLIENTS.containsKey(this.erp)){
            //关闭其他端的会话
            CLIENTS.get(this.erp).session.close();
            //清除缓存
            CLIENTS.remove(this.erp);
            //连接数减一
            ONLINE_COUNT.decrementAndGet();
            log.info("用户【{}】连接关闭,当前在线人数【{}】",this.erp,ONLINE_COUNT.get());
        }
    }

    /**
     * 使用@OnMessage标注的方法会在接收到客户端发送的消息时被调用。
     * 可以在该方法中处理接收到的消息，并根据需要对客户端作出相应的响应。
     */
    @OnMessage
    public void onMessage(Session session,String message) throws IOException {
        log.info("服务端收到客户端【{}】发来的消息【{}】",erp,message);
        //一段时间不操作会断开连接 需要心跳机制 由前端实现
        if ("ping".equals(message)){
            this.sendMessage(session,"pong");
        }
    }






    @OnError
    public void onError(Session session,Throwable throwable) throws IOException {
        log.error("用户【{}】操作websocket发生错误,错误原因【{}】",erp,throwable.getMessage(),throwable);
        session.close();
    }


    private void sendMessage(Session session,String message) throws IOException {
        session.getBasicRemote().sendText(message);
        log.info("服务端向客户端【{}】发送消息【{}】",erp,message);
    }

    private void sendMessageForAll(String message) throws IOException {
        for (Map.Entry<String,UserWebSocket> entry : CLIENTS.entrySet()){
            String erpName = entry.getKey();
            UserWebSocket webSocket = entry.getValue();
            webSocket.session.getBasicRemote().sendText(message);
            log.info("服务端向客户端【{}】发送消息【{}】",erpName,message);
        }
    }

}
