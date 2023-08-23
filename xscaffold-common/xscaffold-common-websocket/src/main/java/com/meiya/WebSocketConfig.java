package com.meiya;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;

/**
 * @author xiaopf
 */
@Configuration
public class WebSocketConfig {

    /**
     * 用于支持WebSocket的自动注册和管理。
     * 它会自动扫描带有@ServerEndpoint注解的类，并将它们注册成为WebSocket的端点
     * @return 实例对象
     */
    @Bean
    public ServerEndpointExporter serverEndpointExporter(){
        return new ServerEndpointExporter();
    }
}
