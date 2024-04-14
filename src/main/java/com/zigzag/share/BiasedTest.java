package com.zigzag.share;

import lombok.extern.slf4j.Slf4j;
import org.openjdk.jol.info.ClassLayout;

/**
 * 偏向锁测试
 *
 * @author qlk
 */
@Slf4j(topic = "c.BiasedTest")
public class BiasedTest {
    public static void main(String[] args) throws InterruptedException {
        // 创建可偏向对象
        Dog3 dog = new Dog3();
        dog.hashCode(); // 会禁用这个对象的偏向锁
        String printable = ClassLayout.parseInstance(dog).toPrintable();
        log.debug(printable);

        // 偏向锁延迟特性
        Thread.sleep(4000);
        log.debug(ClassLayout.parseInstance(new Dog3()).toPrintable());

        // 加偏向锁
        synchronized (dog) {
            // 00101010101110001001110000010110000000000101
            // 最后还是101
            // 前面是os给线程赋的id
            log.debug(ClassLayout.parseInstance(dog).toPrintable());
        }
        // 后续mark word都是添加偏向锁
        log.debug(ClassLayout.parseInstance(dog).toPrintable());
    }
}

class Dog {

}
