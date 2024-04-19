package com.zigzag.pattern;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;

/**
 * 两阶段终止
 * 使用volatile
 *
 * @author qlk
 */
@Slf4j(topic = "c.TwoPhaseTermination2Volatile")
public class TwoPhaseTermination2Volatile {

    public static void main(String[] args) throws InterruptedException {

        TPTVolatile t = new TPTVolatile();
        t.start();

        Thread.sleep(3500);
        log.debug("退出");
        t.stop();
    }
}

@Slf4j(topic = "c.TPTVolatile")
class TPTVolatile {
    private Thread thread;
    private volatile boolean stop = false;

    // 加入犹豫模式
    private boolean isStarting = false;

    public void start() {
        // start已经在做事情
        synchronized (this) {
            if (isStarting) {
                return;
            }
            isStarting = true;
        }
        thread = new Thread(() -> {
           while (true) {
               Thread current = Thread.currentThread();
               if (stop) {
                   log.debug("料理后事");
                   break;
               }

               try {
                   Thread.sleep(1000);
                   log.debug("将结果保存起来");
               } catch (InterruptedException e) {
                   e.printStackTrace();
               }
           }
        }, "监控线程");
        thread.start();
    }

    public void stop() {
        stop = true;
        thread.interrupt();
    }

}
