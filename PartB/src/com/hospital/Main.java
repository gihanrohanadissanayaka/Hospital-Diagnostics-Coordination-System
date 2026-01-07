package com.hospital;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

/**
 * Main simulator class for Part B: Using BlockingQueue.
 * 
 * This implementation demonstrates the Producer-Consumer pattern using Java's
 * BlockingQueue instead of custom monitors. BlockingQueue handles all
 * synchronization internally, simplifying the implementation.
 * 
 * Key Differences from Part A:
 * - Uses ArrayBlockingQueue instead of BoundedQueueMonitor
 * - No manual wait()/notifyAll() required
 * - Simpler and more maintainable code
 * - Built-in thread safety
 * 
 * This class runs two different workload scenarios:
 * 1. CALM - Light workload with 2 producers, 2 consumers, 1 reader, 1 writer
 * 2. SURGE - Heavy workload with 5 producers, 2 consumers, 3 readers, 1 writer
 * 
 * @author Hospital Diagnostics System
 * @version 1.0
 */
public class Main {
    public static void main(String[] args) throws InterruptedException {
        System.out.println("=".repeat(60));
        System.out.println("PART B: Using BlockingQueue");
        System.out.println("=".repeat(60));
        
        runCalmWorkload();
        System.out.println("\n" + "=".repeat(60) + "\n");
        runSurgeWorkload();
        
        System.out.println("\n" + "=".repeat(60));
        System.out.println("PART B: Simulation Complete");
        System.out.println("=".repeat(60));
    }

    /**
     * Workload A: CALM - Light workload scenario.
     */
    public static void runCalmWorkload() throws InterruptedException {
        System.out.println("=== Workload A: CALM ===\n");

        // Using ArrayBlockingQueue - a bounded, thread-safe queue
        BlockingQueue<TestOrder> queue = new ArrayBlockingQueue<>(5);
        PolicyRWMonitor policy = new PolicyRWMonitor();

        // Create producer threads (clinics)
        Producer p1 = new Producer(queue, "ClinicA", 120);
        Producer p2 = new Producer(queue, "ClinicB", 100);
        
        // Create consumer threads (analyzers)
        Consumer c1 = new Consumer(queue, "Analyzer1", 70);
        Consumer c2 = new Consumer(queue, "Analyzer2", 80);
        
        // Create reader thread (auditor)
        Reader r1 = new Reader(policy, "Auditor1", 200);
        
        // Create writer thread (supervisor)
        Writer w1 = new Writer(policy, "Supervisor1", 3000);

        Thread[] threads = {
                new Thread(p1), new Thread(p2),
                new Thread(c1), new Thread(c2),
                new Thread(r1), new Thread(w1)
        };

        // Start all threads
        for (Thread t : threads) {
            t.start();
        }
        
        // Let the system run for 5 seconds
        Thread.sleep(5000);

        // Signal all threads to stop
        p1.stop();
        p2.stop();
        c1.stop();
        c2.stop();
        r1.stop();
        w1.stop();
        
        // Interrupt all threads
        for (Thread t : threads) {
            t.interrupt();
        }
        
        // Wait for all threads to finish
        for (Thread t : threads) {
            t.join();
        }

        System.out.println("\n=== CALM Complete ===");
    }

    /**
     * Workload B: SURGE - Heavy workload scenario.
     */
    public static void runSurgeWorkload() throws InterruptedException {
        System.out.println("=== Workload B: SURGE ===\n");

        // Using ArrayBlockingQueue with same capacity as Part A for comparison
        BlockingQueue<TestOrder> queue = new ArrayBlockingQueue<>(5);
        PolicyRWMonitor policy = new PolicyRWMonitor();

        // Create producer threads (multiple clinics/departments)
        Producer p1 = new Producer(queue, "ER", 10);
        Producer p2 = new Producer(queue, "ICU", 15);
        Producer p3 = new Producer(queue, "WardA", 20);
        Producer p4 = new Producer(queue, "WardB", 10);
        Producer p5 = new Producer(queue, "Outpatient", 15);
        
        // Create consumer threads (analyzers)
        Consumer c1 = new Consumer(queue, "Analyzer1", 200);
        Consumer c2 = new Consumer(queue, "Analyzer2", 250);
        
        // Create reader threads (auditors)
        Reader r1 = new Reader(policy, "Auditor1", 50);
        Reader r2 = new Reader(policy, "Auditor2", 75);
        Reader r3 = new Reader(policy, "Auditor3", 100);
        
        // Create writer thread (supervisor)
        Writer w1 = new Writer(policy, "Supervisor1", 1500);

        Thread[] threads = {
                new Thread(p1), new Thread(p2), new Thread(p3), new Thread(p4), new Thread(p5),
                new Thread(c1), new Thread(c2),
                new Thread(r1), new Thread(r2), new Thread(r3),
                new Thread(w1)
        };

        // Start all threads
        for (Thread t : threads) {
            t.start();
        }
        
        // Let the system run for 5 seconds
        Thread.sleep(5000);

        // Signal all threads to stop
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
        
        // Interrupt all threads
        for (Thread t : threads) {
            t.interrupt();
        }
        
        // Wait for all threads to finish
        for (Thread t : threads) {
            t.join();
        }

        System.out.println("\n=== SURGE Complete ===");
    }
}
