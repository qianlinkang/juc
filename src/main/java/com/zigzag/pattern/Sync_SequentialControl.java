package com.zigzag.pattern;

import lombok.extern.slf4j.Slf4j;

/**
 * 同步模式
 * 顺序控制:先打印1再打印2
 * wait/notify实现
 *
 * @author qlk
 */
@Slf4j(topic = "c.Sync_SequentialControl")
public class Sync_SequentialControl {

    // 同步锁
    private final static Object LOCK = new Object();

    // 同步变量
    private static boolean isLogged = false;

    public static void main(String[] args) {

        Thread t1 = new Thread(() -> {
            log.debug("1");
            synchronized (LOCK) {
                isLogged = true;
                LOCK.notifyAll();
            }
        }, "线程1");

        Thread t2 = new Thread(() -> {
            synchronized (LOCK) {
                while (!isLogged) {
                    try {
                        // 1还没有打印, 进入等待
                        LOCK.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    log.debug("2");
                }
            }
        }, "线程2");


        t1.start();
        t2.start();
    }
}
