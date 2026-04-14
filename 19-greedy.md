# Topic 19: Greedy Algorithms

*Document 19 of 20 in the FAANG DSA Prep series.*

Make the locally optimal choice at each step, hoping it leads to global optimum. Requires proof: greedy choice property + optimal substructure.

**Top askers:** Google, Amazon, Meta, Microsoft, Bloomberg, Apple

---

## Greedy vs DP

| Greedy | DP |
|---|---|
| One choice per step, never revisit | Considers all choices |
| Needs proof of correctness | Guaranteed by recurrence |
| O(n log n) often | O(n²) or O(n·k) often |
| No overlapping subproblems needed | Overlapping subproblems required |

---

## Templates

### Template 1: Sort + Iterate
```java
// Sort by criterion, then greedy choice
Arrays.sort(items, (a, b) -> a.end - b.end);  // e.g., activity selection
int count = 0;
int lastEnd = -1;
for (Item item : items) {
    if (item.start >= lastEnd) { count++; lastEnd = item.end; }
}
```

### Template 2: Two-Pass
```java
// Satisfy constraints from both directions
int[] result = new int[n];
Arrays.fill(result, 1);
for (int i = 1; i < n; i++)          // left to right
    if (ratings[i] > ratings[i-1]) result[i] = result[i-1] + 1;
for (int i = n-2; i >= 0; i--)        // right to left
    if (ratings[i] > ratings[i+1]) result[i] = Math.max(result[i], result[i+1] + 1);
```

### Template 3: Heap-Based
```java
// When "best" changes dynamically
PriorityQueue<Item> pq = new PriorityQueue<>((a,b) -> b.priority - a.priority);
while (!pq.isEmpty()) {
    Item item = pq.poll();
    // process best item
    for (Item next : item.related) pq.offer(next);
}
```

---

## Problem Solutions

### LC 55: Jump Game (Medium)
```java
// Greedy: farthest reachable at each step
public boolean canJump(int[] nums) {
    int farthest = 0;
    for (int i = 0; i < nums.length; i++) {
        if (i > farthest) return false;
        farthest = Math.max(farthest, i + nums[i]);
    }
    return true;
}
```

### LC 45: Jump Game II (Hard)
```java
// Greedy: jump from position with max reach
public int jump(int[] nums) {
    int jumps = 0, farthest = 0, end = 0;
    for (int i = 0; i < nums.length - 1; i++) {
        farthest = Math.max(farthest, i + nums[i]);
        if (i == end) { jumps++; end = farthest; }
    }
    return jumps;
}
```

### LC 452: Minimum Number of Arrows (Medium)
```java
// Sort by end, greedily pop balloons
public int findMinArrowShots(int[][] points) {
    Arrays.sort(points, (a, b) -> a[1] - b[1]);
    int arrows = 1, lastEnd = points[0][1];
    for (int i = 1; i < points.length; i++)
        if (points[i][0] > lastEnd) { arrows++; lastEnd = points[i][1]; }
    return arrows;
}
```

### LC 435: Non-overlapping Intervals (Medium)
```java
// Sort by end, greedy removal
public int eraseOverlapIntervals(int[][] intervals) {
    Arrays.sort(intervals, (a, b) -> a[1] - b[1]);
    int count = 0, lastEnd = intervals[0][1];
    for (int i = 1; i < intervals.length; i++) {
        if (intervals[i][0] < lastEnd) count++;
        else lastEnd = intervals[i][1];
    }
    return count;
}
```

### LC 134: Gas Station (Medium)
```java
// If total gas < total cost, impossible. Else, start at first where surplus >= 0.
public int canCompleteCircuit(int[] gas, int[] cost) {
    int total = 0, tank = 0, start = 0;
    for (int i = 0; i < gas.length; i++) {
        total += gas[i] - cost[i];
        tank += gas[i] - cost[i];
        if (tank < 0) { start = i + 1; tank = 0; }
    }
    return total >= 0 ? start : -1;
}
```

### LC 455: Assign Cookies (Easy)
```java
// Sort both, greedily match smallest
public int findContentChildren(int[] g, int[] s) {
    Arrays.sort(g); Arrays.sort(s);
    int i = 0, j = 0, count = 0;
    while (i < g.length && j < s.length) {
        if (s[j] >= g[i]) { count++; i++; j++; }
        else j++;
    }
    return count;
}
```

### LC 665: Non-decreasing Array (Medium)
```java
// At most one fix allowed
public boolean checkPossibility(int[] nums) {
    int count = 0;
    for (int i = 1; i < nums.length && count <= 1; i++) {
        if (nums[i] < nums[i-1]) {
            count++;
            if (i < 2 || nums[i-2] <= nums[i]) nums[i-1] = nums[i];
            else nums[i] = nums[i-1];
        }
    }
    return count <= 1;
}
```

### LC 621: Task Scheduler (Medium)
```java
// Greedy with max-heap
public int leastInterval(char[] tasks, int n) {
    int[] count = new int[26];
    for (char t : tasks) count[t - 'A']++;
    PriorityQueue<Integer> pq = new PriorityQueue<>(Collections.reverseOrder());
    for (int c : count) if (c > 0) pq.offer(c);
    int time = 0;
    while (!pq.isEmpty()) {
        List<Integer> temp = new ArrayList<>();
        for (int i = 0; i <= n; i++) {
            if (!pq.isEmpty()) {
                int f = pq.poll();
                if (f > 1) temp.add(f - 1);
                time++;
            } else if (!temp.isEmpty()) {
                time++;
            }
            if (pq.isEmpty() && temp.isEmpty()) break;
        }
        pq.addAll(temp);
    }
    return time;
}
```

### LC 406: Queue Reconstruction by Height (Medium)
```java
// Sort by height desc, k asc, then insert by k
public int[][] reconstructQueue(int[][] people) {
    Arrays.sort(people, (a, b) -> a[0] != b[0] ? b[0] - a[0] : a[1] - b[1]);
    List<int[]> result = new ArrayList<>();
    for (int[] p : people) result.add(p[1], p);
    return result.toArray(new int[people.length][]);
}
```

### LC 605: Can Place Flowers (Easy)
```java
public boolean canPlaceFlowers(int[] flowerbed, int n) {
    for (int i = 0; i < flowerbed.length && n > 0; i++) {
        if (flowerbed[i] == 0 &&
            (i == 0 || flowerbed[i-1] == 0) &&
            (i == flowerbed.length - 1 || flowerbed[i+1] == 0)) {
            flowerbed[i] = 1;
            n--;
        }
    }
    return n == 0;
}
```

### LC 53: Maximum Subarray (Easy)
```java
// Kadane's algorithm
public int maxSubArray(int[] nums) {
    int max = nums[0], cur = nums[0];
    for (int i = 1; i < nums.length; i++) {
        cur = Math.max(nums[i], cur + nums[i]);
        max = Math.max(max, cur);
    }
    return max;
}
```

---

## Practice Roadmap

1. **Foundation:** LC 55, LC 455, LC 605
2. **Intermediate:** LC 45, LC 134, LC 665
3. **Advanced:** LC 435, LC 406, LC 621, LC 53
