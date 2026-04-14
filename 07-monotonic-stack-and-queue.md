# Topic 7: Monotonic Stack & Queue

*Document 7 of 20 in the FAANG DSA Prep series.*

Monotonic stack answers "next greater/smaller element" in O(n) time. Each element is pushed and popped at most once → total O(n). This solves some of the hardest interview problems.

**Top askers:** Google, Amazon, Meta, Microsoft, Bloomberg

---

## Core Insight

Store **indices** (not values) in the stack. When current element `curr` pops an element `x`:
- `curr` is the **next greater/smaller** for `x`
- `stack.peek()` (new top) is the **previous greater/smaller** for `x`

That's three pieces of information from one pop.

---

## When to Use

- "Next greater/smaller element" (or previous)
- "Largest rectangle in histogram"
- "Sliding window maximum/minimum"
- "How many days until warmer temperature"
- "Sum of subarray minimums"
- "Remove k digits" to get smallest number

---

## Templates

### Template 1: Next Greater Element (Decreasing Stack)

```java
// Stack indices, values decrease from bottom to top
public int[] nextGreaterElement(int[] arr) {
    int n = arr.length;
    int[] result = new int[n];
    Arrays.fill(result, -1);
    Deque<Integer> stack = new ArrayDeque<>();

    for (int i = 0; i < n; i++) {
        while (!stack.isEmpty() && arr[stack.peek()] < arr[i]) {
            result[stack.pop()] = arr[i];  // popped element found its next greater
        }
        stack.push(i);
    }
    return result;
}
```

**Trace `[2, 1, 2, 4, 3]`:**
```
i=0: push 0.        stack=[0]
i=1: arr[0]>=arr[1], push 1.  stack=[0,1]
i=2: arr[1]<arr[2], pop→result[1]=2. arr[0]>=arr[2], push 2. stack=[0,2]
i=3: arr[2]<arr[3], pop→result[2]=4. arr[0]<arr[3], pop→result[0]=4. push 3. stack=[3]
i=4: arr[3]>=arr[4], push 4.  stack=[3,4]
Result: [4, 2, 4, -1, -1]
```

### Template 2: Next Smaller Element (Increasing Stack)

Flip the comparison: `>` instead of `<`.

```java
public int[] nextSmallerElement(int[] arr) {
    int n = arr.length;
    int[] result = new int[n];
    Arrays.fill(result, -1);
    Deque<Integer> stack = new ArrayDeque<>();

    for (int i = 0; i < n; i++) {
        while (!stack.isEmpty() && arr[stack.peek()] > arr[i]) {
            result[stack.pop()] = arr[i];
        }
        stack.push(i);
    }
    return result;
}
```

### Template 3: Previous Greater / Smaller

Read answer from `stack.peek()` BEFORE pushing, after popping elements that don't qualify.

```java
public int[] previousGreater(int[] arr) {
    int n = arr.length;
    int[] result = new int[n];
    Arrays.fill(result, -1);
    Deque<Integer> stack = new ArrayDeque<>();

    for (int i = 0; i < n; i++) {
        while (!stack.isEmpty() && arr[stack.peek()] <= arr[i]) stack.pop();
        if (!stack.isEmpty()) result[i] = arr[stack.peek()];
        stack.push(i);
    }
    return result;
}
```

### Template 4: Monotonic Deque for Sliding Window

- Remove from back: elements smaller than current (useless, current is newer and larger)
- Remove from front: elements that slid out of the window
- Front is always the maximum

```java
public int[] maxSlidingWindow(int[] nums, int k) {
    int[] result = new int[nums.length - k + 1];
    Deque<Integer> deque = new ArrayDeque<>();  // stores indices

    for (int i = 0; i < nums.length; i++) {
        while (!deque.isEmpty() && nums[deque.peekLast()] <= nums[i]) deque.pollLast();
        deque.offerLast(i);

        if (deque.peekFirst() <= i - k) deque.pollFirst();

        if (i >= k - 1) result[i - k + 1] = nums[deque.peekFirst()];
    }
    return result;
}
```

---

## Problem Solutions

### LC 739: Daily Temperatures (Medium)
```java
// Next greater element: find how many days until warmer
public int[] dailyTemperatures(int[] temperatures) {
    int n = temperatures.length;
    int[] result = new int[n];
    Deque<Integer> stack = new ArrayDeque<>();

    for (int i = 0; i < n; i++) {
        while (!stack.isEmpty() && temperatures[stack.peek()] < temperatures[i]) {
            int idx = stack.pop();
            result[idx] = i - idx;
        }
        stack.push(i);
    }
    return result;
}
```

### LC 84: Largest Rectangle in Histogram (Hard)
```java
// Key: add sentinel 0 at end to flush the stack
public int largestRectangleArea(int[] heights) {
    int n = heights.length;
    int maxArea = 0;
    Deque<Integer> stack = new ArrayDeque<>();  // stores indices

    for (int i = 0; i <= n; i++) {
        int curr = (i == n) ? 0 : heights[i];
        while (!stack.isEmpty() && heights[stack.peek()] > curr) {
            int h = heights[stack.pop()];
            int w = stack.isEmpty() ? i : i - stack.peek() - 1;
            maxArea = Math.max(maxArea, h * w);
        }
        stack.push(i);
    }
    return maxArea;
}
```

**Trace `[2,1,5,6,2,3]`:**
```
i=0: push 0
i=1: pop 0→h=2,w=1→area=2. push 1
i=2: push 2
i=3: push 3
i=4: curr=2<6, pop3→h=6,w=1→area=6. pop2→h=5,w=2→area=10. push 4
i=5: push 5
i=6: curr=0, pop5→h=3,w=1→area=3. pop4→h=2,w=2→area=4. pop1→h=1,w=5→area=5
```

### LC 85: Maximal Rectangle (Hard)
```java
// Treat each row as histogram, apply LC 84
public int maximalRectangle(char[][] matrix) {
    if (matrix.length == 0) return 0;
    int m = matrix.length, n = matrix[0].length;
    int[] heights = new int[n];
    int maxArea = 0;

    for (int i = 0; i < m; i++) {
        for (int j = 0; j < n; j++) {
            heights[j] = (matrix[i][j] == '1') ? heights[j] + 1 : 0;
        }
        maxArea = Math.max(maxArea, largestRectangleArea(heights));
    }
    return maxArea;
}
```

### LC 239: Sliding Window Maximum (Hard)
```java
// Template 4: monotonic decreasing deque
public int[] maxSlidingWindow(int[] nums, int k) {
    int[] result = new int[nums.length - k + 1];
    Deque<Integer> deque = new ArrayDeque<>();

    for (int i = 0; i < nums.length; i++) {
        while (!deque.isEmpty() && nums[deque.peekLast()] <= nums[i]) deque.pollLast();
        deque.offerLast(i);

        if (deque.peekFirst() <= i - k) deque.pollFirst();

        if (i >= k - 1) result[i - k + 1] = nums[deque.peekFirst()];
    }
    return result;
}
```

### LC 907: Sum of Subarray Minimums (Medium)
```java
// For each element, find contribution as minimum in all subarrays containing it
// Contribution = arr[i] × left × right where left/right = distance to previous/next smaller
public int sumSubarrayMins(int[] arr) {
    int n = arr.length;
    long MOD = 1_000_000_007;
    Deque<Integer> stack = new ArrayDeque<>();
    long[] dp = new long[n];  // dp[i] = sum of minimums of all subarrays ending at i

    long result = 0;
    for (int i = 0; i < n; i++) {
        while (!stack.isEmpty() && arr[stack.peek()] > arr[i]) stack.pop();
        int prevSmaller = stack.isEmpty() ? -1 : stack.peek();
        dp[i] = (prevSmaller == -1 ? 0 : dp[prevSmaller]) + (long)arr[i] * (i - prevSmaller);
        result = (result + dp[i]) % MOD;
        stack.push(i);
    }
    return (int)result;
}
```

### LC 496: Next Greater Element I (Easy)
```java
// Template 1, then map results to nums1
public int[] nextGreaterElement(int[] nums1, int[] nums2) {
    Map<Integer, Integer> map = new HashMap<>();
    Deque<Integer> stack = new ArrayDeque<>();
    for (int num : nums2) {
        while (!stack.isEmpty() && stack.peek() < num) map.put(stack.pop(), num);
        stack.push(num);
    }
    int[] result = new int[nums1.length];
    for (int i = 0; i < nums1.length; i++)
        result[i] = map.getOrDefault(nums1[i], -1);
    return result;
}
```

### LC 402: Remove K Digits (Medium)
```java
// Monotonic increasing stack: remove larger digits earlier
public String removeKdigits(String num, int k) {
    Deque<Character> stack = new ArrayDeque<>();
    for (char c : num.toCharArray()) {
        while (k > 0 && !stack.isEmpty() && stack.peekLast() > c) {
            stack.pollLast();
            k--;
        }
        stack.offerLast(c);
    }
    while (k > 0 && !stack.isEmpty()) {
        stack.pollLast();
        k--;
    }
    StringBuilder sb = new StringBuilder();
    while (!stack.isEmpty()) sb.append(stack.pollFirst());
    while (sb.length() > 0 && sb.charAt(0) == '0') sb.deleteCharAt(0);
    return sb.length() == 0 ? "0" : sb.toString();
}
```

---

## Practice Roadmap

1. **Foundation:** LC 496, LC 739, LC 225 (min stack)
2. **Core:** LC 84, LC 739, LC 503 (circular)
3. **Hard:** LC 85, LC 239, LC 907, LC 402
4. **Advanced:** LC 901, LC 862, LC 1081
