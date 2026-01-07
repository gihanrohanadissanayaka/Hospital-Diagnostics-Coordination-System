package com.hospital;

import java.util.Random;

public class Producer implements Runnable {
    private final String clinicName;
    private final BoundedQueueMonitor queue;
    private final int numOrders;
    private final long interval;
    private final Random random = new Random();
    private final String[] testTypes = {"XRay", "CTScan", "MRI", "BloodTest"};
    
    public Producer(String clinicName, BoundedQueueMonitor queue, int numOrders, long interval) {
        this.clinicName = clinicName;
        this.queue = queue;
        this.numOrders = numOrders;
        this.interval = interval;
    }
    
    @Override
    public void run() {
        try {
            for (int i = 1; i <= numOrders; i++) {
                String testType = testTypes[random.nextInt(testTypes.length)];
                String priority = "P" + (random.nextInt(3) + 1);
                String patientId = clinicName + "-" + priority;
                
                TestOrder order = new TestOrder(clinicName, testType, patientId);
                System.out.printf("[%s] Created: %s%n", clinicName, order);
                
                queue.put(order);
                System.out.printf("[%s] Queued: %s%n", clinicName, order);
                
                if (interval > 0) {
                    Thread.sleep(interval + random.nextInt(50));
                }
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        System.out.printf("[%s] Stopped%n", clinicName);
    }
}
