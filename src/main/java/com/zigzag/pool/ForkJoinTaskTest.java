package com.zigzag.pool;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;

/**
 * @author qlk
 */
public class ForkJoinTaskTest {
    public static void main(String[] args) {
        ForkJoinPool forkJoinPool = new ForkJoinPool(4);
        // 执行1~5的求和
        System.out.println(forkJoinPool.invoke(new AccTask(5)));
    }
}

@Slf4j(topic = "c.AccTask")
class AccTask extends RecursiveTask<Integer> {

    int n;

    public AccTask(int n) {
        this.n = n;
    }

    @Override
    public String toString() {
        return "AccTask{" +
                "n=" + n +
                '}';
    }

    @Override
    protected Integer compute() {
        if (n == 1) {
            log.debug("join(): {}", n);
            return n;
        }

        AccTask t1 = new AccTask(n-1);
        // 拆分
        t1.fork();
        log.debug("fork() {} + {}", n, t1);

        // 合并
        int result = n + t1.join();
        log.debug("join() {} + {} = {}", n, t1, result);
        return result;
    }
}
