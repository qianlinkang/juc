package com.zigzag.jmm;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;

/**
 * jmm可见性
 *
 * @author qlk
 */
@Slf4j(topic = "c.VisibilityTest")
public class VisibilityTest {

    private volatile static boolean IS_TRUE = true;

    public static void main(String[] args) throws InterruptedException {

        Thread t1 = new Thread(() -> {
            while (IS_TRUE) {
                //
                System.out.println();
            }
        });

        t1.start();

        TimeUnit.SECONDS.sleep(1);
        log.debug("停止");
        IS_TRUE = false;

    }
}
