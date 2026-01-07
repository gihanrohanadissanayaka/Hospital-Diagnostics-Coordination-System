package com.hospital.partb;

import com.hospital.TestOrder;
import java.util.Random;
import java.util.concurrent.BlockingQueue;

public class ProducerB implements Runnable {
    private final BlockingQueue<TestOrder> queue;
    private final String clinicName;
    private final int sleepMs;
    private volatile boolean running = true;

    private static final String[] TEST_TYPES = { "BloodTest", "XRay", "MRI", "CTScan" };
    private static final Random random = new Random();

    public ProducerB( BlockingQueue<TestOrder> queue, String clinicName, int sleepMs) {
        this.queue = queue;
        this.clinicName = clinicName;
        this.sleepMs = sleepMs;
    }

    public void stop() {
        running = false;
    }

    @Override
    public void run() {
        int count = 0;
        while (running) {
            try {
                String patientId = clinicName + "-P" + (++count);
                String testType = TEST_TYPES[random.nextInt(TEST_TYPES.length)];
                int priority = random.nextInt(3) + 1;

                TestOrder order = new TestOrder(patientId, testType, priority);
                System.out.println("[" + clinicName + "] Created: " + order);
                queue.put(order);
                System.out.println("[" + clinicName + "] Queued: " + order);

                Thread.sleep(sleepMs);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
        System.out.println("[" + clinicName + "] Stopped");
    }
}
