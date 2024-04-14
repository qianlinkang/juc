package com.zigzag.share;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * 模式:
 * 保护性暂停
 * <p>
 * 超时版本
 *
 * @author qlk
 */
@Slf4j(topic = "c.GuardSuspensionV2")
public class GuardSuspensionV2 {

    private final Object LOCK = new Object();

    private Object response;

    public static void main(String[] args) {
        GuardSuspensionV2 guardSuspension = new GuardSuspensionV2();

        new Thread(() -> {
            try {
                TimeUnit.SECONDS.sleep(1);
                guardSuspension.complete(null);
                TimeUnit.SECONDS.sleep(1);
                guardSuspension.complete(Arrays.asList("a", "b", "c"));
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }, "子线程").start();

        log.debug("waiting...");
        // 主线程阻塞等待
//        Object resp = guardSuspension.get(1500);
        Object resp = guardSuspension.get(2500);
        if (resp != null) {
            log.debug("get response: [{}] lines", ((List<String>) resp).size());
        } else {
            log.debug("can't get  response");
        }


    }

    public Object get(long mills) {
        synchronized (LOCK) {
            // 记录时间
            long begin = System.currentTimeMillis();
            long timePasswd = 0;
            while (response == null) {
                // 多次进入循环
                // 记录剩余还需要等待的时间
                long waitTime = mills - timePasswd;
                log.debug("waitTime: {}", waitTime);
                // 超过等待时间,退出循环,不再等待
                if (waitTime <= 0) {
                    log.debug("break...");
                    break;
                }
                try {
                    // 动态更新等待时间
                    LOCK.wait(waitTime);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                // 如果被提前唤醒,这是已经经历的时间假设为 400
                timePasswd = System.currentTimeMillis() - begin;
                log.debug("timePassed: {}, object is null: {}", timePasswd, response == null);
            }
            return response;
        }
    }

    public void complete(Object response) {
        synchronized (LOCK) {
            this.response = response;
            LOCK.notifyAll();
        }
    }
}
