# DSA Prep Playground

**20 topics | 300+ LeetCode problems | Java solutions | Interactive testing**

An interactive Java playground for practicing Data Structures and Algorithms. Each topic includes working code implementations with comprehensive test cases.

> This is the **hands-on practice companion** to [dsa-prep](https://github.com/abhichauhan1994/dsa-prep), which contains the theory, templates, and problem-solving guides.

## Quick Start

```bash
# Clone the repository
git clone https://github.com/abhichauhan1994/dsa-prep-playground.git
cd dsa-prep-playground

# Build the project
./gradlew build

# Run all tests (Main class contains test cases)
java -cp build/classes/java/main org.play.Main

# Or run specific tests
./gradlew test
```

## Prerequisites

- **Java 17+** — [Install JDK](https://adoptium.net/)
- **Gradle** — Included via wrapper (`gradlew`), no separate installation needed

Verify your Java version:
```bash
java -version  # Should show 17 or higher
```

## Topics

| # | Topic | File | Key Patterns |
|---|-------|------|-------------|
| 1 | Sliding Window | `FixedSlidingWindow.java` | Fixed window, variable window |
| 2 | Two Pointers | `TwoPointers.java` | Opposite direction, merge |
| 3 | Fast & Slow Pointers | `FastSlowPointers.java` | Cycle detection, midpoint |
| 4 | Binary Search | `BinarySearch.java` | Classic, rotated array |
| 5 | Prefix Sum | `PrefixSum.java` | Range sum, difference array |
| 6 | HashMap & Frequency | `HashMapProblems.java` | Two-sum, grouping |
| 7 | Monotonic Stack | `MonotonicStack.java` | Next greater/smaller |
| 8 | Stacks & Queues | `StacksQueues.java` | Parentheses, expression eval |
| 9 | Linked Lists | `LinkedListProblems.java` | Reversal, merge, k-group |
| 10 | Trees DFS | `TreesDFS.java` | Traversals, path sum, BST |
| 11 | Trees BFS | `TreesBFS.java` | Level-order, right view |
| 12 | Heaps | `HeapProblems.java` | Top-K, k-way merge |
| 13 | Backtracking | `Backtracking.java` | Subsets, permutations |
| 14 | Graph Traversal | `GraphTraversal.java` | BFS, DFS, components |
| 15 | Topological Sort | `TopologicalSort.java` | Kahn's, DFS-based |
| 16 | Shortest Path | `ShortestPath.java` | Dijkstra, Bellman-Ford |
| 17 | Union-Find | `UnionFind.java` | Path compression, MST |
| 18 | Intervals | `IntervalProblems.java` | Merge, scheduling |
| 19 | Greedy | `GreedyProblems.java` | Activity selection, scheduling |
| 20 | Dynamic Programming | `DynamicProgramming.java` | 1D, 2D, knapsack |

## Project Structure

```
dsa-prep-playground/
├── src/main/java/org/play/
│   ├── Main.java                 # Entry point, runs all tests
│   └── slidingWindow/
│       ├── FixedSlidingWindow.java    # Fixed window implementations
│       └── VariableSlidingWindow.java  # Variable window implementations
├── src/test/java/               # JUnit test cases (future)
├── 01-sliding-window.md         # Topic reference
├── 02-two-pointers.md
├── ...
├── 20-dynamic-programming.md
├── build.gradle                 # Gradle configuration
└── gradlew                      # Gradle wrapper
```

## How to Practice

### 1. Read the Theory
Start with the `.md` files in this repo or the parent [dsa-prep](https://github.com/abhichauhan1994/dsa-prep) for comprehensive explanations, templates, and problem-solving strategies.

### 2. Understand the Implementation
Each class contains:
- Problem solutions with detailed comments
- Test cases covering edge cases and LeetCode examples
- Links to original LeetCode problems

### 3. Run the Tests
```bash
# Build and run Main.java
./gradlew build && java -cp build/classes/java/main org.play.Main

# Expected output:
# === testMaxSumOfSubarrayOfSizeK ===
# Test 1 - Basic mixed array: arr=[1, 12, -5, -6, 50, 3], k=4
#   Expected: 51, Got: 51, PASS
# ...
```

### 4. Add Your Own
Add test cases in the `run*Tests()` methods or create new problem classes:

```java
// In Main.java or new class
public static void myNewTest() {
    int[] arr = {1, 2, 3, 4, 5};
    int k = 2;
    int result = mySolution(arr, k);
    System.out.println("Expected: X, Got: " + result + 
        ", " + (result == X ? "PASS" : "FAIL"));
}
```

## Running Tests

```bash
# All tests via Main class
java -cp build/classes/java/main org.play.Main

# JUnit tests (when test directory is set up)
./gradlew test

# Clean and rebuild
./gradlew clean build
```

## Common Gradle Commands

| Command | Description |
|---------|-------------|
| `./gradlew build` | Compile and package |
| `./gradlew clean` | Clean build directory |
| `./gradlew test` | Run unit tests |
| `./gradlew run` | Run application (requires `application` plugin) |

## Stats

- **Topics**: 20 comprehensive DSA patterns
- **Problems**: ~300 LeetCode problems with Java solutions
- **Templates**: Reusable code patterns
- **Test Cases**: Comprehensive coverage including edge cases
- **Company Tags**: Google, Amazon, Meta, Microsoft, Bloomberg, Apple, Goldman Sachs

## Related

- **[dsa-prep](https://github.com/abhichauhan1994/dsa-prep)** — Complete theory, templates, and study roadmap
- **[LeetCode](https://leetcode.com)** — Practice platform with 3000+ problems

## Contributing

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/new-problem`)
3. Add your implementation and test cases
4. Commit your changes (`git commit -m 'Add solution for problem X'`)
5. Push to the branch (`git push origin feature/new-problem`)
6. Open a Pull Request

## License

MIT License — See [LICENSE](LICENSE) for details.
