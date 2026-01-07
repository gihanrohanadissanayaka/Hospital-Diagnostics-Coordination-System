package com.hospital;

import java.util.LinkedList;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Monitor class implementing the Producer-Consumer pattern using ReentrantLock and Condition.
 * This class uses explicit locks instead of synchronized methods.
 * 
 * Advantages over synchronized (Part A):
 * - Multiple condition variables (notFull, notEmpty)
 * - More flexible lock management
 * - Support for try-lock and timed locks
 * - Better separation of concerns
 * 
 * Shared resource between Producer threads (clinics) and Consumer threads (analyzers).
 * 
 * @author Hospital Diagnostics System
 * @version 1.0
 */
public class BoundedQueueMonitor {
    private final LinkedList<TestOrder> queue = new LinkedList<>();
    private final int capacity;
    
    // Explicit lock instead of implicit synchronized lock
    private final Lock lock = new ReentrantLock();
    
    // Separate condition variables for producers and consumers
    private final Condition notFull = lock.newCondition();  // For producers
    private final Condition notEmpty = lock.newCondition(); // For consumers

    /**
     * Creates a bounded queue with specified capacity.
     * 
     * @param capacity Maximum number of test orders that can be queued
     */
    public BoundedQueueMonitor(int capacity) {
        this.capacity = capacity;
    }

    /**
     * Producer method: Adds a test order to the queue.
     * Blocks if the queue is full (at capacity).
     * 
     * Uses ReentrantLock and Condition instead of synchronized/wait.
     * 
     * @param order The test order to add
     * @throws InterruptedException if the thread is interrupted while waiting
     */
    public void put(TestOrder order) throws InterruptedException {
        lock.lock(); // Explicitly acquire lock
        try {
            while (queue.size() == capacity) {
                // Queue is full - producer waits on notFull condition
                notFull.await(); // Similar to wait() but for specific condition
            }
            queue.add(order);
            // Signal waiting consumers that queue is not empty
            notEmpty.signal(); // Signal one waiting consumer
        } finally {
            lock.unlock(); // Always unlock in finally block
        }
    }

    /**
     * Consumer method: Removes and returns a test order from the queue.
     * Blocks if the queue is empty.
     * 
     * Uses ReentrantLock and Condition instead of synchronized/wait.
     * 
     * @return The test order at the front of the queue
     * @throws InterruptedException if the thread is interrupted while waiting
     */
    public TestOrder take() throws InterruptedException {
        lock.lock(); // Explicitly acquire lock
        try {
            while (queue.isEmpty()) {
                // Queue is empty - consumer waits on notEmpty condition
                notEmpty.await(); // Similar to wait() but for specific condition
            }
            TestOrder order = queue.removeFirst();
            // Signal waiting producers that queue is not full
            notFull.signal(); // Signal one waiting producer
            return order;
        } finally {
            lock.unlock(); // Always unlock in finally block
        }
    }

    /**
     * Returns the current size of the queue.
     * 
     * @return Number of test orders currently in the queue
     */
    public int size() {
        lock.lock();
        try {
            return queue.size();
        } finally {
            lock.unlock();
        }
    }
}
