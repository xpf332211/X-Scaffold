package com.meiya;

import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.websocket.HandshakeResponse;
import javax.websocket.server.HandshakeRequest;
import javax.websocket.server.ServerEndpointConfig;
import java.util.List;
import java.util.Map;


@Component
/**
 * @author xiaopf
 * 继承ServerEndpointConfig.Configurator类,用于配置WebSocket端点的实例。
 * 它提供了一些自定义配置和处理此端点的方法
 */
public class WebSocketServerConfig extends ServerEndpointConfig.Configurator {


    /**
     * 检查连接来源是否合法
     */
    @Override
    public boolean checkOrigin(String originHeaderValue) {
        ServletRequestAttributes requestAttributes =
                (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        assert requestAttributes != null;
        HttpServletRequest request = requestAttributes.getRequest();
        //进行校验
        return true;
    }

    /**
     * 在WebSocket握手过程中，客户端会发送一个HTTP请求给服务器，包含一些信息，例如请求头、Cookies等。
     * 服务器在处理这个握手请求时，可以使用modifyHandshake方法对请求进行自定义的修改。
     */
    @Override
    public void modifyHandshake(ServerEndpointConfig sec, HandshakeRequest request, HandshakeResponse response) {
        Map<String, List<String>> parameterMap = request.getParameterMap();
        List<String> erpList = parameterMap.get("erp");
        if (!CollectionUtils.isEmpty(erpList)){
            Map<String, Object> userProperties = sec.getUserProperties();
            userProperties.put("erp",erpList.get(0));
        }
    }
}
