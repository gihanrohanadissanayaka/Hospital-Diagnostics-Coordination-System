package com.hospital;

public class Writer implements Runnable {
    private final String supervisorId;
    private final PolicyRWMonitor monitor;
    private final long timeout;
    private final String[] policies = {"URGENT_PRIORITY", "MAINTENANCE", "NORMAL"};
    private int writeCount = 0;
    
    public Writer(String supervisorId, PolicyRWMonitor monitor, long timeout) {
        this.supervisorId = supervisorId;
        this.monitor = monitor;
        this.timeout = timeout;
    }
    
    @Override
    public void run() {
        long startTime = System.currentTimeMillis();
        
        try {
            int policyIndex = 0;
            while (System.currentTimeMillis() - startTime < timeout) {
                Thread.sleep(timeout / policies.length);
                
                if (System.currentTimeMillis() - startTime < timeout) {
                    monitor.writePolicy(policies[policyIndex]);
                    writeCount++;
                    System.out.printf("[%s] Updated policy to: %s%n", 
                        supervisorId, policies[policyIndex]);
                    policyIndex = (policyIndex + 1) % policies.length;
                }
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        System.out.printf("[%s] Stopped. Total writes: %d%n", supervisorId, writeCount);
    }
}
