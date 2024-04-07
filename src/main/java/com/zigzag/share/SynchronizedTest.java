package com.zigzag.share;

import lombok.extern.slf4j.Slf4j;

/**
 *
 * synchronized解决临界区的竞态条件
 *
 * @author qlk
 */
@Slf4j(topic = "c.SynchronizedTest")
public class SynchronizedTest {

    private static int count = 0;

    private static final Object ROOM_LOCK = new Object();

    public static void main(String[] args) throws InterruptedException {
        Thread t1 = new Thread(() -> {
            for (int i = 0; i < 15000; i++) {
                synchronized (ROOM_LOCK) {
                    count++;
                }
            }
        }, "t1");


        Thread t2 = new Thread(() -> {
            for (int i = 0; i < 15000; i++) {
                synchronized (ROOM_LOCK) {
                    count--;
                }
            }
        }, "t2");

        t1.start();
        t2.start();

        t1.join();
        t2.join();

        log.debug("count: {}", count);


    }

}
