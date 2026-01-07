package com.hospital;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Monitor class implementing the Reader-Writer pattern using ReentrantLock and Condition.
 * 
 * Rules:
 * - Multiple readers (0 to N) can read simultaneously when no writer is active
 * - Only one writer (0 or 1) can write when no readers are active
 * - Writers have priority over readers to prevent writer starvation
 * 
 * Uses explicit locks (ReentrantLock) instead of synchronized methods.
 * 
 * Shared resource between Reader threads (auditors) and Writer threads (supervisors).
 * 
 * @author Hospital Diagnostics System
 * @version 1.0
 */
public class PolicyRWMonitor {
    private int readers = 0;
    private boolean writerActive = false;
    private int writersWaiting = 0;
    private String currentPolicy = "NORMAL";
    
    // Explicit lock instead of implicit synchronized lock
    private final Lock lock = new ReentrantLock();
    
    // Condition variable for coordinating readers and writers
    private final Condition condition = lock.newCondition();

    /**
     * Reader begins reading - must wait if writer is active or writers are waiting.
     * 
     * @throws InterruptedException if the thread is interrupted while waiting
     */
    public void startRead() throws InterruptedException {
        lock.lock();
        try {
            while (writerActive || writersWaiting > 0) {
                // Wait if a writer is active or writers are waiting (writer priority)
                condition.await();
            }
            readers++;
        } finally {
            lock.unlock();
        }
    }

    /**
     * Reader finishes reading - notifies waiting writers if last reader.
     */
    public void endRead() {
        lock.lock();
        try {
            readers--;
            if (readers == 0) {
                // Last reader finished - notify waiting writers
                condition.signalAll();
            }
        } finally {
            lock.unlock();
        }
    }

    /**
     * Writer begins writing - must wait if any readers or another writer is active.
     * 
     * @throws InterruptedException if the thread is interrupted while waiting
     */
    public void startWrite() throws InterruptedException {
        lock.lock();
        try {
            writersWaiting++;
            while (writerActive || readers > 0) {
                // Wait if another writer is active or readers are reading
                condition.await();
            }
            writersWaiting--;
            writerActive = true;
        } finally {
            lock.unlock();
        }
    }

    /**
     * Writer finishes writing - notifies all waiting readers and writers.
     */
    public void endWrite() {
        lock.lock();
        try {
            writerActive = false;
            // Notify all waiting threads (readers and writers)
            condition.signalAll();
        } finally {
            lock.unlock();
        }
    }

    /**
     * Reads the current policy (actual read operation).
     * 
     * @return The current policy string
     */
    public String getPolicy() {
        return currentPolicy;
    }

    /**
     * Updates the policy (actual write operation).
     * 
     * @param policy The new policy value
     */
    public void setPolicy(String policy) {
        this.currentPolicy = policy;
    }
}
