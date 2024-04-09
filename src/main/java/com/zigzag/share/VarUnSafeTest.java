package com.zigzag.share;

import java.util.ArrayList;
import java.util.List;

/**
 * 引用类型的局部变量非出现线程安全问题
 *
 * @author qlk
 */
public class VarUnSafeTest {

    // 启动线程数
    static final int THREAD_NUMBER = 2;

    // 循环次数
    static final int LOOP_NUMBER = 200;

    public static void main(String[] args) {
//        ThreadUnsafe test = new ThreadUnsafe();
        ThreadSafe test = new ThreadSafe();
        for (int i = 0; i < THREAD_NUMBER; i++) {
            new Thread(() -> {
               test.method1(LOOP_NUMBER);
            }, "thread" + i).start();
        }

    }
}

/**
 * 出现的异常分析(ArrayIndexOutOfBoundsException):
 *  线程2还没add
 *  线程1就执行了remove
 */
class ThreadUnsafe {

    // 成员变量,在堆中
    ArrayList<String> list = new ArrayList<>();

    public void method1(int loopNumber) {
        for (int i = 0; i < loopNumber; i++) {
            // 临界区
            method2();
            method3();
        }

    }

    public void method2() {
        list.add("1");
    }

    public void method3() {
        list.remove(0);
    }

}

/**
 *  两个线程调用method1,会在堆中创建两个局部变量,不存在共享
 */
class ThreadSafe {

    public final void method1(int loopNumber) {
        // 局部变量,在堆中
        ArrayList<String> list = new ArrayList<>();

        for (int i = 0; i < loopNumber; i++) {
            // 临界区
            method2(list);
            method3(list);
        }

    }

    private void method2(List<String> list) {
        list.add("1");
    }

    private void method3(List<String> list) {
        list.remove(0);
    }

}
