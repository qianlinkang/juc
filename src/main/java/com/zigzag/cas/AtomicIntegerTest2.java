package com.zigzag.cas;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * 模拟原子乘法
 *
 * @author qlk
 */
public class AtomicIntegerTest2 {

    public static void main(String[] args) {
        AtomicInteger i = new AtomicInteger(2);

        // 其中函数中的操作能保证原子，但函数需要无副作用
        System.out.println(i.updateAndGet(p -> p * 5));

        // 模拟原子乘法
        while (true) {
            int prev = i.get();
            int next = prev * 5;
            if (i.compareAndSet(prev, next)) {
                // prev与最新值一样,跳出循环
                break;
            }
        }
        System.out.println(i.get());

    }
}
