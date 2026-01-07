package com.hospital;

public class Reader implements Runnable {
    private final String auditorId;
    private final PolicyRWMonitor monitor;
    private final long timeout;
    private final long interval;
    private volatile boolean running = true;
    private int readCount = 0;
    
    public Reader(String auditorId, PolicyRWMonitor monitor, long timeout, long interval) {
        this.auditorId = auditorId;
        this.monitor = monitor;
        this.timeout = timeout;
        this.interval = interval;
    }
    
    public void stop() {
        running = false;
    }
    
    @Override
    public void run() {
        long startTime = System.currentTimeMillis();
        
        try {
            while (running && (System.currentTimeMillis() - startTime < timeout)) {
                String policy = monitor.readPolicy();
                readCount++;
                System.out.printf("[%s] Read policy: %s%n", auditorId, policy);
                
                if (interval > 0) {
                    Thread.sleep(interval);
                }
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        System.out.printf("[%s] Stopped. Total reads: %d%n", auditorId, readCount);
    }
}
