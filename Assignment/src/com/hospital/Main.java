package com.hospital;

// Simulator 
public class Main {
    public static void main(String[] args) throws InterruptedException {
        runCalmWorkload();
        System.out.println("\n" + "=".repeat(50) + "\n");
        runSurgeWorkload();
    }

    public static void runCalmWorkload() throws InterruptedException {
        System.out.println("=== Workload A: CALM ===\n");

        BoundedQueueMonitor queue = new BoundedQueueMonitor(5);
        PolicyRWMonitor policy = new PolicyRWMonitor();

        Producer p1 = new Producer(queue, "ClinicA", 120);
        Producer p2 = new Producer(queue, "ClinicB", 100);
        Consumer c1 = new Consumer(queue, "Analyzer1", 70);
        Consumer c2 = new Consumer(queue, "Analyzer2", 80);
        Reader r1 = new Reader(policy, "Auditor1", 200);
        Writer w1 = new Writer(policy, "Supervisor1", 3000);

        Thread[] threads = {
                new Thread(p1), new Thread(p2),
                new Thread(c1), new Thread(c2),
                new Thread(r1), new Thread(w1)
        };

        for (Thread t : threads)
            t.start();
        Thread.sleep(5000); // main thread goes into TIMED_WAITING state for 5000 MS

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

        BoundedQueueMonitor queue = new BoundedQueueMonitor(5);
        PolicyRWMonitor policy = new PolicyRWMonitor();

        Producer p1 = new Producer(queue, "ER", 10);
        Producer p2 = new Producer(queue, "ICU", 15);
        Producer p3 = new Producer(queue, "WardA", 20);
        Producer p4 = new Producer(queue, "WardB", 10);
        Producer p5 = new Producer(queue, "Outpatient", 15);
        Consumer c1 = new Consumer(queue, "Analyzer1", 200);
        Consumer c2 = new Consumer(queue, "Analyzer2", 250);
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
