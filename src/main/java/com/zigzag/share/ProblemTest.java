package com.zigzag.share;

import lombok.extern.slf4j.Slf4j;

/**
 * 多线程下,访问共享资源问题:
 * 两个线程对初始值为0的静态变量一个做自增,一个做自减,各做5000次
 *
 * @author qlk
 */
@Slf4j(topic = "c.ProblemTest")
public class ProblemTest {

    private static int count = 0;

    public static void main(String[] args) throws InterruptedException {
        Thread t1 = new Thread(() -> {
            for (int i = 0; i < 15000; i++) {
                count++;
            }
        }, "t1");


        Thread t2 = new Thread(() -> {
            for (int i = 0; i < 15000; i++) {
                count--;
            }
        }, "t2");

        t1.start();
        t2.start();

        t1.join();
        t2.join();

        log.debug("count: {}", count);


    }


}
