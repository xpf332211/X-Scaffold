package com.meiya.init;

import com.alibaba.fastjson.JSON;
import com.meiya.entity.po.UserPo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * @author xiaopf
 */
@Component
@Slf4j
public class ApplicationInit implements ApplicationListener<ApplicationReadyEvent> {

    private final Map<String, InitFunction> initMap = new HashMap<>();
    {
        initMap.put("initJson",this::initJson);
        initMap.put("initHttpClient",this::initHttpClient);
        initMap.put("initThreadPool",this::initThreadPool);
    }


    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        initMap.forEach((desc,initFunction) -> {
            try{
                long start = System.currentTimeMillis();
                initFunction.invoke();
                long end = System.currentTimeMillis();
                log.info("【{}】.costTime:【{}】ms",desc,end-start);
            }catch (Exception e){
                log.error("【{}】.error:",desc,e);
            }
        });
    }


    /**
     * 预热json
     */
    private void initJson(){
        UserPo po = UserPo.builder().id(1L).name("test").age(18).build();
        String jsonString = JSON.toJSONString(po);
        UserPo userPo = JSON.parseObject(jsonString, UserPo.class);
    }

    /**
     * 预热httpClient
     */
    private void initHttpClient(){

    }

    /**
     * 预热线程池
     */
    private void initThreadPool(){

    }

    /**
     * 函数式接口
     */
    interface InitFunction{
        /**
         * 调用this.xxx()方法
         */
        void invoke();
    }
}
