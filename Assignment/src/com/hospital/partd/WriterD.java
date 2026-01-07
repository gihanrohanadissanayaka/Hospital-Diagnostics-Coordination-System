package com.hospital.partd;

public class WriterD implements Runnable {
    private final PolicyRWMonitorD policy;
    private final String supervisorName;
    private final int sleepMs;
    private volatile boolean running = true;
    private int writeCount = 0;
    private static final String[] POLICIES = { "NORMAL", "URGENT_PRIORITY", "MAINTENANCE" };

    public WriterD(PolicyRWMonitorD policy, String supervisorName, int sleepMs) {
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
                try {
                    policyIndex = (policyIndex + 1) % POLICIES.length;
                    String newPolicy = POLICIES[policyIndex];
                    policy.setPolicy(newPolicy);
                    writeCount++;
                    System.out.println("[" + supervisorName + "] Updated policy to: " + newPolicy);
                } finally {
                    policy.endWrite();
                }
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
