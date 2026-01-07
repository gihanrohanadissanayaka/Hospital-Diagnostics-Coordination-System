package com.hospital.partb;

import java.util.concurrent.BlockingQueue;

import com.hospital.TestOrder;

public class ConsumerB implements Runnable {
    private final BlockingQueue<TestOrder> queue;
    private final String analyzerName;
    private final int processingTimeMs;
    private volatile boolean running = true;

    public ConsumerB(BlockingQueue<TestOrder> queue, String analyzerName, int processingTimeMs) {
        this.queue = queue;
        this.analyzerName = analyzerName;
        this.processingTimeMs = processingTimeMs;
    }

    public void stop() {
        running = false;
    }

    @Override
    public void run() {
        while (running) {
            try {
                TestOrder order = queue.take();
                long waitTime = System.currentTimeMillis() - order.getCreatedAt();

                System.out.println("[" + analyzerName + "] Processing: " + order + " (waited " + waitTime + "ms)");
                Thread.sleep(processingTimeMs);
                System.out.println("[" + analyzerName + "] Completed: " + order);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
        System.out.println("[" + analyzerName + "] Stopped");
    }
}
