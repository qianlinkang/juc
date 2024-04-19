package com.zigzag.cas;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.atomic.AtomicInteger;

import static java.lang.Thread.sleep;

/**
 * CAS 实现锁的原理
 *
 * @author qlk
 */
@Slf4j(topic = "c.CasLock")
public class CasLock {
    // state变量使用原子整数来充当锁状态
    private AtomicInteger state = new AtomicInteger(0);

    public void lock() {
        while (true) {
            // 0 -> 1 表示加锁成功
            if (state.compareAndSet(0, 1)) {
                break;
            }
        }
    }

    // 1 -> 0 表示解锁
    public void unlock() {
        log.debug("unlock...");
        state.set(0);
    }

    public static void main(String[] args) {
        CasLock lock = new CasLock();
        new Thread(() -> {
            log.debug("begin...");
            lock.lock();  // 如果t2上锁成功,线程t1在此处while循环,不进入blocked,会消耗cpu资源
            try {
                log.debug("lock...");
                sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                lock.unlock();
            }
        }, "t1").start();

        new Thread(() -> {
            log.debug("begin...");
            lock.lock();
            try {
                log.debug("lock...");
            } finally {
                lock.unlock();
            }
        }, "t2").start();
    }
}
