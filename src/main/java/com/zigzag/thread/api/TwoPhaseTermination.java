package com.zigzag.thread.api;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;

/**
 * 两阶段终止
 * @author qlk
 */
@Slf4j(topic = "c.TwoPhaseTermination")
public class TwoPhaseTermination {

    public static void main(String[] args) throws InterruptedException {
        MonitorInterrupted monitor = new MonitorInterrupted();
        monitor.start();

        TimeUnit.MILLISECONDS.sleep(3500);
        log.debug("stop");
        monitor.stop();
    }
}

/**
 * 只有在阻塞是打断才会清除中断标记
 * 只需要重新在catch异常中再再次执行interrupt(),就会重置打断标记为true
 */
@Slf4j(topic = "c.Monitor")
class MonitorInterrupted {

    private Thread thread;

    public void start() {
        thread = new Thread(() -> {
            while (true) {
                Thread current = Thread.currentThread();
                if (current.isInterrupted()) {
                    log.debug("料理后事");
                    break;
                }

                try {
                    TimeUnit.SECONDS.sleep(1);
                    log.debug("等待1s后保存结果");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    // 睡眠时被打断,中断标记为false
                    // 重新设置打断标记为true
                    current.interrupt();
                }
            }
        }, "监控程序");

        thread.start();
    }

    public void stop() {
        // 正常运行执行打断,中断标志为true
        thread.interrupt();
    }
}