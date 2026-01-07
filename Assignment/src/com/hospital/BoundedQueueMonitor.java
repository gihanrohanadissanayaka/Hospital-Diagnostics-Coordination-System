package com.hospital;

import java.util.LinkedList;
/**
 * 
 * @author gugsi
 * 
 * First Monitor class for Producer / Consumer 
 * 
 * Object of this class will be the shared resource between producer and consumer 
 *
 */

public class BoundedQueueMonitor {
	// shared variable - Queue of TestOrder
    private final LinkedList<TestOrder> queue = new LinkedList<>(); // Bounded Buffer
    private final int capacity; // bounded buffer will always has the maximum capacity 
    // capacity variable stores the maximum value 

    public BoundedQueueMonitor(int capacity) {
        this.capacity = capacity;
    }

    // producer method - put() is the method called by the producer to add an test order
    // if the queue is full (reached the maximum capacity) - producer has to wait
    // implicit Lock - synchronized
    public synchronized void put(TestOrder order) throws InterruptedException {
        while (queue.size() == capacity) { // if this queue.size() == capacity is TRUE then queue is full 
            wait(); // if the queue producer threads goes into WAITING state until at least one slot become empty
        }
        queue.add(order);
        notifyAll(); // since an test order has been added to the Queue informs the Consumers (All the threads in the WAITING state)
    }

    // Consumer method 
    // Consumer calls the take() method to consume a test order and process it 
    // implicit Lock - synchronized
    // 
    public synchronized TestOrder take() throws InterruptedException {
        while (queue.isEmpty()) { // if queue.isEmpty() is TRUE meaning is nothing in the queue then consumer cannot process anything so consumers goes into WAITING state 
            wait(); // if queue is empty Consumes goes into WAITING state 
        }
        TestOrder order = queue.removeFirst(); // removes (FIFO queue) first item from the queue  
        notifyAll(); // since at least one space become available signals all the producers in the WAITING state to wake up
        return order;
    }

    public synchronized int size() {
        return queue.size();
    }
}
