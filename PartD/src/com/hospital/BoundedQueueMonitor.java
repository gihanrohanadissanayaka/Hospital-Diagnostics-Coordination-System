package com.hospital;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class BoundedQueueMonitor {
    private final Queue<TestOrder> queue = new LinkedList<>();
    private final int capacity;
    private final Lock lock = new ReentrantLock();
    private final Condition notFull = lock.newCondition();
    private final Condition notEmpty = lock.newCondition();
    
    public BoundedQueueMonitor(int capacity) {
        this.capacity = capacity;
    }
    
    public void put(TestOrder order) throws InterruptedException {
        lock.lock();
        try {
            while (queue.size() >= capacity) {
                notFull.await();
            }
            order.setQueuedTime(System.currentTimeMillis());
            queue.offer(order);
            notEmpty.signal();
        } finally {
            lock.unlock();
        }
    }
    
    public TestOrder take() throws InterruptedException {
        lock.lock();
        try {
            while (queue.isEmpty()) {
                notEmpty.await();
            }
            TestOrder order = queue.poll();
            notFull.signal();
            return order;
        } finally {
            lock.unlock();
        }
    }
}
