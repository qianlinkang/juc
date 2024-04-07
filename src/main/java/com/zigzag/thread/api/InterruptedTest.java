package com.zigzag.thread.api;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.LockSupport;


/**
 * 休眠线程打断抛出异常
 * <p>
 * 打断阻塞线程,会清空打断状态
 *
 * @author qlk
 */
@Slf4j(topic = "c.InterruptedTest")
public class InterruptedTest {

    public static void main(String[] args) throws InterruptedException {
//        test1();
//        test2();
        test3();
    }

    // 打断park线程,不清除中断
    private static void test3() throws InterruptedException {
        Thread t1 = new Thread(() -> {
            log.debug("park...");
            LockSupport.park();
            log.debug("unPark...");
//            log.debug("打断状态: {}...", Thread.currentThread().isInterrupted());
            log.debug("打断状态: {}...", Thread.interrupted());

            // 再次打断
            // isInterrupted()不会清除中断标志,再次使用park会失效
            // Thread.interrupted()会清除中断标志,park仍然有效
            LockSupport.park();
            log.debug("unPark...");

        }, "t1");

        t1.start();

        TimeUnit.MILLISECONDS.sleep(500);
        t1.interrupt();

    }

    // 阻塞线程(sleep, wait, join)打断抛出异常
    private static void test1() throws InterruptedException {
        Thread t1 = new Thread("t1") {
            @Override
            public void run() {
                log.debug("enter sleep...");
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    log.debug("线程打断状态: {}", isInterrupted());
                    log.error("wake up...");
                    e.printStackTrace();
                }
            }
        };

        t1.start();

        Thread.sleep(1000);

        log.debug("interrupt...");
        t1.interrupt();
        log.debug("线程打断状态: {}", t1.isInterrupted());
    }

    // 正常线程打断,中断标志为true
    private static void test2() throws InterruptedException {
        Thread t1 = new Thread(() -> {
            while (true) {
                Thread current = Thread.currentThread();
                boolean interrupted = current.isInterrupted();
                if (interrupted) {
                    log.debug("中断标志: {}", interrupted);
                    break;
                }
            }
        }, "t1");

        t1.start();

        TimeUnit.SECONDS.sleep(1);
        t1.interrupt();

    }
}
