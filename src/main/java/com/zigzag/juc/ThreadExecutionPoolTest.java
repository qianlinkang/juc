package com.zigzag.juc;

import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.*;

/**
 * @author qlk
 */
@Slf4j(topic = "c.ThreadExecutionPoolTest")
public class ThreadExecutionPoolTest {

    public static void main(String[] args) throws InterruptedException, ExecutionException, TimeoutException {
        ExecutorService pool = Executors.newFixedThreadPool(2);

        // method1(pool);

        // method2(pool);

         method3(pool);

    }

    /**
     * invokeAny
     */
    private static void method3(ExecutorService pool) throws InterruptedException, ExecutionException, TimeoutException {
        // 返回2,最先执行完成
        String result = pool.invokeAny(Arrays.asList(
                () -> {
                    log.debug("begin");
                    Thread.sleep(1000);
                    return "1";
                },
                () -> {
                    log.debug("begin");
                    Thread.sleep(500);
                    return "2";
                },
                () -> {
                    log.debug("begin");
                    Thread.sleep(2000);
                    return "3";
                }
        ), 60, TimeUnit.SECONDS);

        System.out.println("result = " + result);
    }

    /**
     * invokeAll
     */
    private static void method2(ExecutorService pool) throws InterruptedException {
        List<Future<String>> futures = pool.invokeAll(Arrays.asList(
                () -> {
                    log.debug("begin");
                    Thread.sleep(1000);
                    return "1";
                },
                () -> {
                    log.debug("begin");
                    Thread.sleep(500);
                    return "2";
                },
                () -> {
                    log.debug("begin");
                    Thread.sleep(2000);
                    return "3";
                }
        ), 60, TimeUnit.SECONDS);

        futures.forEach(f -> {
            try {
                log.debug("{}", f.get());
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        });
    }

    /**
     * execute,submit
     */
    private static void method1(ExecutorService pool) {
        pool.execute(() -> {
            log.debug("execute running");
        });

        Future<String> future = pool.submit(new Callable<String>() {
            @Override
            public String call() throws Exception {
                log.debug("submit running");
                Thread.sleep(1000);
                return "submit ok";
            }
        });

        try {
            log.debug("{}", future.get());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
