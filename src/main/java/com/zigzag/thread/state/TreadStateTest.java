package com.zigzag.thread.state;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;

/**
 * java层面的六状态模型
 *
 * @author qlk
 */
@Slf4j(topic = "c.TreadStateTest")
public class TreadStateTest {

    public static void main(String[] args) {

        // NEW 没有调用start方法
        Thread t1 = new Thread(() -> {
            log.debug("running");
        }, "t1");

        // RUNNABLE 运行状态,包括了os的可运行,运行和阻塞状态
        Thread t2 = new Thread(() -> {
            while (true) {

            }
        }, "t2");
        t2.start();

        // TERMINATED 运行结束
        Thread t3 = new Thread(() -> {

        }, "t3");
        t3.start();

        // TIMED_WAITING 阻塞状态的一种,有时间的阻塞
        Thread t4 = new Thread(() -> {
            synchronized (TreadStateTest.class) {
                try {
                    TimeUnit.SECONDS.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }, "t4");
        t4.start();

        // WAITING 阻塞,等待线程2结束(线程2当前在运行)
        Thread t5 = new Thread(() -> {
            try {
                t2.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }, "t5");
        t5.start();

        // BLOCKED 阻塞,等待获取锁
        Thread t6 = new Thread(() -> {
            synchronized (TreadStateTest.class) {
                try {
                    Thread.sleep(1000000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

        }, "t6");
        t6.start();

        log.debug("t1 state {}", t1.getState());
        log.debug("t2 state {}", t2.getState());
        log.debug("t3 state {}", t3.getState());
        log.debug("t4 state {}", t4.getState());
        log.debug("t5 state {}", t5.getState());
        log.debug("t6 state {}", t6.getState());
    }
}
