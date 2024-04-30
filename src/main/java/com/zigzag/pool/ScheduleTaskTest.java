package com.zigzag.pool;

import lombok.extern.slf4j.Slf4j;

import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * @author qlk
 */
@Slf4j(topic = "c.ScheduleTaskTest")
public class ScheduleTaskTest {
    public static void main(String[] args) {
        // 每周四 18:00:00 定时执行任务

        LocalDateTime now = LocalDateTime.now();
        LocalDateTime time = now.withHour(18).withMinute(0).withSecond(0).with(DayOfWeek.THURSDAY);  // 只能修改到本周
        // 计算时间间隔
        // 如果 当前时间 > 本周周四,需要找到下周周四
        if (now.compareTo(time) > 0) {
            time = time.plusWeeks(1);
        }
        long initialDelay = Duration.between(now, time).toMillis();

        // period: 间隔时间
        long period = 1000 * 60 * 60 * 24 * 7;
        ScheduledExecutorService pool = Executors.newScheduledThreadPool(1);
        pool.scheduleAtFixedRate(() -> {
            System.out.println("running");
        }, initialDelay, period, TimeUnit.MILLISECONDS);
    }
}
