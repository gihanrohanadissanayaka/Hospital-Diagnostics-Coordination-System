package com.hospital;

import java.util.concurrent.atomic.AtomicInteger;

public class TestOrder {
    private static final AtomicInteger counter = new AtomicInteger(0);
    
    private final int id;
    private final String clinic;
    private final String testType;
    private final String patientId;
    private final long createdTime;
    private long queuedTime;
    
    public TestOrder(String clinic, String testType, String patientId) {
        this.id = counter.incrementAndGet();
        this.clinic = clinic;
        this.testType = testType;
        this.patientId = patientId;
        this.createdTime = System.currentTimeMillis();
    }
    
    public int getId() { return id; }
    public String getClinic() { return clinic; }
    public String getTestType() { return testType; }
    public String getPatientId() { return patientId; }
    public long getCreatedTime() { return createdTime; }
    
    public void setQueuedTime(long time) { this.queuedTime = time; }
    public long getQueuedTime() { return queuedTime; }
    
    @Override
    public String toString() {
        return String.format("Order-%d[%s,%s,%s]", id, clinic, testType, patientId);
    }
}
