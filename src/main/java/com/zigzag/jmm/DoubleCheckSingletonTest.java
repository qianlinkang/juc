package com.zigzag.jmm;

/**
 * double check 单例模式
 * @author qlk
 */
public class DoubleCheckSingletonTest {

    public DoubleCheckSingletonTest() {}

    private static DoubleCheckSingletonTest INSTANCE = null;

    public static DoubleCheckSingletonTest getInstance() {
        if (INSTANCE == null) {
            synchronized (DoubleCheckSingletonTest.class) {
                if (INSTANCE == null) {
                    INSTANCE = new DoubleCheckSingletonTest();
                }
            }
        }
        return INSTANCE;
    }
}
