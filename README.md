# Complex Graph traversal

## Overview
A Java program that performs parallel level-order traversal (BFS) on a **directed** graph using multi-threading. Each BFS level is processed concurrently.

## Approach
- Graph represented using adjacency list.
- BFS starts from node 1.
- Each BFS level processed in parallel using a thread pool.
- Uses thread-safe data structures to avoid race conditions.

## Tools/Libraries
- `java.util.*`: Collections and Scanner.
- `java.util.concurrent.*`:
  - `ConcurrentLinkedQueue` for level queues.
  - `ConcurrentHashMap.newKeySet()` for visited tracking.
  - `ExecutorService` with `Callable` for concurrent processing.

## Assumptions
- Node keys are unique integers.
- Graph is **directed**.
- Input format strictly followed:
  - First: number of nodes `N`, then `N` lines of `<key>: <name>`
  - Then: number of edges `M`, then `M` lines of `<u>:<v>`

## Design Decisions
- Level-wise concurrency ensures simplicity and safety.
- Uses available CPU cores for thread pool.
- Separated `Graph` class for clarity and reuse.
