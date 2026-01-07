package com.hospital;

import java.util.Random;

public class Consumer implements Runnable {
    private final String analyzerId;
    private final BoundedQueueMonitor queue;
    private final long timeout;
    private final Random random = new Random();
    private volatile boolean running = true;
    
    public Consumer(String analyzerId, BoundedQueueMonitor queue, long timeout) {
        this.analyzerId = analyzerId;
        this.queue = queue;
        this.timeout = timeout;
    }
    
    public void stop() {
        running = false;
    }
    
    @Override
    public void run() {
        long startTime = System.currentTimeMillis();
        
        try {
            while (running && (System.currentTimeMillis() - startTime < timeout)) {
                TestOrder order = queue.take();
                long waitTime = System.currentTimeMillis() - order.getQueuedTime();
                
                System.out.printf("[%s] Processing: %s (waited %dms)%n", 
                    analyzerId, order, waitTime);
                
                // Simulate processing
                Thread.sleep(50 + random.nextInt(100));
                
                System.out.printf("[%s] Completed: %s%n", analyzerId, order);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        System.out.printf("[%s] Stopped%n", analyzerId);
    }
}
