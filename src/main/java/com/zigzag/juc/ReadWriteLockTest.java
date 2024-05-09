package com.zigzag.juc;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * 读写锁
 *
 * @author qlk
 */
public class ReadWriteLockTest {

    public static void main(String[] args) {
        DataContainer dataContainer = new DataContainer();

        // 读,读锁并发 ok
//        new Thread(() -> {
//            dataContainer.read();
//        }).start();
//
//        new Thread(() -> {
//            dataContainer.read();
//        }).start();

        // 读,写锁相互阻塞
        new Thread(() -> {
            dataContainer.read();
        }).start();

        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        new Thread(() -> {
            dataContainer.write();
        }).start();

        // 写,写锁相互阻塞
//        new Thread(() -> {
//            dataContainer.write();
//        }).start();
//
//
//        new Thread(() -> {
//            dataContainer.write();
//        }).start();

    }

    @Slf4j(topic = "c.DataContainer")
    static class DataContainer {
        private Object data;
        private ReentrantReadWriteLock rw = new ReentrantReadWriteLock();

        private ReentrantReadWriteLock.ReadLock r = rw.readLock();
        private ReentrantReadWriteLock.WriteLock w = rw.writeLock();

        public Object read() {
            log.debug("获取读锁");
            r.lock();

            try {
                log.debug("读取");
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                return data;
            } finally {
                log.debug("释放读锁");
                r.unlock();
            }
        }

        public void write() {
            log.debug("获取写锁");
            w.lock();
            try {
                log.debug("写入");
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } finally {
                log.debug("释放写锁");
                w.unlock();
            }
        }
    }
}
