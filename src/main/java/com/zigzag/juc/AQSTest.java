package com.zigzag.juc;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.AbstractQueuedSynchronizer;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

/**
 * @author qlk
 */
@Slf4j(topic = "c.AQSTest")
public class AQSTest {
    public static void main(String[] args) {
        MyLock lock = new MyLock();

        new Thread(() -> {
            try {
                lock.lock();
                log.debug("locking");

                // 重复获取锁会被挡住,不可重入
//                lock.lock();
//                log.debug("locking");
            } finally {
                log.debug("unlocking");
                lock.unlock();
            };
        }, "t1").start();

        new Thread(() -> {
            lock.lock();
            try {
                log.debug("locking");
            } finally {
                log.debug("unlocking");
                lock.unlock();
            };
        }, "t2").start();
    }
}

/**
 * 自定义锁,不可重入锁
 */
class MyLock implements Lock {

    static MySync sync = new MySync();

    /**
     * 尝试获取锁,不成功,进入等待队列
     */
    @Override
    public void lock() {
        sync.acquire(1);
    }

    /**
     * 尝试获取锁,不成功,进入等待队列,可打断
     */
    @Override
    public void lockInterruptibly() throws InterruptedException {
        sync.acquireInterruptibly(1);
    }

    /**
     * 尝试一次,不成功返回,不加入队列
     */
    @Override
    public boolean tryLock() {
        return sync.tryAcquire(1);
    }

    /**
     * 尝试一次,不成功返回,不进入队列
     */
    @Override
    public boolean tryLock(long time, TimeUnit unit) throws InterruptedException {
        return sync.tryAcquireNanos(1, unit.toNanos(time));
    }

    /**
     * 释放锁
     */
    @Override
    public void unlock() {
        sync.release(1);
    }

    /**
     * 生成条件变量
     */
    @Override
    public Condition newCondition() {
        return sync.newCondition();
    }
}

/**
 * 自定义state同步器,实现aqs的复用
 */
class MySync extends AbstractQueuedSynchronizer {

    @Override
    protected boolean tryAcquire(int acquires) {
        if (acquires == 1) {
            // cas方式加锁
            if (compareAndSetState(0, 1)) {
                // 并设置owner为当前线程
                setExclusiveOwnerThread(Thread.currentThread());
                return true;
            }
        }
        return false;
    }

    @Override
    protected boolean tryRelease(int acquires) {
        if (acquires == 1) {
            if (getState() == 0) {
                throw new IllegalMonitorStateException();
            }
            // 解锁,设置owner为null
            setExclusiveOwnerThread(null);
            // 此处不需要cas,因为加锁的线程只有一个
            // 且state是由volatile修饰
            setState(0);
            return true;
        }
        return false;
    }

    /**
     * 是否持有独占锁
     */
    @Override
    protected boolean isHeldExclusively() {
        return getState() == 1;
    }

    public Condition newCondition() {
        return new ConditionObject();
    }
}