package com.hospital;

/**
 * Reader thread representing an auditor that reads system policies.
 * Implements the Reader role in the Reader-Writer pattern.
 * 
 * @author Hospital Diagnostics System
 * @version 1.0
 */
public class Reader implements Runnable {
    private final PolicyRWMonitor policy;
    private final String auditorName;
    private final int sleepMs;
    private volatile boolean running = true;

    private int readCount = 0;

    /**
     * Creates a new Reader thread.
     * 
     * @param policy The shared policy monitor
     * @param auditorName Name of the auditor (for logging)
     * @param sleepMs Time to sleep between reads
     */
    public Reader(PolicyRWMonitor policy, String auditorName, int sleepMs) {
        this.policy = policy;
        this.auditorName = auditorName;
        this.sleepMs = sleepMs;
    }

    /**
     * Signals the reader to stop reading.
     */
    public void stop() {
        running = false;
    }

    @Override
    public void run() {
        while (running) {
            try {
                // Acquire read access
                policy.startRead();
                
                // Perform the read operation
                String currentPolicy = policy.getPolicy();
                readCount++;
                System.out.println("[" + auditorName + "] Read policy: " + currentPolicy);
                
                // Release read access
                policy.endRead();

                // Simulate time between reads
                Thread.sleep(sleepMs);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
        System.out.println("[" + auditorName + "] Stopped. Total reads: " + readCount);
    }

    /**
     * Returns the total number of successful reads.
     * 
     * @return Read count
     */
    public int getReadCount() {
        return readCount;
    }
}
