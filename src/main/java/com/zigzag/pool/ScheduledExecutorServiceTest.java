package com.zigzag.pool;

import lombok.extern.slf4j.Slf4j;

import java.util.Date;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * @author qlk
 */
@Slf4j(topic = "c.ScheduledExecutorServiceTest")
public class ScheduledExecutorServiceTest {
    public static void main(String[] args) {
        // ScheduledExecutorService pool = Executors.newScheduledThreadPool(2);

        ScheduledExecutorService pool = Executors.newScheduledThreadPool(1);

        // method1(pool);

        // method2(pool);

        // method3(pool);

        log.debug("start...");
        pool.scheduleWithFixedDelay(()-> {
            log.debug("running...");
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }, 1, 1, TimeUnit.SECONDS);

    }

    private static void method3(ScheduledExecutorService pool) {
        log.debug("start...");
        pool.scheduleAtFixedRate(() -> {
            log.debug("running...");
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }, 1, 1, TimeUnit.SECONDS);
    }

    private static void method2(ScheduledExecutorService pool) {
        // 1s后将每隔1s执行一次
        log.debug("start...");
        pool.scheduleAtFixedRate(() -> {
            log.debug("running...");
        }, 1, 1, TimeUnit.SECONDS);
    }

    private static void method1(ScheduledExecutorService pool) {
        log.debug("start...");
        pool.schedule(() -> {
            log.debug("任务1,执行时间:" + new Date());
            try {
                // 当线程数改为1个时,仍然还是串行执行,
                // task2等待2s后执行
                Thread.sleep(2000);
                // 当任务出现异常时,task2仍然会执行
                int i = 1/0;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }, 1000, TimeUnit.MILLISECONDS);

        pool.schedule(() -> {
            log.debug("任务2,执行时间:" + new Date());
        }, 1000, TimeUnit.MILLISECONDS);
    }
}
