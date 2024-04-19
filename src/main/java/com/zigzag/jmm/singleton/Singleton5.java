package com.zigzag.jmm.singleton;

/**
 * @author qlk
 */
public final class Singleton5 {
    private Singleton5() {
    }

    // 问题1：属于懒汉式还是饿汉式? --> 懒汉式
    // 类加载本身就是懒惰的
    private static class LazyHolder {
        static final Singleton5 INSTANCE = new Singleton5();
    }

    // 问题2：在创建时是否有并发问题? --> 不会,jvm会保证其线程安全性
    public static Singleton5 getInstance() {
        return LazyHolder.INSTANCE;
    }
}
