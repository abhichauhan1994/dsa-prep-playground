# Topic 18: Intervals

*Document 18 of 20 in the FAANG DSA Prep series.*

Almost every interval problem starts with: **sort by start time**. Then single-pass processing.

**Top askers:** Google, Amazon, Meta, Microsoft, Bloomberg

---

## Three Core Operations

1. **Merge Overlapping:** Sort, walk, extend or start new
2. **Insert Interval:** Three phases (before, merge, after)
3. **Sweep Line:** Convert intervals to events (+1 start, -1 end), sweep through

---

## Overlap Condition

- Overlap: `a.start <= b.end && b.start <= a.end`
- No overlap: `a.end < b.start || b.end < a.start`

---

## Templates

### Template 1: Merge Intervals
```java
public int[][] merge(int[][] intervals) {
    Arrays.sort(intervals, (a, b) -> a[0] - b[0]);
    List<int[]> result = new ArrayList<>();
    int[] cur = intervals[0];
    for (int i = 1; i < intervals.length; i++) {
        if (intervals[i][0] <= cur[1]) cur[1] = Math.max(cur[1], intervals[i][1]);
        else { result.add(cur); cur = intervals[i]; }
    }
    result.add(cur);
    return result.toArray(new int[result.size()][]);
}
```

### Template 2: Insert Interval
```java
public int[][] insert(int[][] intervals, int[] newInterval) {
    List<int[]> result = new ArrayList<>();
    int i = 0;
    while (i < intervals.length && intervals[i][1] < newInterval[0]) result.add(intervals[i++]);
    while (i < intervals.length && intervals[i][0] <= newInterval[1]) {
        newInterval[0] = Math.min(newInterval[0], intervals[i][0]);
        newInterval[1] = Math.max(newInterval[1], intervals[i++][1]);
    }
    result.add(newInterval);
    while (i < intervals.length) result.add(intervals[i++]);
    return result.toArray(new int[result.size()][]);
}
```

### Template 3: Sweep Line
```java
public int minMeetingRooms(int[][] intervals) {
    int n = intervals.length;
    int[][] events = new int[2 * n][2];
    for (int i = 0; i < n; i++) {
        events[2 * i] = new int[]{intervals[i][0], 1};
        events[2 * i + 1] = new int[]{intervals[i][1], -1};
    }
    Arrays.sort(events, (a, b) -> a[0] != b[0] ? a[0] - b[0] : a[1] - b[1]);
    int rooms = 0, current = 0;
    for (int[] e : events) { current += e[1]; rooms = Math.max(rooms, current); }
    return rooms;
}
```

---

## Problem Solutions

### LC 56: Merge Intervals (Medium)
```java
// Template 1
public int[][] merge(int[][] intervals) {
    Arrays.sort(intervals, (a, b) -> a[0] - b[0]);
    List<int[]> result = new ArrayList<>();
    int[] cur = intervals[0];
    for (int i = 1; i < intervals.length; i++) {
        if (intervals[i][0] <= cur[1]) cur[1] = Math.max(cur[1], intervals[i][1]);
        else { result.add(cur); cur = intervals[i]; }
    }
    result.add(cur);
    return result.toArray(new int[result.size()][]);
}
```

### LC 57: Insert Interval (Hard)
```java
// Template 2
public int[][] insert(int[][] intervals, int[] newInterval) {
    List<int[]> result = new ArrayList<>();
    int i = 0;
    while (i < intervals.length && intervals[i][1] < newInterval[0]) result.add(intervals[i++]);
    while (i < intervals.length && intervals[i][0] <= newInterval[1]) {
        newInterval[0] = Math.min(newInterval[0], intervals[i][0]);
        newInterval[1] = Math.max(newInterval[1], intervals[i++][1]);
    }
    result.add(newInterval);
    while (i < intervals.length) result.add(intervals[i++]);
    return result.toArray(new int[result.size()][]);
}
```

### LC 252: Meeting Rooms (Easy)
```java
// Sort starts and ends separately
public boolean canAttendMeetings(int[][] intervals) {
    int[] starts = new int[intervals.length];
    int[] ends = new int[intervals.length];
    for (int i = 0; i < intervals.length; i++) { starts[i] = intervals[i][0]; ends[i] = intervals[i][1]; }
    Arrays.sort(starts); Arrays.sort(ends);
    for (int i = 1; i < intervals.length; i++)
        if (starts[i] < ends[i - 1]) return false;
    return true;
}
```

### LC 253: Meeting Rooms II (Medium)
```java
// Min-heap of end times
public int minMeetingRooms(int[][] intervals) {
    Arrays.sort(intervals, (a, b) -> a[0] - b[0]);
    PriorityQueue<Integer> pq = new PriorityQueue<>();
    for (int[] interval : intervals) {
        if (!pq.isEmpty() && pq.peek() <= interval[0]) pq.poll();
        pq.offer(interval[1]);
    }
    return pq.size();
}
```

### LC 435: Non-overlapping Intervals (Medium)
```java
// Greedy: sort by end, remove intervals that overlap with last kept
public int eraseOverlapIntervals(int[][] intervals) {
    if (intervals.length == 0) return 0;
    Arrays.sort(intervals, (a, b) -> a[1] - b[1]);
    int count = 0, lastEnd = intervals[0][1];
    for (int i = 1; i < intervals.length; i++) {
        if (intervals[i][0] < lastEnd) count++;
        else lastEnd = intervals[i][1];
    }
    return count;
}
```

### LC 452: Minimum Number of Arrows (Medium)
```java
// Sort by end, shoot at end of each balloon
public int findMinArrowShots(int[][] points) {
    if (points.length == 0) return 0;
    Arrays.sort(points, (a, b) -> a[1] - b[1]);
    int arrows = 1, lastEnd = points[0][1];
    for (int i = 1; i < points.length; i++)
        if (points[i][0] > lastEnd) { arrows++; lastEnd = points[i][1]; }
    return arrows;
}
```

### LC 986: Interval List Intersections (Medium)
```java
// Two pointers on two sorted lists
public int[][] intervalIntersection(int[][] A, int[][] B) {
    List<int[]> result = new ArrayList<>();
    int i = 0, j = 0;
    while (i < A.length && j < B.length) {
        int lo = Math.max(A[i][0], B[j][0]);
        int hi = Math.min(A[i][1], B[j][1]);
        if (lo <= hi) result.add(new int[]{lo, hi});
        if (A[i][1] < B[j][1]) i++;
        else j++;
    }
    return result.toArray(new int[result.size()][]);
}
```

### LC 763: Partition Labels (Medium)
```java
// Greedy: last occurrence + double pass
public List<Integer> partitionLabels(String s) {
    int[] last = new int[26];
    for (int i = 0; i < s.length(); i++) last[s.charAt(i) - 'a'] = i;
    List<Integer> result = new ArrayList<>();
    int start = 0, end = 0;
    for (int i = 0; i < s.length(); i++) {
        end = Math.max(end, last[s.charAt(i) - 'a']);
        if (i == end) { result.add(end - start + 1); start = i + 1; }
    }
    return result;
}
```

### LC 1094: Car Pooling (Medium)
```java
// Difference array
public boolean carPooling(int[][] trips, int capacity) {
    int[] diff = new int[1001];
    for (int[] t : trips) {
        diff[t[1]] += t[0];
        diff[t[2]] -= t[0];
    }
    int current = 0;
    for (int d : diff) { current += d; if (current > capacity) return false; }
    return true;
}
```

---

## Practice Roadmap

1. **Foundation:** LC 56, LC 252, LC 253
2. **Intermediate:** LC 57, LC 435, LC 986
3. **Advanced:** LC 452, LC 763, LC 1094
