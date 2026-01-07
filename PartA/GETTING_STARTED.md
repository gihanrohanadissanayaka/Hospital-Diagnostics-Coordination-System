# Part A Implementation - Getting Started Guide

## ✅ What Has Been Created

I've successfully implemented **Part A** of your Hospital Diagnostics Coordination System assignment in a new `PartA` directory with the following structure:

```
PartA/
├── src/
│   └── com/
│       └── hospital/
│           ├── TestOrder.java
│           ├── BoundedQueueMonitor.java
│           ├── PolicyRWMonitor.java
│           ├── Producer.java
│           ├── Consumer.java
│           ├── Reader.java
│           ├── Writer.java
│           └── Main.java
└── README.md
```

## 📚 Implementation Details

### Part A: Monitor using synchronized (Implicit Locks)

This implementation uses **traditional Java monitors** with:
- `synchronized` keyword for mutual exclusion
- `wait()` for blocking threads
- `notifyAll()` for signaling waiting threads

### Two Design Patterns Implemented:

#### 1. Producer-Consumer Pattern
- **BoundedQueueMonitor**: Bounded buffer (capacity: 5)
- **Producer threads**: Create test orders (clinics)
- **Consumer threads**: Process test orders (analyzers)

#### 2. Reader-Writer Pattern
- **PolicyRWMonitor**: Manages system policy with reader-writer synchronization
- **Reader threads**: Read policy (auditors)
- **Writer threads**: Update policy (supervisors)
- **Writer Priority**: Prevents writer starvation

### Two Workload Scenarios:

#### Workload A: CALM (Light Load)
- 2 Producers (ClinicA, ClinicB)
- 2 Consumers (Analyzer1, Analyzer2)
- 1 Reader (Auditor1)
- 1 Writer (Supervisor1)
- Duration: 5 seconds

#### Workload B: SURGE (Heavy Load)
- 5 Producers (ER, ICU, WardA, WardB, Outpatient)
- 2 Consumers (Analyzer1, Analyzer2)
- 3 Readers (Auditor1, Auditor2, Auditor3)
- 1 Writer (Supervisor1)
- Duration: 5 seconds

## 🚀 How to Compile and Run

### Step 1: Navigate to the source directory
```bash
cd PartA/src
```

### Step 2: Compile all Java files
```bash
javac com/hospital/*.java
```

### Step 3: Run the main program
```bash
java com.hospital.Main
```

### Alternative: Using VS Code
1. Open the `PartA` folder in VS Code
2. Right-click on `Main.java` → Run Java
3. Or use the Run button in the editor

## 📊 Expected Output

You should see output similar to:
```
============================================================
PART A: Monitor using synchronized (Implicit Locks)
============================================================
=== Workload A: CALM ===

[ClinicA] Created: Order-1[ClinicA-P1,BloodTest,P2]
[ClinicA] Queued: Order-1[ClinicA-P1,BloodTest,P2]
[Analyzer1] Processing: Order-1[ClinicA-P1,BloodTest,P2] (waited 5ms)
[Auditor1] Read policy: NORMAL
[Supervisor1] Updated policy to: URGENT_PRIORITY
...
[ClinicA] Stopped
[Analyzer1] Stopped
[Auditor1] Stopped. Total reads: 25
[Supervisor1] Stopped. Total writes: 1

=== CALM Complete ===

============================================================

=== Workload B: SURGE ===
...
```

## 🔍 Key Observations to Make

### 1. Synchronization Behavior
- Observe producers blocking when queue is full
- Observe consumers blocking when queue is empty
- Notice readers running concurrently
- Notice writers blocking readers (and vice versa)

### 2. Performance Metrics
- **Wait Time**: Time from order creation to processing start
- **Queue Utilization**: How often the queue fills up
- **Read/Write Frequency**: Number of policy reads vs. writes
- **Thread Coordination**: How well threads coordinate

### 3. Thread States
- RUNNABLE: Active execution
- WAITING: Blocked on wait()
- TIMED_WAITING: In sleep()
- TERMINATED: Finished

## 📝 Code Highlights

### BoundedQueueMonitor - Producer Method
```java
public synchronized void put(TestOrder order) throws InterruptedException {
    while (queue.size() == capacity) {
        wait(); // Producer waits if queue is full
    }
    queue.add(order);
    notifyAll(); // Notify waiting consumers
}
```

### PolicyRWMonitor - Writer Priority
```java
public synchronized void startRead() throws InterruptedException {
    while (writerActive || writersWaiting > 0) {
        wait(); // Readers wait for writers (writer priority)
    }
    readers++;
}
```

## 🎯 Learning Objectives

After studying this implementation, you should understand:

1. **Monitor Pattern**: How synchronized methods provide mutual exclusion
2. **Condition Synchronization**: Using wait() and notifyAll()
3. **Producer-Consumer**: Bounded buffer implementation
4. **Reader-Writer**: Multiple readers, single writer pattern
5. **Thread Lifecycle**: States and transitions
6. **Deadlock Prevention**: Why notifyAll() is used instead of notify()
7. **Starvation Prevention**: Writer priority mechanism

## 🔬 Next Steps for Your Assignment

### 1. Run and Analyze Part A
- Execute the program multiple times
- Observe different execution patterns
- Document synchronization behavior
- Measure performance metrics

### 2. Compare with Assignment Sample
- Compare with your Assignment/src code
- Note any differences in approach
- Understand design choices

### 3. Prepare for Part B
- Part B will use `BlockingQueue` instead of custom monitor
- Understand the trade-offs

### 4. Documentation for Report
- Screenshot the output
- Explain synchronization mechanisms
- Discuss advantages/disadvantages of implicit locks
- Analyze performance under CALM vs. SURGE

## 📖 Additional Resources

### Key Synchronization Concepts:
- **Mutual Exclusion**: Only one thread in critical section
- **Condition Synchronization**: Wait for specific conditions
- **Liveness**: All threads make progress (no deadlock/starvation)
- **Fairness**: Equal opportunity for all threads

### Java Monitor Methods:
- `synchronized`: Acquires intrinsic lock
- `wait()`: Releases lock and waits
- `notify()`: Wakes one waiting thread
- `notifyAll()`: Wakes all waiting threads

## 🐛 Troubleshooting

### If Java is not recognized:
1. Check if Java JDK is installed: `java -version`
2. If not installed, download from Oracle or use OpenJDK
3. Add Java to your PATH environment variable
4. Or use VS Code's Java extension to run the code

### If compilation fails:
1. Ensure all files are in correct directory structure
2. Check for syntax errors
3. Verify Java version compatibility (Java 8+)

## 💡 Tips for Success

1. **Run Multiple Times**: Each run will produce different interleaving
2. **Vary Sleep Times**: Experiment with different workload patterns
3. **Add Logging**: Track queue size, read/write counts
4. **Measure Metrics**: Calculate throughput, latency, utilization
5. **Document Everything**: Take screenshots, notes, and observations

---

**Your Part A implementation is complete and ready to run!** 🎉

The code is well-documented with comments explaining the synchronization mechanisms. Once you compile and run it, you'll see the concurrent behavior of producers, consumers, readers, and writers in action.

Good luck with your MSc assignment! 🚀
