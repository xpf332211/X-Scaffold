package com.meiya.event;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.event.TransactionalEventListener;

/**
 * @author xiaopf
 */
@Service
@Slf4j
public class PersonEventListener {

    @TransactionalEventListener(fallbackExecution = true)
    public void listenerCreate(PersonChangeEvent personChangeEvent){
        switch (personChangeEvent.getOperateType()){
            case "create":
                //具体业务逻辑
                log.info("执行了创建事件:【{}】", JSON.toJSONString(personChangeEvent.getPerson()));
                break;
            default:
                break;
        }
    }
}
