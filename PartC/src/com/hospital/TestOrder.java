package com.hospital;

/**
 * Represents a diagnostic test order in the hospital system.
 * This object is shared between producers (clinics) and consumers (analyzers).
 * 
 * @author Hospital Diagnostics System
 * @version 1.0
 */
public class TestOrder {
    private static int counter = 0; // Shared counter for unique order IDs

    private final int orderId;
    private final String patientId;
    private final String testType;
    private final int priority;
    private final long createdAt; // Timestamp for metrics calculation

    /**
     * Creates a new test order.
     * 
     * @param patientId The patient identifier
     * @param testType The type of diagnostic test
     * @param priority Priority level (1-3, where 1 is highest)
     */
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
