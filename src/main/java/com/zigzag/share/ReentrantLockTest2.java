package com.zigzag.share;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.locks.ReentrantLock;

/**
 * ReentrantLock
 * 可打断
 *
 * @author qlk
 */
@Slf4j(topic = "c.ReentrantLockTest2")
public class ReentrantLockTest2 {

    private static ReentrantLock lock = new ReentrantLock();

    public static void main(String[] args) {
        Thread t1 = new Thread(() -> {
            try {
                // 在没有竞争情况下,此方法会获得lock对象锁
                // 如果有竞争就会进入阻塞队列等待,此时就可以被其它线程使用interrupte方法打断
                log.debug("尝试获取锁");
                lock.lockInterruptibly();
            } catch (InterruptedException e) {
                e.printStackTrace();
                log.debug("没有获得锁,返回");
                return;
            }

            try {
                log.debug("获得到锁");
            } finally {
                lock.unlock();
            }

        }, "t1");

        // 主线程lock
        lock.lock();
        log.debug("主线程获得了锁");

        t1.start();

        try {
            Thread.sleep(1000);
            // 打断t1
            t1.interrupt();
            log.debug("执行打断");
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }
}
