package com.hospital;

/**
 * Monitor class implementing the Reader-Writer pattern using synchronized methods.
 * 
 * Rules:
 * - Multiple readers (0 to N) can read simultaneously when no writer is active
 * - Only one writer (0 or 1) can write when no readers are active
 * - Writers have priority over readers to prevent writer starvation
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

    /**
     * Reader begins reading - must wait if writer is active or writers are waiting.
     * 
     * @throws InterruptedException if the thread is interrupted while waiting
     */
    public synchronized void startRead() throws InterruptedException {
        while (writerActive || writersWaiting > 0) {
            // Wait if a writer is active or writers are waiting (writer priority)
            wait();
        }
        readers++;
    }

    /**
     * Reader finishes reading - notifies waiting writers if last reader.
     */
    public synchronized void endRead() {
        readers--;
        if (readers == 0) {
            // Last reader finished - notify waiting writers
            notifyAll();
        }
    }

    /**
     * Writer begins writing - must wait if any readers or another writer is active.
     * 
     * @throws InterruptedException if the thread is interrupted while waiting
     */
    public synchronized void startWrite() throws InterruptedException {
        writersWaiting++;
        while (writerActive || readers > 0) {
            // Wait if another writer is active or readers are reading
            wait();
        }
        writersWaiting--;
        writerActive = true;
    }

    /**
     * Writer finishes writing - notifies all waiting readers and writers.
     */
    public synchronized void endWrite() {
        writerActive = false;
        // Notify all waiting threads (readers and writers)
        notifyAll();
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
