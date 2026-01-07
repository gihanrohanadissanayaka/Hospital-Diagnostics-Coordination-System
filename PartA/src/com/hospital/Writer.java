package com.hospital;

/**
 * Writer thread representing a supervisor that updates system policies.
 * Implements the Writer role in the Reader-Writer pattern.
 * 
 * @author Hospital Diagnostics System
 * @version 1.0
 */
public class Writer implements Runnable {
    private final PolicyRWMonitor policy;
    private final String supervisorName;
    private final int sleepMs;
    private volatile boolean running = true;

    private int writeCount = 0;
    private static final String[] POLICIES = { "NORMAL", "URGENT_PRIORITY", "MAINTENANCE" };

    /**
     * Creates a new Writer thread.
     * 
     * @param policy The shared policy monitor
     * @param supervisorName Name of the supervisor (for logging)
     * @param sleepMs Time to sleep between writes
     */
    public Writer(PolicyRWMonitor policy, String supervisorName, int sleepMs) {
        this.policy = policy;
        this.supervisorName = supervisorName;
        this.sleepMs = sleepMs;
    }

    /**
     * Signals the writer to stop writing.
     */
    public void stop() {
        running = false;
    }

    @Override
    public void run() {
        int policyIndex = 0;
        while (running) {
            try {
                // Simulate time between policy updates
                Thread.sleep(sleepMs);

                // Acquire write access
                policy.startWrite();
                
                // Perform the write operation
                policyIndex = (policyIndex + 1) % POLICIES.length;
                String newPolicy = POLICIES[policyIndex];
                policy.setPolicy(newPolicy);
                writeCount++;
                System.out.println("[" + supervisorName + "] Updated policy to: " + newPolicy);
                
                // Release write access
                policy.endWrite();

            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
        System.out.println("[" + supervisorName + "] Stopped. Total writes: " + writeCount);
    }

    /**
     * Returns the total number of successful writes.
     * 
     * @return Write count
     */
    public int getWriteCount() {
        return writeCount;
    }
}
