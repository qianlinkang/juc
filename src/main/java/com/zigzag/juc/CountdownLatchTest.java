package com.zigzag.juc;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.CountDownLatch;

import static com.zigzag.cas.SlowMotion.sleep;

/**
 * 需要等待三个线程
 * 每个线程执行完成后,计数-1
 * @author qlk
 */
@Slf4j(topic = "c.CountdownLatchTest")
public class CountdownLatchTest {
    public static void main(String[] args) throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(3);
        new Thread(() -> {
            log.debug("begin...");
            sleep(1000);
            latch.countDown();
            log.debug("end...{}", latch.getCount());
        }).start();

        new Thread(() -> {
            log.debug("begin...");
            sleep(2000);
            latch.countDown();
            log.debug("end...{}", latch.getCount());
        }).start();

        new Thread(() -> {
            log.debug("begin...");
            sleep(1500);
            latch.countDown();
            log.debug("end...{}", latch.getCount());
        }).start();

        log.debug("waiting...");
        latch.await();
        log.debug("wait end...");
    }
}
