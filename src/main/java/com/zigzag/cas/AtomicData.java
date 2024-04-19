package com.zigzag.cas;

import sun.misc.Unsafe;

/**
 * 自定义原子data
 *
 * @author qlk
 */
public class AtomicData {
    private volatile int data;
    static final Unsafe unsafe;
    static final long DATA_OFFSET;

    static {
        unsafe = UnsafeAccessor.getUnsafe();
        try {
            // data 属性在 DataContainer 对象中的偏移量，用于 Unsafe 直接访问该属性
            DATA_OFFSET = unsafe.objectFieldOffset(AtomicData.class.getDeclaredField("data"));
        } catch (NoSuchFieldException e) {
            throw new Error(e);
        }
    }

    public AtomicData(int data) {
        this.data = data;
    }

    public void decrease(int amount) {
        int oldValue;
        while (true) {

            // 获取共享变量旧值，可以在这一行加入断点，修改 data 调试来加深理解
            oldValue = data;

            // cas 尝试修改 data 为 旧值 + amount，如果期间旧值被别的线程改了，返回 false
            if (unsafe.compareAndSwapInt(this, DATA_OFFSET, oldValue, oldValue - amount)) {
                return;
            }
        }
    }

    public int getData() {
        return data;
    }

    public static void main(String[] args) {
        Account.demo(new Account() {
            AtomicData atomicData = new AtomicData(10000);
            @Override
            public Integer getBalance() {
                return atomicData.getData();
            }
            @Override
            public void withdraw(Integer amount) {
                atomicData.decrease(amount);
            }
        });
    }

}
