package com.zigzag.pattern;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.locks.LockSupport;

/**
 * 同步模式
 * 顺序控制:先打印1再打印2
 * park/unpark实现
 *
 * @author qlk
 */
@Slf4j(topic = "c.Sync_SequentialControl2")
public class Sync_SequentialControl2 {

    public static void main(String[] args) {

        Thread t2 = new Thread(() -> {
            // 等待"许可"
            // 没有时,当前线程暂停运行
            // 有时,用掉这个许可,当前线程恢复运行
            LockSupport.park();
            log.debug("2");
        }, "线程2");

        Thread t1 = new Thread(() -> {
            log.debug("1");
            // 给线程2发放"许可"
            // 注意.对此调用unpark,也只会发放一个"许可"
            LockSupport.unpark(t2);

        }, "线程1");

        t1.start();
        t2.start();
    }
}
