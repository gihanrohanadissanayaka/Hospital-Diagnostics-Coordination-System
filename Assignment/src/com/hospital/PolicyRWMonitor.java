package com.hospital;

/**
 * 
 * @author gugsi
 * 
 * No of reader can be any number between 0 to N - as long as number of write is ZERO
 * No of Writer either 0 or maximum of 1 - as long as number of reader is 0
 * There can be multiple reader reading simultaneously as long no writer  
 * However no of writer maximum if number of reader is 0
 * if the number writer is 1 then reader must be 0
 * 
 * Second Monitor class for Reader / Writer 
 * Object of this class will be shared between Reader / Writer 
 */

public class PolicyRWMonitor {
    private int readers = 0;
    private boolean writerActive = false;
    private int writersWaiting = 0;
    // shared variable 
    private String currentPolicy = "NORMAL";

    public synchronized void startRead() throws InterruptedException {
        while (writerActive || writersWaiting > 0) {
            wait(); // reader goes into WAITING state if there is an active Writer or number of writers waiting to write is 1 or above 
        }
        readers++;
    }

    public synchronized void endRead() {
        readers--;
        if (readers == 0)
            notifyAll();
    }

    public synchronized void startWrite() throws InterruptedException {
        writersWaiting++;
        while (writerActive || readers > 0) { // only one writer and number of reader has to be ZERO
            wait();// otherwise Writer has to go into WAITING state 
        }
        writersWaiting--; // reducing the number of writer in waiting state by 1 
        writerActive = true; // the active writer to TRUE
    }

    public synchronized void endWrite() {
        writerActive = false;
        notifyAll();
    }

    // actual reader method
    public String getPolicy() {
        return currentPolicy;
    }

    // actual writer method
    public void setPolicy(String policy) {
        this.currentPolicy = policy;
    }
}
