package com.zigzag.juc;

import lombok.extern.slf4j.Slf4j;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashSet;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author qlk
 */
@Slf4j(topic = "c.ThreadPoolTest")
public class ThreadPoolTest {
    public static void main(String[] args) {
//        ThreadPool threadPool = new ThreadPool(2, 1000, TimeUnit.MILLISECONDS, 10);
//        for (int i = 0; i < 5; i++) {
//            int j = i;
//            threadPool.execute(() -> {
//                log.debug("{}", j);
//            });
//        }

        // 当任务队列满时
//        for (int i = 0; i < 15; i++) {
//            int j = i;
//            threadPool.execute(() -> {
//                try {
//                    Thread.sleep(1000000L);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//                log.debug("{}", j);
//            });
//        }

        ThreadPool threadPool = new ThreadPool(2,
                1000, TimeUnit.MILLISECONDS,
                10,
                (queue, task) -> {
                    // 1.死等
                    //  queue.put(task);
                    // 2.带超时等待
//                     queue.offer(task, 1000, TimeUnit.MILLISECONDS);
                    // 3.让调用者放弃任务执行
                    // log.debug("放弃{}", task);
                    // 4.让调用者抛出异常
                    // throw new RuntimeException("任务执行失败 " + task);
                    // 5.让调用者自己执行任务
                    task.run();
                });

                for (int i = 0; i < 15; i++) {
                    int j = i;
                    threadPool.execute(() -> {
                        try {
                            Thread.sleep(1000L);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        log.debug("{}", j);
                    });
                }
    }
}

/**
 * 自定义任务队列,缓冲生产者和消费者的任务调度
 */
@Slf4j(topic = "c.BlockingQueue")
class BlockingQueue<T> {

    // 1. 任务队列容器
    private Deque<T> queue = new ArrayDeque<>();

    // 2. 锁
    private ReentrantLock lock = new ReentrantLock();

    // 3. 生产者条件变量
    // 队列满时,生产者等待
    private Condition fullWaitSet = lock.newCondition();

    // 4. 消费者条件变量
    // 队列空时,消费者等待
    private Condition emptyWaitSet = lock.newCondition();

    // 5. 容量
    private int capacity;

    public BlockingQueue(int capacity) {
        this.capacity = capacity;
    }

    /**
     * 阻塞获取
     * 队列空时,消费者等待
     * 队列非空,消费者取出最先入队的T,同时唤醒生产者
     */
    public T take() {
        lock.lock();
        try {
            while (queue.isEmpty()) {
                try {
                    emptyWaitSet.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            T t = queue.removeFirst();
            fullWaitSet.signal();
            return t;
        } finally {
            lock.unlock();
        }
    }

    /**
     * 阻塞添加
     * 队列满时,生产者等待
     * 队列不满时,生产者放入T,同时唤醒消费者
     */
    public void put(T task) {
        lock.lock();
        try {
            while (queue.size() == capacity) {
                try {
                    log.debug("等待加入任务队列 {} ...", task);
                    fullWaitSet.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            log.debug("加入任务队列 {}", task);
            queue.addLast(task);
            emptyWaitSet.signal();
        } finally {
            lock.unlock();
        }
    }

    public int size() {
        lock.lock();
        try {
            return queue.size();
        } finally {
            lock.unlock();
        }
    }

    /**
     * 带超时的阻塞获取
     */
    public T poll(long timeout, TimeUnit unit) {
        lock.lock();
        try {
            // 将timeout统一转换成nanos
            long nanos = unit.toNanos(timeout);
            while (queue.isEmpty()) {
                try {
                    // awaitNanos返回的时剩余等待时间
                    // 当剩余等待时间 < 0时,则无需等待,返回null
                    if (nanos < 0) {
                        return null;
                    }
                    nanos = emptyWaitSet.awaitNanos(nanos);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            T t = queue.removeFirst();
            fullWaitSet.signal();
            return t;
        } finally {
            lock.unlock();
        }
    }

    /**
     * 带超时的阻塞添加
     */
    public boolean offer(T task, long timeout, TimeUnit unit) {
        lock.lock();
        try {
            long nanos = unit.toNanos(timeout);
            while (queue.size() == capacity) {
                try {
                    // 等待超时,put失败
                    if (nanos < 0) {
                        return false;
                    }
                    log.debug("等待加入任务队列 {} ...", task);
                    nanos = fullWaitSet.awaitNanos(nanos);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            log.debug("加入任务队列 {}", task);
            queue.addLast(task);
            emptyWaitSet.signal();
            return true;
        } finally {
            lock.unlock();
        }
    }

    /**
     * 带拒绝策略的put
     *
     * @param rejectPolicy
     * @param task
     */
    public void tryPut(RejectPolicy<T> rejectPolicy, T task) {
        lock.lock();
        try {
            // 判断队列是否满
            if (queue.size() == capacity) {
                rejectPolicy.reject(this, task);
            } else {
                log.debug("加入任务队列 {}", task);
                queue.addLast(task);
                emptyWaitSet.signal();
            }
        } finally {
            lock.unlock();
        }
    }
}

/**
 * 自定义线程池
 */
@Slf4j(topic = "c.ThreadPool")
class ThreadPool {
    // 任务队列
    private BlockingQueue<Runnable> taskQueue;

    // 线程集合
    private HashSet<Worker> workers = new HashSet<>();

    // 核心线程数
    private int coreSize;

    // 获取任务的超时时间
    private long timeout;
    private TimeUnit unit;

    // 拒绝策略
    private RejectPolicy<Runnable> rejectPolicy;

    public ThreadPool(int coreSize, long timeout, TimeUnit unit, int queueCapacity, RejectPolicy<Runnable> rejectPolicy) {
        this.coreSize = coreSize;
        this.timeout = timeout;
        this.unit = unit;
        this.taskQueue = new BlockingQueue<>(queueCapacity);
        this.rejectPolicy = rejectPolicy;
    }

    // 执行任务
    public void execute(Runnable task) {
        // 当任务数没有超过核心数是,直接交给worker对象执行
        // 如果任务数超过核心数时,加入任务队列暂存
        synchronized (workers) {
            if (workers.size() < coreSize) {
                Worker worker = new Worker(task);
                log.debug("新增 worker{}, {}", worker, task);
                workers.add(worker);
                worker.start();
            } else {
                // 等待策略(拒绝策略)
                // 1. 死等
//                taskQueue.put(task);

                // 使用拒绝策略接口
                // 2. 带超时的等待
                // taskQueue.poll
                // 3. 让调用者放弃任务执行

                // 4. 让调用者抛出异常

                // 5.让调用者自己执行任务

                taskQueue.tryPut(rejectPolicy, task);
            }
        }

    }

    class Worker extends Thread {

        private Runnable task;

        public Worker(Runnable task) {
            this.task = task;
        }

        @Override
        public void run() {
            // 执行任务
            // 1. 当Worker初始化过来的task不为空时,执行task
            // 2. 当task执行玩不,再接着从任务队列中获取任务并执行
            while (task != null || (task = taskQueue.poll(timeout, unit)) != null) {
//            while (task != null || (task = taskQueue.take()) != null) {
                try {
                    log.debug("正在执行...{}", task);
                    task.run();
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    task = null;
                }
            }
            // 没有任务需要执行时,移除worker
            synchronized (workers) {
                log.debug("worker 被移除{}", this);
                workers.remove(this);
            }
        }
    }
}


/**
 * 拒绝策略
 */
@FunctionalInterface
interface RejectPolicy<T> {
    void reject(BlockingQueue<T> queue, T task);
}