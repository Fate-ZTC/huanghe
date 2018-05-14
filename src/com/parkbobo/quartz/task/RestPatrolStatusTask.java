package com.parkbobo.quartz.task;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * @author lijunhong
 * @since 18/5/12 下午5:22
 * 更新状态定时器
 */
@Component
public class RestPatrolStatusTask {



    @Scheduled(cron = "0/5 * * * * ?")
    public void test() {
        System.out.println("每隔五秒进行执行");

    }

}
