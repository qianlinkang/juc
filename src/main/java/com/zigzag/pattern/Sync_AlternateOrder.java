package com.zigzag.pattern;

import lombok.extern.slf4j.Slf4j;

/**
 * 同步模式
 * 交替顺序: 三个线程 abc输出五轮
 * 使用 wait/notify实现
 *
 * @author qlk
 */
@Slf4j(topic = "c.Sync_AlternateOrder")
public class Sync_AlternateOrder {

    public static void main(String[] args) {

        // 参数说明
        // 从1开始,打印5次
        WaitNotify waitNotify = new WaitNotify(1, 5);

        Thread t1 = new Thread(() -> {
            waitNotify.print(1, 2, "a");
        }, "线程1");

        Thread t2 = new Thread(() -> {
            waitNotify.print(2, 3, "b");
        }, "线程2");

        Thread t3 = new Thread(() -> {
            waitNotify.print(3, 1, "c");
        }, "线程3");

        t1.start();
        t2.start();
        t3.start();
    }
}

@Slf4j(topic = "c.WaitNotify")
class WaitNotify {
    // 等待标记
    private int flag;

    // 打印次数
    private int loopNumber;

    public WaitNotify(int flag, int loopNumber) {
        this.flag = flag;
        this.loopNumber = loopNumber;
    }

    public void print(int waitFlag, int nextFlag, String printStr) {
        for (int i = 0; i < loopNumber; i++) {
            // 先加锁
            synchronized (this) {
                while (flag != waitFlag) {
                    try {
                        this.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                log.debug(printStr);
                flag = nextFlag;
                this.notifyAll();
            }
        }
    }
}