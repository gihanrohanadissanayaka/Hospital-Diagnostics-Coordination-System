package com.hospital;

public class Consumer implements Runnable {
	// shared resource - one instance of shared resource is created and shared between produce and consumer 
    private final BoundedQueueMonitor queue;
    private final String analyzerName; // name for the consumer 
    private final int processingTimeMs; // sleep time to represent the processing 
    private volatile boolean running = true; // as long as this variable is TRUE the thread will be running 

    public Consumer(BoundedQueueMonitor queue, String analyzerName, int processingTimeMs) {
        this.queue = queue;
        this.analyzerName = analyzerName;
        this.processingTimeMs = processingTimeMs;
    }

    // signal to stop the thread 
    public void stop() {
        running = false;
    }

    @Override
    public void run() {
        while (running) {
            try {
                TestOrder order = queue.take(); // consumer consumes a TestOrder
                long waitTime = System.currentTimeMillis() - order.getCreatedAt(); // from the time it was created how much time the Tested was staying in the system

                System.out.println("[" + analyzerName + "] Processing: " + order + " (waited " + waitTime + "ms)");
                Thread.sleep(processingTimeMs); // simulate the processing 
                System.out.println("[" + analyzerName + "] Completed: " + order);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
        System.out.println("[" + analyzerName + "] Stopped");
    }
}
