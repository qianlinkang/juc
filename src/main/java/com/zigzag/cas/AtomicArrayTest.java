package com.zigzag.cas;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicIntegerArray;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * 原子数组
 *
 * @author qlk
 */
public class AtomicArrayTest {

    /**
     * 通用测试方法
     *
     * @param arraySupplier 提供一个数组 ()->结果
     * @param lengthFun     计算数组长度的方法
     * @param putConsumer   自增方法
     * @param printConsumer 打印数组方法
     * @param <T>           数组类型
     */
    private static <T> void demo(
            Supplier<T> arraySupplier,
            Function<T, Integer> lengthFun,
            BiConsumer<T, Integer> putConsumer,
            Consumer<T> printConsumer
    ) {
        List<Thread> ts = new ArrayList<>();
        T array = arraySupplier.get();
        int length = lengthFun.apply(array);
        // 启用和数组长度一样的线程数
        for (int i = 0; i < length; i++) {
            ts.add(new Thread(() -> {
                for (int j = 0; j < 10000; j++) {
                    // 对数组的每个元素执行10000次操作
                    putConsumer.accept(array, j % length);
                }
            }));
        }
        // 启动所有线程
        ts.forEach(Thread::start);
        // 等待所有线程结束
        ts.forEach(t -> {
            try {
                t.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        printConsumer.accept(array);


    }

    public static void main(String[] args) {
        // 10000次累加操作均摊到长度为0的数组上
        // 线程不安全的一般数组
        demo(
                ()->new int[10],
                arr -> arr.length,
                (arr, idx) -> arr[idx]++,
                arr -> System.out.println(Arrays.toString(arr))
        );

        // 线程安全的原子数组
        demo(
                ()->new AtomicIntegerArray(10),
                arr -> arr.length(),
                (arr, idx) -> arr.getAndIncrement(idx),
                arr -> System.out.println(arr)
        );
    }
}
