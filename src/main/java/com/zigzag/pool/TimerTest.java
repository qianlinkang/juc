package com.zigzag.pool;

import lombok.extern.slf4j.Slf4j;

import java.util.Timer;
import java.util.TimerTask;

/**
 * @author qlk
 */
@Slf4j(topic = "c.TimerTest")
public class TimerTest {
    public static void main(String[] args) {
        Timer timer = new Timer();

        TimerTask task1 = new TimerTask() {
            @Override
            public void run() {
                log.debug("task 1");
                try {
                    // 加入延时后,task2的开始执行时间也延后了
                    Thread.sleep(2000);
                    // 加入异常后,task2的任务就不执行了
                    // int i = 2 / 0;
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };

        TimerTask task2 = new TimerTask() {
            @Override
            public void run() {
                log.debug("task 2");
            }
        };

        timer.schedule(task1, 1000);
        timer.schedule(task2, 1000);
    }
}
