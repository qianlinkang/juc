package com.zigzag.share;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

/**
 * 生产者消费者
 *
 * @author qlk
 */
@Slf4j(topic = "c.MessageQueue")
public class MessageQueue {
    private LinkedList<Message> queue;
    private int capacity;

    public MessageQueue(int capacity) {
        this.capacity = capacity;
        queue = new LinkedList<>();
    }

    public void put(Message message) {
        synchronized (queue) {
            while (queue.size() == capacity) {
                log.debug("库存已经达到上限, wait...");
                try {
                    queue.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            queue.addLast(message);
            queue.notifyAll();
        }
    }

    public Message take() {
        synchronized (queue) {
            while (queue.isEmpty()) {
                log.debug("没货了, wait...");
                try {
                    queue.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            Message message =queue.removeFirst();
            queue.notifyAll();
            return message;
        }
    }

    public static void main(String[] args) {
        MessageQueue messageQueue = new MessageQueue(2);
        // 4个生产者
        for (int i = 0; i < 4; i++) {
            int id = i;
            new Thread(() -> {

                try {
                    log.debug("download...");
                    List<String> resp = Downloader.download();
                    log.debug("try put message({})...", id);
                    messageQueue.put(new Message(id, resp));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }, "生产者" + i).start();
        }

        // 1个消费者
        new Thread(() -> {
            while (true) {
                Message message = messageQueue.take();
                List<String> response = (List<String>) message.getMessage();
                log.debug("take message({}): [{}] lines", message.getId(), response.size());
            }
        }, "消费者").start();
    }
}

class Message {

    private int id;
    private Object message;

    public Message(int id, Object message) {
        this.id = id;
        this.message = message;
    }

    public int getId() {
        return id;
    }

    public Object getMessage() {
        return message;
    }
}