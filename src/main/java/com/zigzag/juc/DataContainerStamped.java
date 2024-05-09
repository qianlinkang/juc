package com.zigzag.juc;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.locks.StampedLock;

import static com.zigzag.cas.SlowMotion.sleep;

/**
 * @author qlk
 */
@Slf4j(topic = "c.DataContainerStamped")
public class DataContainerStamped {
    public static void main(String[] args) {
        final DataContainerStamped c = new DataContainerStamped(2);

        // 读读
        // 由于没有写锁,压根就没有加读锁
//        new Thread(() -> {
//            c.read(1);
//        }, "t1").start();
//
//        sleep(500);
//
//        new Thread(() -> {
//            c.read(0);
//        }, "t2").start();

        // 读写
        new Thread(() -> {
            c.read(1);
        }, "t1").start();

        sleep(500);

        new Thread(() -> {
            c.write(100);
        }, "t2").start();
    }

    private int data;

    private final StampedLock lock = new StampedLock();

    public DataContainerStamped(int data) {
        this.data = data;
    }

    public int read(int readTime) {
        log.debug("tryOptimisticRead");
        long stamp = lock.tryOptimisticRead();
        sleep(1000);
        if (lock.validate(stamp)) {
            // 验证通过
            // 表示这段时间内,没有其他写操作data,直接返回data即可
            log.debug("read finish stamp:{}|data:{}", stamp, data);
            return data;
        }
        // 验证没通过
        // 锁升级
        try {
            log.debug("update to read lock, stamp|{}", stamp);
            stamp = lock.readLock();
            log.debug("read lock success, stamp|{}", stamp);
            sleep(readTime);
            return data;
        } finally {
            lock.unlockRead(stamp);
        }
    }

    public void write(int newData) {
        long stamp = lock.writeLock();
        log.debug("Write lock {}", stamp);
        try {
            sleep(2000);
            this.data = data;
        } finally {
            log.debug("Write unlock {}", stamp);
            lock.unlockWrite(stamp);
        }
    }
}
