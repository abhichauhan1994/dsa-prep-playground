# Topic 12: Heaps & Priority Queues

*Document 12 of 20 in the FAANG DSA Prep series.*

O(log n) insert/remove, O(1) peek. Java's `PriorityQueue` is a **min-heap by default**.

**Top askers:** Google, Amazon, Meta, Microsoft, Bloomberg

---

## Key Facts

- Default `PriorityQueue` = min-heap (poll() returns smallest)
- Max-heap: `new PriorityQueue<>(Collections.reverseOrder())` or `(a,b) -> Integer.compare(b, a)`
- **AVOID** `(a,b) -> b - a` — overflow risk with large integers
- `contains()` and `remove(element)` are O(n), not O(log n)

---

## The Four Patterns

### Pattern 1: Top-K Elements
- Use min-heap of size K for top-K largest
- The heap top = weakest of current top-K candidates
- Time: O(n log k), Space: O(k)

### Pattern 2: K-Way Merge
- Push first element of each list into min-heap
- Poll smallest, add next from same list
- Time: O(n log k), Space: O(k)

### Pattern 3: Two Heaps for Median
- Max-heap (lower half) + min-heap (upper half)
- Keep sizes balanced (diff ≤ 1)
- Median = top of larger heap, or average of both tops

### Pattern 4: Scheduling / Greedy
- Tasks sorted by priority or end time
- Use min-heap for next available time
- Use max-heap for maximum remaining tasks

---

## Templates

### Template 1: Top-K Largest
```java
// Min-heap of size K — top is weakest of current top-K
public int[] topKLargest(int[] nums, int k) {
    PriorityQueue<Integer> minHeap = new PriorityQueue<>();
    for (int num : nums) {
        minHeap.offer(num);
        if (minHeap.size() > k) minHeap.poll();
    }
    int[] result = new int[k];
    for (int i = k - 1; i >= 0; i--) result[i] = minHeap.poll();
    return result;
}
```

### Template 2: K-Way Merge
```java
public List<Integer> mergeKLists(ListNode[] lists) {
    PriorityQueue<ListNode> pq = new PriorityQueue<>(Comparator.comparingInt(n -> n.val));
    for (ListNode n : lists) if (n != null) pq.offer(n);
    List<Integer> result = new ArrayList<>();
    while (!pq.isEmpty()) {
        ListNode node = pq.poll();
        result.add(node.val);
        if (node.next != null) pq.offer(node.next);
    }
    return result;
}
```

### Template 3: Two Heaps (Streaming Median)
```java
PriorityQueue<Integer> low = new PriorityQueue<>(Collections.reverseOrder()); // max-heap
PriorityQueue<Integer> high = new PriorityQueue<>(); // min-heap

public void addNum(int num) {
    low.offer(num);
    high.offer(low.poll());  // balance: move largest of low to high
    if (low.size() < high.size()) low.offer(high.poll());
}

public double findMedian() {
    return low.size() > high.size() ? low.peek() : (low.peek() + high.peek()) / 2.0;
}
```

### Template 4: Meeting Rooms (Minimum Platforms)
```java
public int minMeetingRooms(int[][] intervals) {
    PriorityQueue<Integer> rooms = new PriorityQueue<>();
    Arrays.sort(intervals, Comparator.comparingInt(a -> a[0]));
    for (int[] interval : intervals) {
        if (!rooms.isEmpty() && rooms.peek() <= interval[0]) rooms.poll();
        rooms.offer(interval[1]);
    }
    return rooms.size();
}
```

---

## Problem Solutions

### LC 295: Find Median from Data Stream (Hard)
```java
// Template 3: two heaps
class MedianFinder {
    PriorityQueue<Integer> lo = new PriorityQueue<>(Collections.reverseOrder());
    PriorityQueue<Integer> hi = new PriorityQueue<>();
    public void addNum(int num) {
        lo.offer(num);
        hi.offer(lo.poll());
        if (lo.size() < hi.size()) lo.offer(hi.poll());
    }
    public double findMedian() {
        return lo.size() > hi.size() ? lo.peek() : (lo.peek() + hi.peek()) / 2.0;
    }
}
```

### LC 703: Kth Largest Element in a Stream (Easy)
```java
// Min-heap of size k
class KthLargest {
    PriorityQueue<Integer> pq = new PriorityQueue<>();
    int k;
    public KthLargest(int k, int[] nums) {
        this.k = k;
        for (int num : nums) {
            pq.offer(num);
            if (pq.size() > k) pq.poll();
        }
    }
    public int add(int val) {
        pq.offer(val);
        if (pq.size() > k) pq.poll();
        return pq.peek();
    }
}
```

### LC 347: Top K Frequent Elements (Medium)
```java
// Bucket sort by frequency — O(n)
public int[] topKFrequent(int[] nums, int k) {
    Map<Integer, Integer> freq = new HashMap<>();
    for (int n : nums) freq.merge(n, 1, Integer::sum);
    List<Integer>[] bucket = new List[nums.length + 1];
    for (int n : freq.keySet()) {
        int f = freq.get(n);
        if (bucket[f] == null) bucket[f] = new ArrayList<>();
        bucket[f].add(n);
    }
    int[] result = new int[k];
    int idx = 0;
    for (int f = nums.length; f >= 0 && idx < k; f--) {
        if (bucket[f] != null) for (int n : bucket[f]) result[idx++] = n;
    }
    return result;
}
```

### LC 23: Merge K Sorted Lists (Hard)
```java
// Template 2
public ListNode mergeKLists(ListNode[] lists) {
    PriorityQueue<ListNode> pq = new PriorityQueue<>(Comparator.comparingInt(n -> n.val));
    for (ListNode n : lists) if (n != null) pq.offer(n);
    ListNode dummy = new ListNode(0), curr = dummy;
    while (!pq.isEmpty()) {
        curr.next = pq.poll();
        curr = curr.next;
        if (curr.next != null) pq.offer(curr.next);
    }
    return dummy.next;
}
```

### LC 621: Task Scheduler (Medium)
```java
// Greedy: most frequent tasks first
public int leastInterval(char[] tasks, int n) {
    int[] count = new int[26];
    for (char c : tasks) count[c - 'A']++;
    PriorityQueue<Integer> pq = new PriorityQueue<>(Collections.reverseOrder());
    for (int c : count) if (c > 0) pq.offer(c);
    int time = 0;
    while (!pq.isEmpty()) {
        List<Integer> used = new ArrayList<>();
        for (int i = 0; i <= n; i++) {
            if (!pq.isEmpty()) {
                int task = pq.poll();
                if (task > 1) used.add(task - 1);
                time++;
            } else if (!used.isEmpty()) {
                time++;  // idle
            }
        }
        for (int t : used) pq.offer(t);
    }
    return time;
}
```

### LC 253: Meeting Rooms II (Medium)
```java
// Min-heap of end times
public int minMeetingRooms(int[][] intervals) {
    if (intervals.length == 0) return 0;
    Arrays.sort(intervals, Comparator.comparingInt(a -> a[0]));
    PriorityQueue<int[]> rooms = new PriorityQueue<>(Comparator.comparingInt(a -> a[1]));
    rooms.offer(intervals[0]);
    for (int i = 1; i < intervals.length; i++) {
        if (rooms.peek()[1] <= intervals[i][0]) rooms.poll();
        rooms.offer(intervals[i]);
    }
    return rooms.size();
}
```

### LC 215: Kth Largest Element in an Array (Medium)
```java
// Template 1
public int findKthLargest(int[] nums, int k) {
    PriorityQueue<Integer> pq = new PriorityQueue<>();
    for (int num : nums) {
        pq.offer(num);
        if (pq.size() > k) pq.poll();
    }
    return pq.peek();
}
```

### LC 659: Split Array into Consecutive Subsequences (Medium)
```java
// Greedy: extend existing sequences, or start new ones
public boolean isPossible(int[] nums) {
    Map<Integer, Integer> avail = new HashMap<>();
    Map<Integer, Integer> need = new HashMap<>();
    for (int num : nums) avail.merge(num, 1, Integer::sum);
    for (int num : nums) {
        if (avail.getOrDefault(num, 0) == 0) continue;
        if (need.getOrDefault(num, 0) > 0) {
            need.merge(num, -1, Integer::sum);
        } else if (avail.getOrDefault(num + 1, 0) > 0 && avail.getOrDefault(num + 2, 0) > 0) {
            avail.merge(num + 1, -1, Integer::sum);
            avail.merge(num + 2, -1, Integer::sum);
            need.merge(num + 3, 1, Integer::sum);
        } else {
            return false;
        }
        avail.merge(num, -1, Integer::sum);
    }
    return true;
}
```

---

## Practice Roadmap

1. **Foundation:** LC 215, LC 703, LC 347
2. **Core:** LC 295, LC 253, LC 23
3. **Advanced:** LC 621, LC 659, LC 218
