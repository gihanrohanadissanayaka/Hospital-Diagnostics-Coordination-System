# Part A: Monitor using synchronized (Implicit Locks)

## Overview
Part A implements the Hospital Diagnostics Coordination System using **traditional Java monitors** with the `synchronized` keyword, `wait()`, and `notifyAll()` methods.

## Architecture

### Producer-Consumer Pattern
- **Shared Resource**: `BoundedQueueMonitor` - A bounded queue for test orders
- **Producers**: `Producer` threads (Clinics) - Create and queue test orders
- **Consumers**: `Consumer` threads (Analyzers) - Process test orders from the queue

### Reader-Writer Pattern
- **Shared Resource**: `PolicyRWMonitor` - Manages system policy
- **Readers**: `Reader` threads (Auditors) - Read current policy
- **Writers**: `Writer` threads (Supervisors) - Update policy

## Key Classes

### 1. TestOrder.java
- Represents a diagnostic test order
- Contains patient ID, test type, priority, and timestamp
- Used as the shared data object in the bounded queue

### 2. BoundedQueueMonitor.java
- Implements bounded buffer using `synchronized` methods
- **put()**: Adds test order (blocks if full)
- **take()**: Removes test order (blocks if empty)
- Uses `wait()` and `notifyAll()` for synchronization

### 3. PolicyRWMonitor.java
- Implements reader-writer synchronization
- **startRead()/endRead()**: Reader lock/unlock
- **startWrite()/endWrite()**: Writer lock/unlock
- Writers have priority to prevent writer starvation

### 4. Producer.java
- Creates test orders at specified intervals
- Simulates clinic workload

### 5. Consumer.java
- Processes test orders from the queue
- Tracks waiting time for performance metrics

### 6. Reader.java
- Reads system policy
- Tracks read count for metrics

### 7. Writer.java
- Updates system policy
- Cycles through NORMAL, URGENT_PRIORITY, MAINTENANCE

### 8. Main.java
- Orchestrates two workload scenarios:
  - **CALM**: 2 producers, 2 consumers, 1 reader, 1 writer
  - **SURGE**: 5 producers, 2 consumers, 3 readers, 1 writer

## Synchronization Mechanism

### Implicit Locking (synchronized)
```java
public synchronized void put(TestOrder order) throws InterruptedException {
    while (queue.size() == capacity) {
        wait(); // Release lock and wait
    }
    queue.add(order);
    notifyAll(); // Wake up all waiting threads
}
```

### Advantages
- Simple and straightforward
- Built into Java language
- Automatic lock acquisition and release

### Disadvantages
- Less flexible than explicit locks
- Cannot interrupt lock acquisition
- No try-lock mechanism
- Single condition variable per object

## Running the Program

### Compile
```bash
cd PartA/src
javac com/hospital/*.java
```

### Run
```bash
java com.hospital.Main
```

## Expected Output
- Producer logs showing test order creation and queuing
- Consumer logs showing order processing and wait times
- Reader logs showing policy reads
- Writer logs showing policy updates
- Thread stop confirmations

## Performance Metrics to Observe
1. **Wait Time**: Time between order creation and processing start
2. **Queue Utilization**: How often the queue reaches capacity
3. **Reader/Writer Concurrency**: Number of concurrent readers
4. **Thread Synchronization**: Observe blocking and wake-up patterns

## Thread States
- **RUNNABLE**: Thread executing or ready to execute
- **WAITING**: Thread waiting on wait() call
- **TIMED_WAITING**: Thread in sleep() call
- **TERMINATED**: Thread finished execution

## Critical Sections
1. **BoundedQueueMonitor**: Entire put() and take() methods
2. **PolicyRWMonitor**: All startRead(), endRead(), startWrite(), endWrite() methods

## Potential Issues to Analyze
1. **Starvation**: Can readers or writers starve?
2. **Deadlock**: Are there circular wait conditions?
3. **Liveness**: Do all threads make progress?
4. **Fairness**: Do threads get equal opportunity?

---

**Next Steps**: Compare this implementation with Part B (BlockingQueue), Part C (ReentrantLock), and Part D (ReadWriteLock) to understand trade-offs.
