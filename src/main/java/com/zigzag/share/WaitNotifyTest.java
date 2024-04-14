package com.zigzag.share;

import lombok.extern.slf4j.Slf4j;

/**
 * @author qlk
 */
@Slf4j(topic = "c.WaitNotifyTest")
public class WaitNotifyTest {

    // 锁对象最好用final修饰一下
    final static Object OBJ = new Object();

    public static void main(String[] args) throws InterruptedException {
        new Thread(() -> {
            synchronized (OBJ) {
                log.debug("执行...");
                try {
                    OBJ.wait();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                log.debug("其它代码继续继续...");

            }
        }, "t1").start();

        new Thread(() -> {
            synchronized (OBJ) {
                log.debug("执行...");
                try {
                    OBJ.wait();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                log.debug("其它代码继续继续...");
            }
        }, "t2").start();


        // 主线程
        log.debug("主线程执行中...");
        Thread.sleep(2000);
        log.debug("唤醒obj上的其它线程");
        synchronized (OBJ) {
//            obj.notify();
            OBJ.notifyAll();
        }
    }

}
