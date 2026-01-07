package com.hospital;

import java.util.Random;
import java.util.concurrent.BlockingQueue;

/**
 * Producer thread representing a clinic that creates diagnostic test orders.
 * Implements the Producer role in the Producer-Consumer pattern.
 * Uses BlockingQueue instead of custom monitor.
 * 
 * @author Hospital Diagnostics System
 * @version 1.0
 */
public class Producer implements Runnable {
    private final BlockingQueue<TestOrder> queue;
    private final String clinicName;
    private final int sleepMs;
    private volatile boolean running = true;

    private static final String[] TEST_TYPES = { "BloodTest", "XRay", "MRI", "CTScan" };
    private static final Random random = new Random();

    /**
     * Creates a new Producer thread.
     * 
     * @param queue The shared BlockingQueue
     * @param clinicName Name of the clinic (for logging)
     * @param sleepMs Time to sleep between producing orders (simulates workload)
     */
    public Producer(BlockingQueue<TestOrder> queue, String clinicName, int sleepMs) {
        this.queue = queue;
        this.clinicName = clinicName;
        this.sleepMs = sleepMs;
    }

    /**
     * Signals the producer to stop producing.
     */
    public void stop() {
        running = false;
    }

    @Override
    public void run() {
        int count = 0;
        while (running) {
            try {
                // Generate a new test order
                String patientId = clinicName + "-P" + (++count);
                String testType = TEST_TYPES[random.nextInt(TEST_TYPES.length)];
                int priority = random.nextInt(3) + 1;

                TestOrder order = new TestOrder(patientId, testType, priority);

                System.out.println("[" + clinicName + "] Created: " + order);
                queue.put(order); // BlockingQueue.put() blocks automatically if queue is full
                System.out.println("[" + clinicName + "] Queued: " + order);

                // Simulate time between order creation
                Thread.sleep(sleepMs);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
        System.out.println("[" + clinicName + "] Stopped");
    }
}
