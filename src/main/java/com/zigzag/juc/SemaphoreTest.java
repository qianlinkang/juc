package com.zigzag.juc;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.Semaphore;

import static com.zigzag.cas.SlowMotion.sleep;

/**
 * @author qlk
 */
@Slf4j(topic = "c.SemaphoreTest")
public class SemaphoreTest {
    public static void main(String[] args) {
        // 创建
        Semaphore semaphore = new Semaphore(3);
        // 10个线程
        for (int i = 0; i < 10; i++) {
            new Thread(() -> {
                // 获取许可
                // 有空闲资源可以使用
                try {
                    semaphore.acquire();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                try {
                    log.debug("run");
                    sleep(1000);
                    log.debug("end");
                } finally {
                    // 释放许可
                    semaphore.release();
                }
            }).start();
        }

    }
}
