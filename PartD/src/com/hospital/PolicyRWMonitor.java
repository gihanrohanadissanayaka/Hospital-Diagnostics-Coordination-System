package com.hospital;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class PolicyRWMonitor {
    private String policy = "NORMAL";
    private final ReadWriteLock rwLock;
    private final Lock readLock;
    private final Lock writeLock;
    
    public PolicyRWMonitor(boolean fair) {
        this.rwLock = new ReentrantReadWriteLock(fair);
        this.readLock = rwLock.readLock();
        this.writeLock = rwLock.writeLock();
    }
    
    public String readPolicy() {
        readLock.lock();
        try {
            return policy;
        } finally {
            readLock.unlock();
        }
    }
    
    public void writePolicy(String newPolicy) {
        writeLock.lock();
        try {
            policy = newPolicy;
        } finally {
            writeLock.unlock();
        }
    }
}
