package com.hospital.partc;

import com.hospital.Reader;
import com.hospital.Writer;
import com.hospital.PolicyRWMonitor;

public class MainC {
    public static void main(String[] args) throws InterruptedException {
        System.out.println("=== PART C: Using ReentrantLock + Condition ===\n");
        runCalmWorkload();
        System.out.println("\n" + "=".repeat(50) + "\n");
        runSurgeWorkload();
    }

    public static void runCalmWorkload() throws InterruptedException {
        System.out.println("=== Workload A: CALM ===\n");

        BoundedQueueMonitorC queue = new BoundedQueueMonitorC(5); // shared resource 
        PolicyRWMonitor policy = new PolicyRWMonitor();

        ProducerC p1 = new ProducerC(queue, "ClinicA", 120);
        ProducerC p2 = new ProducerC(queue, "ClinicB", 100);
        ConsumerC c1 = new ConsumerC(queue, "Analyzer1", 70);
        ConsumerC c2 = new ConsumerC(queue, "Analyzer2", 80);
        Reader r1 = new Reader(policy, "Auditor1", 200);
        Writer w1 = new Writer(policy, "Supervisor1", 3000);

        Thread[] threads = {
                new Thread(p1), new Thread(p2),
                new Thread(c1), new Thread(c2),
                new Thread(r1), new Thread(w1)
        };

        for (Thread t : threads)
            t.start();
        Thread.sleep(5000);

        p1.stop();
        p2.stop();
        c1.stop();
        c2.stop();
        r1.stop();
        w1.stop();
        for (Thread t : threads)
            t.interrupt();
        for (Thread t : threads)
            t.join();

        System.out.println("\n=== CALM Complete ===");
    }

    public static void runSurgeWorkload() throws InterruptedException {
        System.out.println("=== Workload B: SURGE ===\n");

        BoundedQueueMonitorC queue = new BoundedQueueMonitorC(5);
        PolicyRWMonitor policy = new PolicyRWMonitor();

        ProducerC p1 = new ProducerC(queue, "ER", 10);
        ProducerC p2 = new ProducerC(queue, "ICU", 15);
        ProducerC p3 = new ProducerC(queue, "WardA", 20);
        ProducerC p4 = new ProducerC(queue, "WardB", 10);
        ProducerC p5 = new ProducerC(queue, "Outpatient", 15);
        ConsumerC c1 = new ConsumerC(queue, "Analyzer1", 200);
        ConsumerC c2 = new ConsumerC(queue, "Analyzer2", 250);
        Reader r1 = new Reader(policy, "Auditor1", 50);
        Reader r2 = new Reader(policy, "Auditor2", 75);
        Reader r3 = new Reader(policy, "Auditor3", 100);
        Writer w1 = new Writer(policy, "Supervisor1", 1500);

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

        System.out.println("\n=== SURGE Complete ===");
    }
}
