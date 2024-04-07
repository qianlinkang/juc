package com.zigzag.thread.api;

import lombok.extern.slf4j.Slf4j;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

/**
 * join方法
 *
 * @author qlk
 */
@Slf4j(topic = "c.JoinTest")
public class JoinTest {

    private static int r1 = 0;
    private static int r2 = 0;

    public static void main(String[] args) throws InterruptedException {
//        test1();
        test2();
    }

    /**
     * 等待多个线程执行结束
     * @throws InterruptedException /
     */
    public static void test1() throws InterruptedException {
        Thread t1 = new Thread(() -> {
            try {
                TimeUnit.SECONDS.sleep(1);
                r1 = 10;
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }, "t1");

        Thread t2 = new Thread(() -> {
            try {
                TimeUnit.SECONDS.sleep(2);
                r2 = 20;
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }, "t2");

        long start = System.currentTimeMillis();
        t1.start();
        t2.start();

//        t1.join();
        // 选择较长执行时间的线程执行join即可
        t2.join();
        long end = System.currentTimeMillis();

        log.debug("r1: {}, r2: {}, cost: {}", r1, r2, end - start);

    }

    /**
     * 带时效的join
     */
    private static void test2() throws InterruptedException {
        Thread t1 = new Thread(() -> {
            try {
                // 线程执行2s,只是等待1.5s,线程提前结束,执行1.5s
//                TimeUnit.SECONDS.sleep(2);
                // 线程执行1s,只是等待1.5s,线程提前结束.执行1s
                TimeUnit.SECONDS.sleep(1);
                r1 = 10;
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }, "t1");

        long start = System.currentTimeMillis();
        t1.start();


        t1.join(1500);
        long end = System.currentTimeMillis();

        log.debug("r1: {}, cost: {}", r1, end - start);

    }

}
