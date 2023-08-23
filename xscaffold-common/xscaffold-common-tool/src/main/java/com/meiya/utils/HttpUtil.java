package com.meiya.utils;



import org.apache.commons.lang3.tuple.Pair;
import lombok.extern.slf4j.Slf4j;
import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.config.RequestConfig;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManager;
import org.apache.hc.client5.http.socket.ConnectionSocketFactory;
import org.apache.hc.client5.http.socket.PlainConnectionSocketFactory;
import org.apache.hc.client5.http.ssl.SSLConnectionSocketFactory;
import org.apache.hc.core5.http.ParseException;
import org.apache.hc.core5.http.config.Registry;
import org.apache.hc.core5.http.config.RegistryBuilder;
import org.apache.hc.core5.http.io.SocketConfig;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.apache.hc.core5.util.TimeValue;
import org.apache.hc.core5.util.Timeout;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * @author xiaopf
 */
@Slf4j
public class HttpUtil {
    /**
     * 使用 CloseableHttpClient 可以创建一个 HTTP 客户端实例，并执行 GET、POST、PUT 等各种类型的 HTTP 请求。
     * 通过 CloseableHttpClient，你可以设置请求头、请求体、超时时间等参数，并获取服务器返回的响应。
     */
    static CloseableHttpClient httpClient;

    static {
        //为不同的协议提供适当的连接套接字工厂
        Registry<ConnectionSocketFactory> registry = RegistryBuilder.<ConnectionSocketFactory>create()
                .register("http", PlainConnectionSocketFactory.getSocketFactory())
                .register("https", SSLConnectionSocketFactory.getSocketFactory())
                .build();
        //创建http连接池 并设置参数
        PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager(registry);
        connectionManager.setMaxTotal(500);
        connectionManager.setDefaultMaxPerRoute(500);
        connectionManager.setDefaultSocketConfig(
                SocketConfig.custom()
                        .setSoTimeout(15, TimeUnit.SECONDS)
                        .setTcpNoDelay(true)
                        .build()
        );
        connectionManager.setValidateAfterInactivity(TimeValue.ofSeconds(15));

        //创建请求配置对象
        RequestConfig requestConfig = RequestConfig.custom()
                .setConnectTimeout(Timeout.ofSeconds(1))
                .setConnectionRequestTimeout(Timeout.ofSeconds(1))
                .setResponseTimeout(Timeout.ofSeconds(1))
                .build();
        //配置CloseableHttpClient对象
        httpClient = HttpClients.custom()
                .setConnectionManager(connectionManager)
                .setDefaultRequestConfig(requestConfig)
                .disableAutomaticRetries()
                .build();
    }

    public static String post(String url, List<Pair<String,String>> pairList,
                              Map<String,String> headers) throws IOException, ParseException {
        url = url + "?" + buildParam(pairList);
        HttpPost httpPost = new HttpPost(url);
        if (Objects.nonNull(headers) && headers.size() > 0){
            headers.forEach(httpPost::addHeader);
        }
        try(CloseableHttpResponse response = httpClient.execute(httpPost)) {
            return EntityUtils.toString(response.getEntity());
        }
    }

    public static String buildParam(List<Pair<String,String>> pairList){
        StringBuilder stringBuilder = new StringBuilder();
        for (Pair<String, String> pair : pairList) {
            stringBuilder.append(pair.getKey())
                    .append("=")
                    .append(pair.getValue())
                    .append("&");
        }
        stringBuilder.setLength(stringBuilder.length() - 1);
        return stringBuilder.toString();
    }

}
