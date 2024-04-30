package com.zigzag.pool;

import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

/**
 * @author qlk
 */
@Slf4j(topic = "c.ShutdownTest")
public class ShutdownTest {
    public static void main(String[] args) throws InterruptedException {
        ExecutorService pool = Executors.newFixedThreadPool(2);

        Future<Integer> result1 = pool.submit(() -> {
            log.debug("task 1 running");
            Thread.sleep(1000);
            log.debug("task 1 finish");
            return 1;
        });

        Future<Integer> result2 = pool.submit(() -> {
            log.debug("task 2 running");
            Thread.sleep(1000);
            log.debug("task 2 finish");
            return 2;
        });

        Future<Integer> result3 = pool.submit(() -> {
            log.debug("task 3 running");
            Thread.sleep(1000);
            log.debug("task 3 finish");
            return 3;
        });

        log.debug("shutdown");

        // 但已提交任务会执行完
//        pool.shutdown();
        // 用 interrupt 的方式中断正在执行的任务,并返回
        // task1和task2是未完成就被打断了
        // task3被封装成返回值了
        List<Runnable> notFinished = pool.shutdownNow();
        log.debug("未完成的任务: {}", notFinished);

        // 不会接收新任务
        try {
            pool.submit(() -> {
                log.debug("task 4 running");
                Thread.sleep(1000);
                log.debug("task 4 finish");
                return 4;
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

        // 等待所有任务结束的超时时间
        pool.awaitTermination(5, TimeUnit.SECONDS);


        // 不会阻塞调用线程的执行
        log.debug("main finished");
    }
}
