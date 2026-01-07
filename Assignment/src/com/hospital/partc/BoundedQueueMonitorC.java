package com.hospital.partc;

import com.hospital.TestOrder;
import java.util.LinkedList;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class BoundedQueueMonitorC {
    private final LinkedList<TestOrder> queue = new LinkedList<>();
    private final int capacity;
    private final ReentrantLock lock = new ReentrantLock();
    private final Condition notFull = lock.newCondition(); // condition on producers will wait - separate wait set for producer 
    private final Condition notEmpty = lock.newCondition(); // condition on consumers will wait - separate wait set for consumer

    public BoundedQueueMonitorC(int capacity) {
        this.capacity = capacity;
    }

    public void put(TestOrder order) throws InterruptedException {
        lock.lock(); // lock
        try {
            while (queue.size() == capacity) {
                notFull.await();
            }
            queue.add(order);
            notEmpty.signalAll();
        } finally {
            lock.unlock(); // unlock - unlock has to happen in the finally block 
        }
    }

    public TestOrder take() throws InterruptedException {
        lock.lock();
        try {
            while (queue.isEmpty()) {
                notEmpty.await();// consumer goes into waiting state on notEmpty condition 
            }
            TestOrder order = queue.removeFirst();
            notFull.signalAll(); // wakes up producer 
            return order;
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
}
