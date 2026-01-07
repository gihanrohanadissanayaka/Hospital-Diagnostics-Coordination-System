# Part D: ReadWriteLock with Fairness Comparison

## Overview
Part D demonstrates the use of Java's `ReadWriteLock` interface with fairness parameter comparison. This implementation explores how fairness affects reader-writer synchronization in concurrent systems.

## Key Concepts

### ReadWriteLock
- **Interface**: `java.util.concurrent.locks.ReadWriteLock`
- **Implementation**: `ReentrantReadWriteLock`
- **Purpose**: Separate read and write locks for better concurrency
- **Feature**: Fairness parameter controls lock acquisition order

### Fairness Comparison
1. **Non-Fair Mode** (default):
   - No guarantees about lock acquisition order
   - Higher throughput, potential thread starvation
   - Better performance under low contention

2. **Fair Mode**:
   - FIFO lock acquisition order
   - Prevents starvation, ensures fairness
   - Slightly lower throughput due to ordering overhead

## Implementation Details

### PolicyRWMonitor
```java
public class PolicyRWMonitor {
    private String policy = "NORMAL";
    private final ReadWriteLock rwLock;
    private final Lock readLock;
    private final Lock writeLock;
    
    public PolicyRWMonitor(boolean fair) {
        // Fair parameter controls lock behavior
        this.rwLock = new ReentrantReadWriteLock(fair);
        this.readLock = rwLock.readLock();
        this.writeLock = rwLock.writeLock();
    }
}
```

### Key Features
- **Read Lock**: Multiple readers can access simultaneously
- **Write Lock**: Exclusive access for writers
- **Fairness**: Controlled via constructor parameter
- **No Starvation**: Fair mode ensures writers get access

## Workload Scenarios

### CALM Workload
- **Producers**: 2 (ClinicA, ClinicB)
- **Consumers**: 2 (Analyzer1, Analyzer2)
- **Readers**: 1 (Auditor1)
- **Writers**: 1 (Supervisor1)
- **Purpose**: Test basic fairness behavior

### SURGE Workload
- **Producers**: 5 (ER, ICU, WardA, WardB, Outpatient)
- **Consumers**: 2 (Analyzer1, Analyzer2)
- **Readers**: 3 (Auditor1, Auditor2, Auditor3)
- **Writers**: 1 (Supervisor1)
- **Purpose**: Test fairness under high reader contention

## Compilation and Execution

```powershell
# Navigate to source directory
cd PartD\src

# Compile all Java files
javac com/hospital/*.java

# Run the application
java com.hospital.Main
```

## Expected Behavior

### Non-Fair Mode
- Readers may dominate if frequent
- Writers may wait longer
- Higher overall throughput
- Possible reader/writer starvation

### Fair Mode
- Balanced reader/writer access
- FIFO ordering prevents starvation
- Slightly higher latency
- More predictable behavior

## Observations

### Performance Metrics
- **Throughput**: Non-fair typically processes more operations
- **Latency**: Fair mode has more consistent wait times
- **Fairness**: Fair mode ensures balanced access
- **Scalability**: Non-fair better under low contention

### Trade-offs
1. **Non-Fair**:
   - ✅ Higher throughput
   - ✅ Lower overhead
   - ❌ Potential starvation
   - ❌ Unpredictable ordering

2. **Fair**:
   - ✅ No starvation
   - ✅ Predictable behavior
   - ❌ Lower throughput
   - ❌ Higher overhead

## Comparison with Other Parts

| Part | Synchronization | Fairness | Complexity |
|------|----------------|----------|------------|
| A | synchronized | No control | Medium |
| B | BlockingQueue | Fair FIFO | Low |
| C | ReentrantLock | Configurable | Medium |
| **D** | **ReadWriteLock** | **Configurable** | **Medium** |

## Advanced Features

### ReadWriteLock Benefits
- **Concurrent Reads**: Multiple readers simultaneously
- **Exclusive Writes**: Writers get exclusive access
- **Lock Downgrading**: Possible with careful coding
- **Try-Lock Support**: Non-blocking lock attempts

### Fairness Impact
- **Reader Preference**: Non-fair may favor readers
- **Writer Access**: Fair ensures writers aren't starved
- **Queue Management**: Fair maintains internal queue
- **Context Switching**: Fair may increase switches

## Use Cases

### When to Use Non-Fair
- High read/low write workloads
- Performance is critical
- Starvation is unlikely
- Predictability not required

### When to Use Fair
- Balanced read/write workloads
- Fairness is critical
- SLA requirements exist
- Starvation prevention needed

## Learning Outcomes
1. Understanding ReadWriteLock semantics
2. Fairness vs. performance trade-offs
3. Reader-writer synchronization patterns
4. Concurrent lock behavior analysis
5. Real-world concurrency design choices
