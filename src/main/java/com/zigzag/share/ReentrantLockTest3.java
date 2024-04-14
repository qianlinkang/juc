package com.zigzag.share;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

/**
 * ReentrantLock
 * 锁超时
 *
 * @author qlk
 */
@Slf4j(topic = "c.ReentrantLockTest3")
public class ReentrantLockTest3 {

    private static final ReentrantLock LOCK = new ReentrantLock();

    public static void main(String[] args) {

        Thread t1 = new Thread(() -> {
            log.debug("启动");
            try {
                // 尝试获取锁,最多等待1s
                boolean isLock = LOCK.tryLock(1, TimeUnit.SECONDS);
                if (!isLock) {
                    log.debug("获取锁失败,返回");
                    return;
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
                log.debug("获得不到锁了");
                return;
            }
            try {
                log.debug("获得锁了");
            } finally {
                LOCK.unlock();
            }

        }, "t1");

        // 主线程获取到锁
        try {
            LOCK.lock();
            log.debug("主线程-获得锁了");

            t1.start();
        } finally {
            try {
//                Thread.sleep(2000);
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            LOCK.unlock();
        }

    }

}
