package com.zigzag.pattern;

import lombok.extern.slf4j.Slf4j;

/**
 * 犹豫模式
 * 在一个线程发现另一个线程或本线程已经在做某一件相同的事,那么本线程就无需再做了,直接结束返回
 * @author qlk
 */
@Slf4j(topic = "c.Balking")
public class Balking {

    private volatile boolean starting;

    public void start() {
        log.info("尝试启动监控线程");
        synchronized (this) {
            if (starting) {
                return;
            }
            starting = true;
        }

        // 后续真正启动线程的代码在下面
    }
}
