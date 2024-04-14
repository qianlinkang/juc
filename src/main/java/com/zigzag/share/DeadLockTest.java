package com.zigzag.share;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;

/**
 * 死锁问题
 *
 * @author qlk
 */
@Slf4j(topic = "c.DeadLockTest")
public class DeadLockTest {

    public static void main(String[] args) {

        Object A = new Object();
        Object B = new Object();


        Thread t1 = new Thread(() -> {
            synchronized (A) {
                try {
                    log.debug("lock a");
                    TimeUnit.SECONDS.sleep(1);
                    synchronized (B) {
                        log.debug("lock b");
                        log.debug("操作...");
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }, "t1");

        Thread t2 = new Thread(() -> {
            synchronized (B) {
                try {
                    log.debug("lock b");
                    TimeUnit.SECONDS.sleep(1);
                    synchronized (A) {
                        log.debug("lock a");
                        log.debug("操作...");
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }, "t2");

        t1.start();
        t2.start();

    }


}
