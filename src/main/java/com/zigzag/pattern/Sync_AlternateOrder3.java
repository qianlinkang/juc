package com.zigzag.pattern;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.LockSupport;

/**
 * 同步模式
 * 交替顺序: 三个线程 abc输出五轮
 * 使用 park,unpark实现
 *
 * @author qlk
 */
@Slf4j(topic = "c.Sync_AlternateOrder3")
public class Sync_AlternateOrder3 {

    public static void main(String[] args) {

        ParkUnPark parkUnPark = new ParkUnPark(5);

        Thread t1 = new Thread(() -> {
            parkUnPark.print("a");
        }, "线程1");

        Thread t2 = new Thread(() -> {
            parkUnPark.print("b");
        }, "线程2");

        Thread t3 = new Thread(() -> {
            parkUnPark.print("c");
        }, "线程3");

        parkUnPark.setThreads(t1, t2, t3);
        parkUnPark.start();
    }
}

@Slf4j(topic = "c.ParkUnPark")
class ParkUnPark {

    private int lookNumber;

    private Thread[] threads;

    public ParkUnPark(int lookNumber) {
        this.lookNumber = lookNumber;
    }

    public void setThreads(Thread... threads) {
        this.threads = threads;
    }

    public void print(String printStr) {
        for (int i = 0; i < lookNumber; i++) {
            LockSupport.park();
            log.debug(printStr);
            LockSupport.unpark(nextThread());
        }

    }

    /**
     * @return 返回 threads 中的下一个线程
     */
    public Thread nextThread() {
        // 匹配当前线程的下标
        int index = 0;
        Thread current = Thread.currentThread();
        for (int i = 0; i < threads.length; i++) {
            if (threads[i] == current) {
                index = i;
                break;
            }
        }

        // 0 1: +1
        // 2  :  0
        if (index < threads.length - 1) {
            return threads[index + 1];
        } else {
            return threads[0];
        }
    }

    public void start() {
        for (Thread thread : threads) {
            thread.start();
        }
        LockSupport.unpark(threads[0]);
    }
}