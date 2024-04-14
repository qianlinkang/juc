package com.zigzag.share;

import lombok.extern.slf4j.Slf4j;

/**
 * @author qlk
 */
@Slf4j(topic = "c.WaitNotifyTest2")
public class WaitNotifyTest2 {

    // 锁对象最好用final修饰一下
    final static Object ROOM = new Object();
    static boolean hasCigarette = false;
    static boolean hasTakeout = false;

    public static void main(String[] args) throws InterruptedException {
        new Thread(() -> {
            synchronized (ROOM) {
                log.debug("有烟没? [{}]", hasCigarette);
//                if (!hasCigarette) {
                while (!hasCigarette) {
                    log.debug("没烟,先歇会儿!");
                    try {
//                        Thread.sleep(2000);
                        ROOM.wait(2000);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
                log.debug("有烟没? [{}]", hasCigarette);
                if (hasCigarette) {
                    log.debug("可以开售干活了...");
                } else {
                    log.debug("没干成活...");
                }

            }

        }, "小南").start();


        new Thread(() -> {
            synchronized (ROOM) {
                log.debug("外卖送到没? [{}]", hasTakeout);
//                if (!hasTakeout) {
                while (!hasTakeout) {
                    log.debug("没外卖,先歇会儿!");
                    try {
                        ROOM.wait();
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
                log.debug("外卖送到没? [{}]", hasTakeout);
                if (hasTakeout) {
                    log.debug("可以开售干活了...");
                } else {
                    log.debug("没干成活...");
                }
            }
        }, "小女").start();

        for (int i = 0; i < 5; i++) {
            new Thread(() -> {
                synchronized (ROOM) {
                    log.debug("可以开售干活了...");
                }
            }, "其他人" + i).start();
        }

        Thread.sleep(1000);

        new Thread(() -> {
//            hasCigarette = true;
//            log.debug("烟到了哦");

            synchronized (ROOM) {
                hasCigarette = true;
                log.debug("烟到了哦");
//                ROOM.notify();
                ROOM.notifyAll();
            }
        }, "送烟的").start();
    }

}
