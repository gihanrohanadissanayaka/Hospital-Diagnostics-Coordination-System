package com.hospital.partd;

public class ReaderD implements Runnable {
    private final PolicyRWMonitorD policy;
    private final String auditorName;
    private final int sleepMs;
    private volatile boolean running = true;
    private int readCount = 0;

    public ReaderD(PolicyRWMonitorD policy, String auditorName, int sleepMs) {
        this.policy = policy;
        this.auditorName = auditorName;
        this.sleepMs = sleepMs;
    }

    public void stop() {
        running = false;
    }

    @Override
    public void run() {
        while (running) {
            try {
                policy.startRead();
                try {
                    String currentPolicy = policy.getPolicy();
                    readCount++;
                    System.out.println("[" + auditorName + "] Read policy: " + currentPolicy);
                } finally {
                    policy.endRead();
                }
                Thread.sleep(sleepMs);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
        System.out.println("[" + auditorName + "] Stopped. Total reads: " + readCount);
    }

    public int getReadCount() {
        return readCount;
    }
}
