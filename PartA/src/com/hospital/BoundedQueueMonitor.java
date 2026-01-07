package com.hospital;

import java.util.LinkedList;

/**
 * Monitor class implementing the Producer-Consumer pattern using synchronized methods.
 * This class uses implicit locks (synchronized keyword) with wait() and notifyAll().
 * 
 * Shared resource between Producer threads (clinics) and Consumer threads (analyzers).
 * 
 * @author Hospital Diagnostics System
 * @version 1.0
 */
public class BoundedQueueMonitor {
    private final LinkedList<TestOrder> queue = new LinkedList<>();
    private final int capacity;

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
     * @param order The test order to add
     * @throws InterruptedException if the thread is interrupted while waiting
     */
    public synchronized void put(TestOrder order) throws InterruptedException {
        while (queue.size() == capacity) {
            // Queue is full - producer must wait
            wait();
        }
        queue.add(order);
        // Notify all waiting consumers that an item is available
        notifyAll();
    }

    /**
     * Consumer method: Removes and returns a test order from the queue.
     * Blocks if the queue is empty.
     * 
     * @return The test order at the front of the queue
     * @throws InterruptedException if the thread is interrupted while waiting
     */
    public synchronized TestOrder take() throws InterruptedException {
        while (queue.isEmpty()) {
            // Queue is empty - consumer must wait
            wait();
        }
        TestOrder order = queue.removeFirst();
        // Notify all waiting producers that space is available
        notifyAll();
        return order;
    }

    /**
     * Returns the current size of the queue.
     * 
     * @return Number of test orders currently in the queue
     */
    public synchronized int size() {
        return queue.size();
    }
}
