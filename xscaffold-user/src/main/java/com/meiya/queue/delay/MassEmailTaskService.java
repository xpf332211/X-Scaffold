package com.meiya.queue.delay;

import com.alibaba.fastjson.JSON;
import com.meiya.util.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.Date;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author xiaopf
 */
@Slf4j
@Service
public class MassEmailTaskService {

    private static final String MASS_EMAIL_TASK_KEY = "massEmailTask";
    @Resource
    private RedisUtil redisUtil;

    /**
     * 将任务加入队列
     * @param massEmailTask 群发邮件任务
     */
    public void pushTaskToQueue(MassEmailTask massEmailTask){
        Date startTime = massEmailTask.getStartTime();
        Long taskId = massEmailTask.getTaskId();
        if (taskId == null || startTime == null || startTime.compareTo(new Date()) <= 0){
            return;
        }

        redisUtil.zAdd(MASS_EMAIL_TASK_KEY,massEmailTask.getTaskId().toString(),startTime.getTime());
        log.info("定时任务【{}】加入redis延迟队列", JSON.toJSONString(massEmailTask));
    }

    /**
     * 将到达执行时间的任务拉出
     * @return 到达执行时间的群发邮件任务
     */
    public Set<Long> pullTaskFromQueue(){
        Set<String> taskIdSet = redisUtil.rangeByScore(MASS_EMAIL_TASK_KEY, 0, System.currentTimeMillis());
        if (CollectionUtils.isEmpty(taskIdSet)){
            return Collections.emptySet();
        }
        Set<Long> ids = taskIdSet.stream().map(Long::parseLong).collect(Collectors.toSet());
        redisUtil.removeZsetList(MASS_EMAIL_TASK_KEY,taskIdSet);
        return ids;
    }

}
