package com.zigzag.thread.daemon;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;

/**
 * 守护线程
 *
 * @author qlk
 */
@Slf4j(topic = "c.DaemonTest")
public class DaemonTest {

    public static void main(String[] args) throws InterruptedException {

        test1();
    }

    private static void test1() throws InterruptedException {
        Thread t1 = new Thread(() -> {
            while (true) {
                // 线程设置了daemon
                // 其它非守护线程结束,触发interrupt
                if (Thread.currentThread().isInterrupted()) {
                    log.debug("结束");
                    break;
                }
            }
        }, "t1");

        // 启动前设置守护线程
        t1.setDaemon(true);
        t1.start();

        Thread.sleep(1000);
        log.debug("结束");
    }

}
