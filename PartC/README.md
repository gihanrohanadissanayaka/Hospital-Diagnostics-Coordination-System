# Part C: ReentrantLock + Condition

## Overview
Part C implements the Hospital Diagnostics Coordination System using **ReentrantLock and Condition variables** instead of `synchronized` methods (Part A) or `BlockingQueue` (Part B).

## Architecture

### Producer-Consumer Pattern
- **Shared Resource**: `BoundedQueueMonitor` using `ReentrantLock` and `Condition`
- **Producers**: `Producer` threads (Clinics) - Create and queue test orders
- **Consumers**: `Consumer` threads (Analyzers) - Process test orders from the queue

### Reader-Writer Pattern
- **Shared Resource**: `PolicyRWMonitor` using `ReentrantLock` and `Condition`
- **Readers**: `Reader` threads (Auditors) - Read current policy
- **Writers**: `Writer` threads (Supervisors) - Update policy

## Key Differences from Part A and Part B

### Part A (synchronized + wait/notify)
```java
public synchronized void put(TestOrder order) throws InterruptedException {
    while (queue.size() == capacity) {
        wait(); // Implicit lock, single wait set
    }
    queue.add(order);
    notifyAll(); // Wake all waiting threads
}
```

### Part B (BlockingQueue)
```java
queue.put(order); // All synchronization hidden
```

### Part C (ReentrantLock + Condition)
```java
public void put(TestOrder order) throws InterruptedException {
    lock.lock(); // Explicit lock acquisition
    try {
        while (queue.size() == capacity) {
            notFull.await(); // Specific condition variable
        }
        queue.add(order);
        notEmpty.signal(); // Signal specific condition
    } finally {
        lock.unlock(); // Must unlock in finally
    }
}
```

## ReentrantLock API

### Lock Methods
- `lock.lock()` - Acquires the lock (blocks if unavailable)
- `lock.unlock()` - Releases the lock (must be in finally block)
- `lock.tryLock()` - Attempts to acquire lock without blocking
- `lock.tryLock(timeout, unit)` - Attempts with timeout
- `lock.lockInterruptibly()` - Acquires lock unless interrupted

### Condition Variables
- `Condition notFull = lock.newCondition()` - Create condition
- `notFull.await()` - Release lock and wait (like `wait()`)
- `notFull.signal()` - Wake one waiting thread (like `notify()`)
- `notFull.signalAll()` - Wake all waiting threads (like `notifyAll()`)

## Advantages of ReentrantLock + Condition

### 1. **Multiple Condition Variables**
```java
private final Condition notFull = lock.newCondition();  // For producers
private final Condition notEmpty = lock.newCondition(); // For consumers
```
- Part A: Single wait set per object
- Part C: Separate wait sets for different conditions
- **Benefit**: More efficient signaling, less spurious wake-ups

### 2. **Explicit Lock Management**
```java
lock.lock();
try {
    // Critical section
} finally {
    lock.unlock(); // Always unlocks
}
```
- Must explicitly unlock (even with exceptions)
- More control over lock scope
- **Benefit**: Can't forget to unlock if using try-finally

### 3. **Fairness Policy**
```java
private final Lock lock = new ReentrantLock(true); // Fair lock
```
- Can choose fair or non-fair locking
- Fair: FIFO order for waiting threads
- Non-fair: Better throughput but possible starvation
- **Benefit**: Configurable fairness vs. performance trade-off

### 4. **Try-Lock Support**
```java
if (lock.tryLock()) {
    try {
        // Got the lock
    } finally {
        lock.unlock();
    }
} else {
    // Couldn't get lock, do something else
}
```
- Non-blocking lock acquisition
- Timeout support
- **Benefit**: Avoid deadlocks, implement timeouts

### 5. **Interruptible Locking**
```java
lock.lockInterruptibly(); // Can be interrupted while waiting
```
- Thread can be interrupted while waiting for lock
- Part A: Can't interrupt `synchronized` acquisition
- **Benefit**: Better responsiveness to interrupts

### 6. **Lock Condition Queries**
```java
lock.isHeldByCurrentThread(); // Check if current thread holds lock
lock.getHoldCount();          // Number of holds by current thread
lock.hasQueuedThreads();      // Check if threads are waiting
```
- Query lock state
- **Benefit**: Better debugging and monitoring

## Key Classes

### 1. BoundedQueueMonitor.java
**Key Changes from Part A:**
```java
private final Lock lock = new ReentrantLock();
private final Condition notFull = lock.newCondition();
private final Condition notEmpty = lock.newCondition();

// Instead of:
// public synchronized void put(...)
public void put(TestOrder order) throws InterruptedException {
    lock.lock();
    try {
        while (queue.size() == capacity) {
            notFull.await(); // Instead of wait()
        }
        queue.add(order);
        notEmpty.signal(); // Instead of notifyAll()
    } finally {
        lock.unlock();
    }
}
```

**Benefits:**
- Separate conditions for "not full" and "not empty"
- More efficient signaling (signal vs. signalAll)
- Producers only wake consumers, not other producers

### 2. PolicyRWMonitor.java
**Uses ReentrantLock instead of synchronized:**
```java
private final Lock lock = new ReentrantLock();
private final Condition condition = lock.newCondition();

public void startRead() throws InterruptedException {
    lock.lock();
    try {
        while (writerActive || writersWaiting > 0) {
            condition.await();
        }
        readers++;
    } finally {
        lock.unlock();
    }
}
```

### 3. Producer.java, Consumer.java, Reader.java, Writer.java
- **No changes** to these classes
- They use the monitor interfaces the same way
- Demonstrates that implementation details are hidden

## Performance Considerations

### Signal vs. SignalAll
**Part A (notifyAll):**
- Wakes ALL waiting threads
- Many threads compete for lock
- More context switches

**Part C (signal):**
- Wakes ONE waiting thread
- Less contention
- Fewer context switches
- **Trade-off**: Must ensure correct thread is woken

### Lock Fairness
**Non-fair (default):**
- Better throughput
- Possible thread starvation
- Barging allowed

**Fair:**
- FIFO ordering
- No starvation
- Lower throughput
- **Trade-off**: Fairness vs. performance

## Common Pitfalls

### 1. Forgetting to Unlock
```java
// WRONG - lock never released if exception occurs
lock.lock();
// ... critical section ...
lock.unlock();

// CORRECT - always use try-finally
lock.lock();
try {
    // ... critical section ...
} finally {
    lock.unlock();
}
```

### 2. Wrong Condition Variable
```java
// WRONG - signaling wrong condition
notFull.await(); // Waiting for not full
notFull.signal(); // Should use notEmpty.signal()

// CORRECT - match condition to situation
notFull.await();  // Producer waits for not full
notEmpty.signal(); // Producer signals not empty
```

### 3. Nested Locks (Deadlock Risk)
```java
// WRONG - nested locks can deadlock
lock1.lock();
lock2.lock(); // If another thread has lock2 and wants lock1...
// ... deadlock!

// CORRECT - use tryLock with timeout
if (lock1.tryLock()) {
    try {
        if (lock2.tryLock()) {
            try {
                // ... safe ...
            } finally { lock2.unlock(); }
        }
    } finally { lock1.unlock(); }
}
```

## Running the Program

### Compile
```bash
cd PartC/src
javac com/hospital/*.java
```

### Run
```bash
java com.hospital.Main
```

## Expected Behavior

### Same Workloads as Part A and B
1. **CALM**: 2 producers, 2 consumers, 1 reader, 1 writer
2. **SURGE**: 5 producers, 2 consumers, 3 readers, 1 writer

### Expected Output
- Similar output to Part A and B
- Same blocking behavior
- Potentially more efficient signaling

## Code Comparison

| Aspect | Part A (synchronized) | Part B (BlockingQueue) | Part C (ReentrantLock) |
|--------|----------------------|------------------------|------------------------|
| Lock Type | Implicit | Hidden | Explicit |
| Condition Variables | 1 per object | N/A | Multiple possible |
| Flexibility | Low | N/A | High |
| Fairness Control | No | Yes (constructor) | Yes (constructor) |
| Try-Lock | No | Yes (offer/poll) | Yes |
| Code Complexity | Medium | Low | Medium-High |
| Performance | Good | Good | Good-Excellent |

## When to Use ReentrantLock

### ✅ Use ReentrantLock When:
- Need multiple condition variables
- Require try-lock functionality
- Want fairness guarantees
- Need lock interruptibility
- Require advanced lock features

### ❌ Use synchronized When:
- Simple mutual exclusion
- Code simplicity is priority
- Single condition variable sufficient
- No special lock features needed

### ❌ Use BlockingQueue When:
- Standard Producer-Consumer only
- Want simplest implementation
- Don't need custom synchronization logic

## Learning Objectives

After studying Part C, you should understand:

1. **Explicit Locking**: ReentrantLock API and usage patterns
2. **Condition Variables**: Multiple conditions for different wait sets
3. **Lock Management**: Always unlock in finally blocks
4. **Fairness Policies**: Trade-offs between fairness and throughput
5. **Advanced Features**: try-lock, timed locks, interruptible locks
6. **Code Structure**: Proper exception handling with locks
7. **Performance**: Signal vs. signalAll optimization

## Comparison Summary

**Part A → Part C Migration:**
- `synchronized` → `lock.lock()` + `try-finally`
- `wait()` → `condition.await()`
- `notify()` → `condition.signal()`
- `notifyAll()` → `condition.signalAll()`
- Single wait set → Multiple conditions

**Advantages Gained:**
- Multiple condition variables
- Try-lock and timed locks
- Lock interruptibility
- Fairness policies
- Better monitoring/debugging

**Trade-offs:**
- More verbose code
- Must remember to unlock
- More complex error handling
- Steeper learning curve

---

**Next Steps**: Compare all three implementations (Part A, B, C), then proceed to Part D (ReadWriteLock) to see specialized reader-writer locking.
