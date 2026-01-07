package com.hospital;

import java.util.Random;

public class Producer implements Runnable {
	// shared resource between producers and consumers 
    private final BoundedQueueMonitor queue;
    private final String clinicName;
    private final int sleepMs; // Time for which we put the current Thread into sleep
    // sleepMs amount of time in Millisecond the thread has to go into TIMED_WAITING state
    private volatile boolean running = true; // if this true producer will keep running if it is set to false
    // system has to be shutdown

    private static final String[] TEST_TYPES = { "BloodTest", "XRay", "MRI", "CTScan" };
    private static final Random random = new Random();

    public Producer(BoundedQueueMonitor queue, String clinicName, int sleepMs) {
        this.queue = queue;
        this.clinicName = clinicName;
        this.sleepMs = sleepMs;
    }

    /*
     * when this method is called Thread will be signaled to stop
     */
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
                queue.put(order);// adding an test order to the bounded queue 
                System.out.println("[" + clinicName + "] Queued: " + order);

                Thread.sleep(sleepMs); // just to simulate some work is going on 
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
        System.out.println("[" + clinicName + "] Stopped");
    }
}
