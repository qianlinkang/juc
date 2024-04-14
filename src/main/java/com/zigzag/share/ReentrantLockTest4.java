package com.zigzag.share;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * ReentrantLock
 * 条件变量
 *
 * @author qlk
 */
@Slf4j(topic = "c.ReentrantLockTest4")
public class ReentrantLockTest4 {

    private static final ReentrantLock lock = new ReentrantLock();

    // 等烟休息室
    private static final Condition waitCigaretteQueue = lock.newCondition();
    // 等早餐休息室
    private static final Condition waitBreakfastQueue = lock.newCondition();

    private static boolean hasCigarette = false;
    private static boolean hasBreakfast = false;

    public static void main(String[] args) {

        new Thread(() -> {
            try {
                lock.lock();
                log.debug("有烟没？ 【{}】", hasCigarette);
                while (!hasCigarette) {
                    try {
                        // 等待烟，进入等待队列
                        log.debug("没烟，先歇会儿");
                        waitCigaretteQueue.await();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    log.debug("等到了他的烟");
                }
            } finally {
                lock.unlock();
            }
        }, "小南").start();

        new Thread(() -> {
            try {
                lock.lock();
                log.debug("有早餐没？ 【{}】", hasBreakfast);
                while (!hasBreakfast) {
                    try {
                        log.debug("没早餐，先歇会儿");
                        waitBreakfastQueue.await();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    log.debug("等到了他的早餐");
                }
            } finally {
                lock.unlock();
            }
        }, "小女").start();


        try {
            Thread.sleep(1000);
            sendCigarette();
            Thread.sleep(1000);
            sendBreakfast();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

    }

    private static void sendCigarette() {
        try {
            lock.lock();

            log.debug("烟送来了");
            hasCigarette = true;
            // 唤起 waitCigaretteQueue 等待队列中的线程
            waitCigaretteQueue.signal();
        } finally {
            lock.unlock();
        }
    }

    private static void sendBreakfast() {
        try {
            lock.lock();

            log.debug("早餐送来了");
            hasBreakfast = true;
            // 唤起 waitCigaretteQueue 等待队列中的线程
            waitBreakfastQueue.signal();
        } finally {
            lock.unlock();
        }
    }

}
