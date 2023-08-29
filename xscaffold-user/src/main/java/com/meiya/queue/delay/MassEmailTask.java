package com.meiya.queue.delay;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * 群发邮件任务
 * @author xiaopf
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MassEmailTask {
    private Long taskId;
    private Date startTime;
}
