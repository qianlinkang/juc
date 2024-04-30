package com.zigzag.pool;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;

/**
 * @author qlk
 */
public class ForkJoinTaskTest2 {
    public static void main(String[] args) {
        ForkJoinPool forkJoinPool = new ForkJoinPool(4);
        // 执行1~5的求和
        System.out.println(forkJoinPool.invoke(new AccTask2(1, 5)));
    }
}

@Slf4j(topic = "c.AccTask2")
class AccTask2 extends RecursiveTask<Integer> {

    int begin;
    int end;

    public AccTask2(int begin, int end) {
        this.begin = begin;
        this.end = end;
    }

    @Override
    public String toString() {
        return "AccTask2{" +
                "begin=" + begin +
                ", end=" + end +
                '}';
    }

    @Override
    protected Integer compute() {

        // 5,5: 直接返回5
        if (begin == end) {
            log.debug("join() {}", begin);
            return begin;
        }
        // 4,5 直接返回和
        if (end - begin == 1) {
            log.debug("join() {} + {} = {}", begin, end, end + begin);
            return end + begin;
        }

        // 二分
        int mid = (end + begin) / 2;

        AccTask2 t1 = new AccTask2(begin, mid);
        // 拆分
        t1.fork();
        AccTask2 t2 = new AccTask2(mid + 1, end);
        t2.fork();

        // 合并
        int result = t1.join() + t2.join();
        log.debug("join() {} + {} = {}", t1, t2, result);
        return result;
    }
}
