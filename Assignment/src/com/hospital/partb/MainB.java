package com.hospital.partb;

import com.hospital.Reader;
import com.hospital.TestOrder;
import com.hospital.Writer;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import com.hospital.PolicyRWMonitor;

public class MainB {
    public static void main(String[] args) throws InterruptedException {
        System.out.println("=== PART B: Using BlockingQueue ===\n");
        runCalmWorkload();
        System.out.println("\n" + "=".repeat(50) + "\n");
        runSurgeWorkload();
    }

    public static void runCalmWorkload() throws InterruptedException {
        System.out.println("=== Workload A: CALM ===\n");

        BlockingQueue<TestOrder> queue = new ArrayBlockingQueue<>(5); // shared resource between producer and consumer 
        // so separate user defined monitor class 
        PolicyRWMonitor policy = new PolicyRWMonitor();

        ProducerB p1 = new ProducerB(queue, "ClinicA", 120);
        ProducerB p2 = new ProducerB(queue, "ClinicB", 100);
        ConsumerB c1 = new ConsumerB(queue, "Analyzer1", 70);
        ConsumerB c2 = new ConsumerB(queue, "Analyzer2", 80);
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

        BlockingQueue<TestOrder> queue = new ArrayBlockingQueue<>(5);
        PolicyRWMonitor policy = new PolicyRWMonitor();

        ProducerB p1 = new ProducerB(queue, "ER", 10);
        ProducerB p2 = new ProducerB(queue, "ICU", 15);
        ProducerB p3 = new ProducerB(queue, "WardA", 20);
        ProducerB p4 = new ProducerB(queue, "WardB", 10);
        ProducerB p5 = new ProducerB(queue, "Outpatient", 15);
        ConsumerB c1 = new ConsumerB(queue, "Analyzer1", 200);
        ConsumerB c2 = new ConsumerB(queue, "Analyzer2", 250);
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
