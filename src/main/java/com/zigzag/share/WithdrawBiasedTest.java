package com.zigzag.share;

import lombok.extern.slf4j.Slf4j;
import org.openjdk.jol.info.ClassLayout;

/**
 * 偏向锁测试
 *
 * @author qlk
 */
@Slf4j(topic = "c.WithdrawBiasedTest")
public class WithdrawBiasedTest {
    public static void main(String[] args) throws InterruptedException {
        // 创建可偏向对象
        Dog3 dog = new Dog3();


        new Thread(() -> {
            log.debug(ClassLayout.parseInstance(dog).toPrintable());
            synchronized (dog) {
                log.debug(ClassLayout.parseInstance(dog).toPrintable());
            }
            log.debug(ClassLayout.parseInstance(dog).toPrintable());

            synchronized (WithdrawBiasedTest.class) {
                WithdrawBiasedTest.class.notify();
            }

        }, "t1").start();

        new Thread(() -> {

            synchronized (WithdrawBiasedTest.class) {
                try {
                    WithdrawBiasedTest.class.wait();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }

            log.debug(ClassLayout.parseInstance(dog).toPrintable());
            synchronized (dog) {
                log.debug(ClassLayout.parseInstance(dog).toPrintable());
            }
            log.debug(ClassLayout.parseInstance(dog).toPrintable());
        }, "t2").start();
    }
}

class Dog2 {

}
