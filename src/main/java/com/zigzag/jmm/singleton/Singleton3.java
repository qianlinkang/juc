package com.zigzag.jmm.singleton;

/**
 * @author qlk
 */
public final class Singleton3 {
    private Singleton3() {
    }

    private static Singleton3 INSTANCE = null;

    // 分析这里的线程安全, 并说明有什么缺点
    // 保证了线程安全 synchronized
    // 锁的范围较大,后续调用仍然会加锁,降低了性能
    public static synchronized Singleton3 getInstance() {
        if (INSTANCE != null) {
            return INSTANCE;
        }
        INSTANCE = new Singleton3();
        return INSTANCE;
    }
}
