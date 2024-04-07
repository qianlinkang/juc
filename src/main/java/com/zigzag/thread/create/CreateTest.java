package com.zigzag.thread.create;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

/**
 * 线程创建
 *
 * @author qlk
 */
@Slf4j(topic = "c.CreateTest")
public class CreateTest {

    public static void main(String[] args) {
//        m1();
//        m2();
//        m2jdk8();
        m3();

        log.debug("running");
    }


    /**
     * 方法1:new thread
     */
    public static void m1() {
        Thread t1 = new Thread("t1") {
            @Override
            public void run() {
                log.debug("t1 running");
            }
        };
        t1.start();
    }

    /**
     * 方法2:runnable和thread
     */
    public static void m2() {
        Runnable r2 = new Runnable() {
            @Override
            public void run() {
                log.debug("t2 running");
            }
        };
        Thread t2 = new Thread(r2, "t2");
        t2.start();
    }

    /**
     * 方法2:runnable和thread,lambda实现
     */
    public static void m2jdk8() {
        Runnable task = () -> log.debug("t2 running");
        Thread t2 = new Thread(task, "t2");
        t2.start();
    }

    /**
     * 方法3: FutureTask配合Thread
     */
    public static void m3() {
        FutureTask<Integer> task = new FutureTask<>(() -> {
            log.debug("t3 running");
            return 100;
        });

        Thread t3 = new Thread(task, "t3");
        t3.start();

        // 主线程注册,等待task执行完毕后返回结果
        Integer result = null;
        try {
            result = task.get();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        log.debug("t3 running result" + String.valueOf(result));

    }
}
