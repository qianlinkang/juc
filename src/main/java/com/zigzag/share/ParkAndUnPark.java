package com.zigzag.share;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.LockSupport;

/**
 * park和Unpark的基本使用
 *
 * @author qlk
 */
@Slf4j(topic = "c.ParkAndUnPark")
public class ParkAndUnPark {
    public static void main(String[] args) throws InterruptedException {
        Thread t1 = new Thread(() -> {
            try {
                log.debug("start");
                TimeUnit.SECONDS.sleep(1);
//                TimeUnit.SECONDS.sleep(2);
                log.debug("park...");
                log.debug("resume...");
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

        }, "t1");
        t1.start();

//        TimeUnit.SECONDS.sleep(1);
        TimeUnit.SECONDS.sleep(2);
        log.debug("unpark...");
        LockSupport.unpark(t1);
    }
}
