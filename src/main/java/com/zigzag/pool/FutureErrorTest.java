package com.zigzag.pool;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * @author qlk
 */
@Slf4j(topic = "c.FutureErrorTest")
public class FutureErrorTest {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        ExecutorService pool = Executors.newFixedThreadPool(1);

        Future<Boolean> task = pool.submit(() -> {
            log.debug("异常");
            int i = 2 / 0;
            return true;
        });

        // 任务异常,get到的是exception
        System.out.println("===");
        System.out.println("===" + task.get());
    }
}
