package com.zigzag.juc;

import java.util.Date;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author qlk
 */
public class CyclicBarrierTest {
    public static void main(String[] args) {
        // 注意 线程池中的线程数目要和 CyclicBarrier 中的等待数相同
        // 如果不一致,会导致两个task1将cb的计数减为0
        ExecutorService pool = Executors.newFixedThreadPool(2);
        // 等计数2结束后才继续执行
        CyclicBarrier cb = new CyclicBarrier(2, () -> {
            System.out.println("计数完成");
        });

        // cb可以重复使用
        for (int i = 0; i < 3; i++) {
            pool.submit(() -> {
                System.out.println("线程1开始.." + new Date());
                try {
                    cb.await(); // 当个数不足时，等待
                } catch (InterruptedException | BrokenBarrierException e) {
                    e.printStackTrace();
                }
            });


            pool.submit(() -> {
                System.out.println("线程2开始.." + new Date());
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                try {
                    cb.await(); // 2 秒后，线程个数够2，继续运行
                } catch (InterruptedException | BrokenBarrierException e) {
                    e.printStackTrace();
                }
            });
        }

        pool.shutdown();
    }
}
