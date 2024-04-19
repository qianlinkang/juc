package com.zigzag.cas;

import java.util.concurrent.atomic.AtomicIntegerFieldUpdater;

/**
 * 字段更新器
 *
 * @author qlk
 */
public class AtomicIntegerFieldUpdaterTest {

    private volatile int field;

    public static void main(String[] args) {

        AtomicIntegerFieldUpdater<AtomicIntegerFieldUpdaterTest> updater = AtomicIntegerFieldUpdater
                .newUpdater(AtomicIntegerFieldUpdaterTest.class, "field");

        AtomicIntegerFieldUpdaterTest fieldUpdate = new AtomicIntegerFieldUpdaterTest();

        // 修改成功
        updater.compareAndSet(fieldUpdate, 0, 10);
        System.out.println(fieldUpdate.field);

        updater.compareAndSet(fieldUpdate, 10, 20);
        System.out.println(fieldUpdate.field);

        // 修改失败
        updater.compareAndSet(fieldUpdate, 10, 30);
        System.out.println(fieldUpdate.field);
    }


}