package com.zigzag.share;

import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Vector;

/**
 * 售票练习
 *
 * @author qlk
 */
@Slf4j(topic = "c.ExerciseSellTest")
public class ExerciseSellTest {

    public static void main(String[] args) {
        TicketWindow ticketWindow = new TicketWindow(2000);

        List<Thread> list = new ArrayList<>();

        // 存储卖出多少张票
        List<Integer> sellAmount = new Vector<>();
        for (int i = 0; i < 4000; i++) {
            Thread t = new Thread(() -> {
                // 存在竞态条件
                int count = ticketWindow.sell(randomAmount());

                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                sellAmount.add(count);
            });
            list.add(t);
            t.start();
        }

        list.forEach(t -> {
            try {
                t.join();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });

        log.debug("卖出数量: {}", sellAmount.stream().mapToInt(i -> i).sum());

        log.debug("剩余数量: {}", ticketWindow.getCount());

    }

    static Random random = new Random();

    public static int randomAmount() {
        // 1到5的随机数
        return random.nextInt(5) + 1;
    }


}


class TicketWindow {
    private int count;

    public TicketWindow(int count) {
        this.count = count;
    }

    public int getCount() {
        return count;
    }

    public synchronized int sell(int amount) {
        if (this.count >= amount) {
            this.count -= amount;
            return amount;
        } else {
            return 0;
        }
    }
}
