package com.zigzag.jmm.singleton;

/**
 * @author qlk
 */
public final class Singleton4 {
    private Singleton4() {
    }

    // 问题1：解释为什么要加 volatile ?
    // 保证可见性和有序性
    // 在 INSTANCE 被赋值后,需要让其他线程能读到,并在第一次check是拦截
    // 防止指令重排,先赋值,还没调用构造方法(正常逻辑是先构造方法,再赋值)
    private static volatile Singleton4 INSTANCE = null;

    // 问题2：对比实现3, 说出这样做的意义?
    // 缩小了锁的范围,后续调用不需要加锁
    public static Singleton4 getInstance() {
        if (INSTANCE != null) {
            return INSTANCE;
        }
        synchronized (Singleton4.class) {  // t1,t2都在这等着
            // 问题3：为什么还要在这里加为空判断, 之前不是判断过了吗?
            // 保证首次创建时,多个线程的并发问题,第一次创建等锁的线程进来后要再判断一次
            if (INSTANCE != null) {  // t2
                return INSTANCE;
            }
            // 正常逻辑是先构造方法,再赋值, 可能重排序(如果没有volatile)
            INSTANCE = new Singleton4();
            return INSTANCE;
        }
    }
}
