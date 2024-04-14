package com.zigzag.pattern;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 同步模式
 * 交替顺序: 三个线程 abc输出五轮
 * 使用 ReentrantLock实现,await,signal
 *
 * @author qlk
 */
@Slf4j(topic = "c.Sync_AlternateOrder2")
public class Sync_AlternateOrder2 {

    public static void main(String[] args) {
        AwaitSignal awaitSignal = new AwaitSignal(5);
        Condition aWaitSet = awaitSignal.newCondition();
        Condition bWaitSet = awaitSignal.newCondition();
        Condition cWaitSet = awaitSignal.newCondition();


        Thread t1 = new Thread(() -> {
            awaitSignal.print(aWaitSet, bWaitSet, "a");
        }, "线程1");

        Thread t2 = new Thread(() -> {
            awaitSignal.print(bWaitSet, cWaitSet, "b");
        }, "线程2");

        Thread t3 = new Thread(() -> {
            awaitSignal.print(cWaitSet, aWaitSet, "c");
        }, "线程3");

        t1.start();
        t2.start();
        t3.start();

        // 让线程充分准备,防止虚假唤醒
        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // 先signal a
        awaitSignal.start(aWaitSet);
    }
}

@Slf4j(topic = "c.AwaitSignal")
class AwaitSignal extends ReentrantLock {

    private final int loopNumber;

    public AwaitSignal(int loopNumber) {
        // 打印次数
        this.loopNumber = loopNumber;
    }

    public void print(Condition current, Condition next, String printStr) {
        for (int i = 0; i < loopNumber; i++) {
            this.lock();
            try {
                current.await();
                log.debug(printStr);
                next.signal();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                this.unlock();
            }
        }
    }

    public void start(Condition first) {
        this.lock();
        try {
            log.debug("start");
            first.signal();
        } finally {
            this.unlock();
        }

    }
}