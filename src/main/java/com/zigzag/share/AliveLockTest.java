package com.zigzag.share;

import lombok.extern.slf4j.Slf4j;

/**
 * 活锁
 *
 * @author qlk
 */
@Slf4j(topic = "c.AliveLockTest")
public class AliveLockTest {

    static volatile int count = 10;

    public static void main(String[] args) {
        new Thread(() -> {
            while (count > 0) {
                try {
                    Thread.sleep(200);
                    count--;
                    log.debug("count:{}", count);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }, "t1").start();

        new Thread(() -> {
            while (count < 20) {
                try {
                    Thread.sleep(200);
                    count++;
                    log.debug("count:{}", count);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }, "t2").start();
    }
}
