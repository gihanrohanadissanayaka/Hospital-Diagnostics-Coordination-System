package com.hospital.partd;

import java.util.concurrent.locks.ReentrantReadWriteLock;

public class PolicyRWMonitorD {
    private final ReentrantReadWriteLock rwLock;
    private final ReentrantReadWriteLock.ReadLock readLock;
    private final ReentrantReadWriteLock.WriteLock writeLock;
    private String currentPolicy = "NORMAL";

    public PolicyRWMonitorD(boolean fair) {
        this.rwLock = new ReentrantReadWriteLock(fair);
        this.readLock = rwLock.readLock();
        this.writeLock = rwLock.writeLock();
    }

    public void startRead() {
        readLock.lock();
    }

    public void endRead() {
        readLock.unlock();
    }

    public void startWrite() {
        writeLock.lock();
    }

    public void endWrite() {
        writeLock.unlock();
    }

    // read operation 
    public String getPolicy() {
    	
        return currentPolicy;
        
    }

    
    public void setPolicy(String policy) {
        this.currentPolicy = policy;
    }
}
