package com.hospital.partd;

import com.hospital.partc.BoundedQueueMonitorC;
import com.hospital.partc.ProducerC;
import com.hospital.partc.ConsumerC;

public class MainD {
    public static void main(String[] args) throws InterruptedException {
        System.out.println("=== PART D: Using ReentrantReadWriteLock ===\n");

        System.out.println("--- Running with fairness=FALSE ---");
        runSurgeWorkload(false);

        System.out.println("\n" + "=".repeat(50) + "\n");

        System.out.println("--- Running with fairness=TRUE ---");
        runSurgeWorkload(true);
    }

    public static void runSurgeWorkload(boolean fair) throws InterruptedException {
        System.out.println("=== Workload B: SURGE (fair=" + fair + ") ===\n");

        BoundedQueueMonitorC queue = new BoundedQueueMonitorC(5);
        PolicyRWMonitorD policy = new PolicyRWMonitorD(fair);

        ProducerC p1 = new ProducerC(queue, "ER", 10);
        ProducerC p2 = new ProducerC(queue, "ICU", 15);
        ProducerC p3 = new ProducerC(queue, "WardA", 20);
        ProducerC p4 = new ProducerC(queue, "WardB", 10);
        ProducerC p5 = new ProducerC(queue, "Outpatient", 15);
        ConsumerC c1 = new ConsumerC(queue, "Analyzer1", 200);
        ConsumerC c2 = new ConsumerC(queue, "Analyzer2", 250);
        ReaderD r1 = new ReaderD(policy, "Auditor1", 50);
        ReaderD r2 = new ReaderD(policy, "Auditor2", 75);
        ReaderD r3 = new ReaderD(policy, "Auditor3", 100);
        WriterD w1 = new WriterD(policy, "Supervisor1", 1500);

        Thread[] threads = {
                new Thread(p1), new Thread(p2), new Thread(p3), new Thread(p4), new Thread(p5),
                new Thread(c1), new Thread(c2),
                new Thread(r1), new Thread(r2), new Thread(r3),
                new Thread(w1)
        };

        for (Thread t : threads)
            t.start();
        Thread.sleep(5000);

        p1.stop();
        p2.stop();
        p3.stop();
        p4.stop();
        p5.stop();
        c1.stop();
        c2.stop();
        r1.stop();
        r2.stop();
        r3.stop();
        w1.stop();
        for (Thread t : threads)
            t.interrupt();
        for (Thread t : threads)
            t.join();

        System.out.println("\n=== SURGE Complete (fair=" + fair + ") ===");
    }
}
