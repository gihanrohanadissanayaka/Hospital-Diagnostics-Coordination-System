package com.hospital;

public class Reader implements Runnable {
	// instance of the monitor is the shared resource 
    private final PolicyRWMonitor policy;
    private final String auditorName; // name for the reader 
    private final int sleepMs; // simulation time 
    private volatile boolean running = true;

    private int readCount = 0;

    public Reader(PolicyRWMonitor policy, String auditorName, int sleepMs) {
        this.policy = policy;
        this.auditorName = auditorName;
        this.sleepMs = sleepMs;
    }

    public void stop() { // when called Reader thread will be signaled to stop
        running = false;
    }

    @Override
    public void run() {
        while (running) {
            try {
                policy.startRead();
                String currentPolicy = policy.getPolicy();
                readCount++;
                System.out.println("[" + auditorName + "] Read policy: " + currentPolicy);
                policy.endRead();

                Thread.sleep(sleepMs); // simulation of read operation
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
