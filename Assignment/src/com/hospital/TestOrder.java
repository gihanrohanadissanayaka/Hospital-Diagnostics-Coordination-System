package com.hospital;

/*
 * This is the object of this class will be put to bounded buffer by the producer
 * consumed by the consumer 
 * Represents a test request 
 */
public class TestOrder {
    private static int counter = 0; // one instance per class for the order id

    private final int orderId;
    private final String patientId;
    private final String testType; // better solution can be an ENUM for the test type
    private final int priority; // better solution can be an ENUM can be used 
    private final long createdAt; // to measure various metrics related to timing 
    // This one is to keep track of time at which order (Test Request) was created 

    public TestOrder(String patientId, String testType, int priority) {
        this.orderId = ++counter;
        this.patientId = patientId;
        this.testType = testType;
        this.priority = priority;
        this.createdAt = System.currentTimeMillis();
    }

    public int getOrderId() {
        return orderId;
    }

    public String getPatientId() {
        return patientId;
    }

    public String getTestType() {
        return testType;
    }

    public int getPriority() {
        return priority;
    }

    public long getCreatedAt() {
        return createdAt;
    }

    @Override
    public String toString() {
        return "Order-" + orderId + "[" + patientId + "," + testType + ",P" + priority + "]";
    }
}
