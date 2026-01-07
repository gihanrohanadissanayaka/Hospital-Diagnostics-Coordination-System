package com.hospital;

import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        System.out.println("============================================================");
        System.out.println("PART D: ReadWriteLock with Fairness Comparison");
        System.out.println("============================================================");
        
        // Test with NON-FAIR lock
        System.out.println("\n=== TEST 1: NON-FAIR ReadWriteLock ===\n");
        runWorkload(false, "CALM");
        Thread.sleep(2000);
        runWorkload(false, "SURGE");
        
        Thread.sleep(3000);
        
        // Test with FAIR lock
        System.out.println("\n\n=== TEST 2: FAIR ReadWriteLock ===\n");
        runWorkload(true, "CALM");
        Thread.sleep(2000);
        runWorkload(true, "SURGE");
        
        System.out.println("\n============================================================");
        System.out.println("PART D: Simulation Complete");
        System.out.println("============================================================");
    }
    
    private static void runWorkload(boolean fair, String workloadType) throws InterruptedException {
        String fairnessMode = fair ? "FAIR" : "NON-FAIR";
        System.out.printf("=== Workload: %s (%s) ===%n%n", workloadType, fairnessMode);
        
        BoundedQueueMonitor queue = new BoundedQueueMonitor(5);
        PolicyRWMonitor policyMonitor = new PolicyRWMonitor(fair);
        
        List<Thread> threads = new ArrayList<>();
        List<Consumer> consumers = new ArrayList<>();
        List<Reader> readers = new ArrayList<>();
        
        if (workloadType.equals("CALM")) {
            // CALM: 2 Producers, 2 Consumers, 1 Reader, 1 Writer
            threads.add(new Thread(new Producer("ClinicA", queue, 40, 100), "ClinicA"));
            threads.add(new Thread(new Producer("ClinicB", queue, 46, 100), "ClinicB"));
            
            Consumer c1 = new Consumer("Analyzer1", queue, 10000);
            Consumer c2 = new Consumer("Analyzer2", queue, 10000);
            consumers.add(c1);
            consumers.add(c2);
            threads.add(new Thread(c1, "Analyzer1"));
            threads.add(new Thread(c2, "Analyzer2"));
            
            Reader r1 = new Reader("Auditor1", policyMonitor, 10000, 400);
            readers.add(r1);
            threads.add(new Thread(r1, "Auditor1"));
            
            threads.add(new Thread(new Writer("Supervisor1", policyMonitor, 10000), "Supervisor1"));
            
        } else {
            // SURGE: 5 Producers, 2 Consumers, 3 Readers, 1 Writer
            threads.add(new Thread(new Producer("ER", queue, 12, 100), "ER"));
            threads.add(new Thread(new Producer("ICU", queue, 11, 100), "ICU"));
            threads.add(new Thread(new Producer("WardA", queue, 10, 100), "WardA"));
            threads.add(new Thread(new Producer("WardB", queue, 11, 100), "WardB"));
            threads.add(new Thread(new Producer("Outpatient", queue, 11, 100), "Outpatient"));
            
            Consumer c1 = new Consumer("Analyzer1", queue, 10000);
            Consumer c2 = new Consumer("Analyzer2", queue, 10000);
            consumers.add(c1);
            consumers.add(c2);
            threads.add(new Thread(c1, "Analyzer1"));
            threads.add(new Thread(c2, "Analyzer2"));
            
            Reader r1 = new Reader("Auditor1", policyMonitor, 10000, 100);
            Reader r2 = new Reader("Auditor2", policyMonitor, 10000, 100);
            Reader r3 = new Reader("Auditor3", policyMonitor, 10000, 100);
            readers.add(r1);
            readers.add(r2);
            readers.add(r3);
            threads.add(new Thread(r1, "Auditor1"));
            threads.add(new Thread(r2, "Auditor2"));
            threads.add(new Thread(r3, "Auditor3"));
            
            threads.add(new Thread(new Writer("Supervisor1", policyMonitor, 10000), "Supervisor1"));
        }
        
        // Start all threads
        for (Thread t : threads) {
            t.start();
        }
        
        // Wait for all threads
        for (Thread t : threads) {
            t.join();
        }
        
        System.out.printf("%n=== %s (%s) Complete ===%n", workloadType, fairnessMode);
        System.out.println();
    }
}
