package com.zigzag.share;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.locks.ReentrantLock;

/**
 * ReentrantLock
 * 可重入
 *
 * @author qlk
 */
@Slf4j(topic = "c.ReentrantLockTest1")
public class ReentrantLockTest1 {

    static ReentrantLock lock = new ReentrantLock();

    public static void main(String[] args) {
        m1();
    }

    public static void m1() {
        try {
            lock.lock();
            log.debug("exec m1");
            m2();
        } finally {
            lock.unlock();
        }
    }

    public static void m2() {
        try {
            // 重入
            lock.lock();
            log.debug("exec m2");
            m3();
        } finally {
            lock.unlock();
        }
    }

    public static void m3() {
        try {
            // 重入
            lock.lock();
            log.debug("exec m3");
        } finally {
            lock.unlock();
        }
    }
}
