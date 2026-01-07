package com.hospital;

import java.util.concurrent.BlockingQueue;

/**
 * Consumer thread representing a diagnostic analyzer that processes test orders.
 * Implements the Consumer role in the Producer-Consumer pattern.
 * Uses BlockingQueue instead of custom monitor.
 * 
 * @author Hospital Diagnostics System
 * @version 1.0
 */
public class Consumer implements Runnable {
    private final BlockingQueue<TestOrder> queue;
    private final String analyzerName;
    private final int processingTimeMs;
    private volatile boolean running = true;

    /**
     * Creates a new Consumer thread.
     * 
     * @param queue The shared BlockingQueue
     * @param analyzerName Name of the analyzer (for logging)
     * @param processingTimeMs Time to process each order (simulates analysis time)
     */
    public Consumer(BlockingQueue<TestOrder> queue, String analyzerName, int processingTimeMs) {
        this.queue = queue;
        this.analyzerName = analyzerName;
        this.processingTimeMs = processingTimeMs;
    }

    /**
     * Signals the consumer to stop consuming.
     */
    public void stop() {
        running = false;
    }

    @Override
    public void run() {
        while (running) {
            try {
                // Get a test order from the queue (BlockingQueue.take() blocks automatically if empty)
                TestOrder order = queue.take();
                long waitTime = System.currentTimeMillis() - order.getCreatedAt();

                System.out.println("[" + analyzerName + "] Processing: " + order + " (waited " + waitTime + "ms)");
                
                // Simulate processing time
                Thread.sleep(processingTimeMs);
                
                System.out.println("[" + analyzerName + "] Completed: " + order);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
        System.out.println("[" + analyzerName + "] Stopped");
    }
}
