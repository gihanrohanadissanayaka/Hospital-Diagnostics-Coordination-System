package com.hospital;

public class Writer implements Runnable {
	// shared monitor object between reader and Writer
    private final PolicyRWMonitor policy;
    private final String supervisorName;
    private final int sleepMs;
    private volatile boolean running = true;

    private int writeCount = 0;
    private static final String[] POLICIES = { "NORMAL", "URGENT_PRIORITY", "MAINTENANCE" };

    public Writer(PolicyRWMonitor policy, String supervisorName, int sleepMs) {
        this.policy = policy;
        this.supervisorName = supervisorName;
        this.sleepMs = sleepMs;
    }

    public void stop() {
        running = false;
    }

    @Override
    public void run() {
        int policyIndex = 0;
        while (running) {
            try {
                Thread.sleep(sleepMs);

                policy.startWrite();
                policyIndex = (policyIndex + 1) % POLICIES.length;
                String newPolicy = POLICIES[policyIndex];
                policy.setPolicy(newPolicy); // write operation
                writeCount++;
                System.out.println("[" + supervisorName + "] Updated policy to: " + newPolicy);
                policy.endWrite();

            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
        System.out.println("[" + supervisorName + "] Stopped. Total writes: " + writeCount);
    }

    public int getWriteCount() {
        return writeCount;
    }
}
