package com.zigzag.share;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.List;

/**
 * 模式:
 * 保护性暂停
 *
 * @author qlk
 */
@Slf4j(topic = "c.GuardSuspension")
public class GuardSuspension {

    private final Object LOCK = new Object();

    private Object response;

    public static void main(String[] args) {
        GuardSuspension guardSuspension = new GuardSuspension();

        new Thread(() -> {
            try {
                List<String> resp = Downloader.download();
                log.debug("download complete...");
                guardSuspension.complete(resp);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }, "子线程").start();

        log.debug("waiting...");
        // 主线程阻塞等待
        Object resp = guardSuspension.get();
        log.debug("get response: [{}] lines", ((List<String>) resp).size());


    }

    public Object get() {
        synchronized (LOCK) {
            while (response == null) {
                try {
                    LOCK.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
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
