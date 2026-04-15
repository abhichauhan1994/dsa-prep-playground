# Topic 1: Sliding Window

The Sliding Window pattern is one of the most frequently tested patterns in FAANG interviews. It transforms naive O(n*k) brute-force solutions into elegant O(n) algorithms by maintaining a contiguous "window" of elements and updating state incrementally as the window moves, rather than recomputing from scratch each time.

This document covers every variant, every common problem, and every trap you'll encounter. Work through it once, and you'll recognize the pattern instantly in any interview.

---

## Table of Contents

1. [Core Concept](#1-core-concept)
2. [ELI5 — The Intuition](#2-eli5--the-intuition)
3. [When to Use — Recognition Signals](#3-when-to-use--recognition-signals)
4. [Core Templates in Java](#4-core-templates-in-java)
   - [Template 1: Fixed-Size Window](#template-1-fixed-size-window)
   - [Template 2: Variable Window — Find Longest](#template-2-variable-window--find-longest)
   - [Template 3: Variable Window — Find Shortest](#template-3-variable-window--find-shortest)
   - [Template 4: Frequency Map Window](#template-4-frequency-map-window)
   - [Template 5: Exactly K = At Most K minus At Most K-1](#template-5-exactly-k--at-most-k---at-most-k-1)
5. [Real-World Applications](#5-real-world-applications)
6. [Problem Categories and Solutions](#6-problem-categories-and-solutions)
7. [Common Mistakes and Edge Cases](#7-common-mistakes-and-edge-cases)
8. [Pattern Comparison](#8-pattern-comparison)
9. [Quick Reference Cheat Sheet](#9-quick-reference-cheat-sheet)
10. [Practice Roadmap](#10-practice-roadmap)

---

**Difficulty Distribution:**
- Easy: 14%
- Medium: 60%
- Hard: 26%

**Top Companies:**
- Google: 101 problems
- Amazon: 94 problems
- Meta: 71 problems
- Microsoft: 70 problems
- Bloomberg: 61 problems

---

## 1. Core Concept

### What is Sliding Window?

A sliding window is a subarray or substring of fixed or variable length that moves through a larger array or string from left to right. Instead of recomputing the answer for every possible window from scratch, you maintain the current window's state and update it by:

1. **Adding** the new element entering from the right
2. **Removing** the element leaving from the left

This incremental update is the entire trick. The window "slides" forward, and you never look at the same element twice in the inner loop.

### The Fundamental Idea

Consider finding the maximum sum of any subarray of size k in an array of n elements.

**Brute force:** For each starting index i, sum elements from i to i+k-1. That's O(n*k).

```
arr = [2, 1, 5, 1, 3, 2], k = 3

Window [2,1,5] → sum = 8
Window [1,5,1] → sum = 7   (recomputed 1 and 5 again — wasteful)
Window [5,1,3] → sum = 9   (recomputed 5 and 1 again — wasteful)
Window [1,3,2] → sum = 6
```

**Sliding window:** When you move from window [2,1,5] to [1,5,1], you just subtract 2 (left element leaving) and add 1 (right element entering). One subtraction, one addition. O(n) total.

```
sum = 8
Move right: sum = 8 - arr[0] + arr[3] = 8 - 2 + 1 = 7
Move right: sum = 7 - arr[1] + arr[4] = 7 - 1 + 3 = 9
Move right: sum = 9 - arr[2] + arr[5] = 9 - 5 + 2 = 6
```

### Time Complexity Improvement

| Approach | Time | Space |
|----------|------|-------|
| Brute force (nested loops) | O(n * k) | O(1) |
| Sliding window | O(n) | O(1) to O(k) |

For n = 10^5 and k = 10^4, brute force does 10^9 operations. Sliding window does 10^5. That's the difference between TLE and AC.

### Two Types of Windows

**Fixed-size window:** The window always contains exactly k elements. You slide it one step at a time. The left pointer always trails the right pointer by exactly k-1 positions.

**Variable-size (dynamic) window:** The window expands and contracts based on a condition. You expand by moving the right pointer, and shrink by moving the left pointer when the window becomes invalid (or when you're looking for the minimum valid window).

---

## 2. ELI5 — The Intuition

### Fixed Window: The Train Window

Imagine you're on a train, looking out through a fixed-size window. As the train moves forward, new scenery appears on the right side of your window and old scenery disappears from the left. You never need to look at all the scenery again from the beginning. You just track what changed: one new thing came in, one old thing went out.

That's a fixed sliding window. The window size never changes. You just update your running answer as the window moves.

### Variable Window: The Rubber Band

Now imagine you're holding a rubber band stretched between your two index fingers, both resting on a ruler. Your left finger is the `left` pointer, your right finger is the `right` pointer.

- You want to find the longest stretch of the ruler where some condition holds (say, no repeated numbers).
- You move your right finger to the right, stretching the band, as long as the condition is satisfied.
- The moment the condition breaks, you move your left finger to the right, shrinking the band, until the condition is satisfied again.
- At every valid stretch, you record the length.

The rubber band never jumps backward. Both fingers only move right. That's why it's O(n): each element is added once and removed at most once.

### The "Exactly K" Trick: Two Rubber Bands

Some problems ask for subarrays with exactly K distinct elements. That's hard to handle directly. But here's the insight: "exactly K" = "at most K" minus "at most K-1". So you run the rubber band exercise twice with different constraints and subtract the counts. This is a powerful mathematical trick that converts a hard problem into two easy ones.

---

## 3. When to Use — Recognition Signals

### Green Flags — Use Sliding Window

Read the problem statement carefully. These phrases almost always signal a sliding window:

- "contiguous subarray" or "contiguous substring"
- "longest substring that..."
- "shortest subarray with..."
- "maximum/minimum sum subarray of size k"
- "subarray of size exactly k"
- "at most k distinct characters/elements"
- "permutation of one string in another"
- "anagram in a string"
- "find all windows where..."
- "sliding window of size k"
- "moving average"

The key structural requirement: **the answer must be a contiguous portion of the input**, and **the validity condition can be maintained incrementally** as the window moves.

### Red Flags — Do NOT Use Sliding Window

**Non-contiguous subsequences:** If the problem asks for subsequences (elements don't need to be adjacent), sliding window won't work. Use DP or two pointers on sorted arrays instead.

**Negative numbers in sum problems:** The sliding window works for sum problems because adding an element always increases the sum and removing always decreases it (monotonic property). With negative numbers, this breaks. A window might become valid again after shrinking past a negative element. Use prefix sums + hash map or Kadane's algorithm instead.

**Need all combinations or permutations:** If you need to enumerate all possible subsets or arrangements, use backtracking. Sliding window only finds optimal windows, not all windows.

**Circular arrays without modification:** Standard sliding window assumes linear traversal. Circular arrays need special handling (usually duplicate the array).

**2D problems:** Sliding window extends to 2D (sliding window on rows + prefix sums on columns), but it's a different technique. Don't apply 1D templates directly.

---

## 4. Core Templates in Java

These five templates cover 95% of sliding window problems. Memorize the structure, understand the invariants, and adapt as needed.

---

### Template 1: Fixed-Size Window

**Use when:** The problem specifies a window of exactly size k.

**Key invariant:** `right - left + 1 == k` at all times after the initial window is formed.

```java
public int fixedWindowTemplate(int[] arr, int k) {
    int n = arr.length;
    
    if (n < k) return -1;
    
    int windowSum = 0;
    int result = Integer.MIN_VALUE;
    
    for (int i = 0; i < k; i++) {
        windowSum += arr[i];
    }
    result = windowSum;
    
    for (int right = k; right < n; right++) {
        windowSum += arr[right];
        windowSum -= arr[right - k];
        result = Math.max(result, windowSum);
    }
    
    return result;
}
```

---

### Template 2: Variable Window — Find Longest

**Use when:** Find the longest/maximum window satisfying some condition.

**Strategy:** Expand right as far as possible. When the window becomes invalid, shrink from the left until it's valid again. Record the window size after every expansion (not after shrinking, because we want the longest valid window).

**Key invariant:** After the shrink loop, the window `[left, right]` is always valid.

```java
public int variableWindowLongest(int[] arr) {
    int left = 0;
    int result = 0;
    Map<Integer, Integer> freq = new HashMap<>();
    
    for (int right = 0; right < arr.length; right++) {
        freq.merge(arr[right], 1, Integer::sum);
        
        while (isInvalid(freq)) {
            freq.merge(arr[left], -1, Integer::sum);
            if (freq.get(arr[left]) == 0) freq.remove(arr[left]);
            left++;
        }
        
        result = Math.max(result, right - left + 1);
    }
    
    return result;
}

private boolean isInvalid(Map<Integer, Integer> freq) {
    return freq.size() > 2;
}
```

**Why use `while` not `if` for shrinking?**

With `if`, you shrink by at most one element per iteration. But sometimes you need to shrink multiple elements before the window becomes valid again. `while` handles this correctly. Using `if` is a classic bug.

---

### Template 3: Variable Window — Find Shortest

**Use when:** Find the shortest/minimum window satisfying some condition.

**Strategy:** Expand right until the window becomes valid. Then shrink from the left as long as the window remains valid (to minimize the window). Record the window size DURING shrinking (every valid window is a candidate).

**Key difference from Template 2:** You update the result inside the shrink loop, not outside it.

```java
public int variableWindowShortest(int[] arr, int target) {
    int left = 0;
    int result = Integer.MAX_VALUE;
    int windowSum = 0;
    
    for (int right = 0; right < arr.length; right++) {
        windowSum += arr[right];
        
        while (windowSum >= target) {
            result = Math.min(result, right - left + 1);
            windowSum -= arr[left];
            left++;
        }
    }
    
    return result == Integer.MAX_VALUE ? 0 : result;
}
```

**The mental model:**
- Template 2 (longest): shrink to fix invalidity, then record. "Fix first, then measure."
- Template 3 (shortest): record while valid, then shrink to minimize. "Measure while shrinking."

---

### Template 4: Frequency Map Window (Anagram/Permutation Problems)

**Use when:** You need to find windows that are permutations or anagrams of a pattern string.

**Key idea:** Maintain a frequency map of the pattern. Track how many characters in the window have the "correct" frequency (the `formed` variable). When `formed == required`, the window is a valid anagram.

```java
public List<Integer> frequencyMapWindow(String s, String p) {
    List<Integer> result = new ArrayList<>();
    
    if (s.length() < p.length()) return result;
    
    int k = p.length();
    int[] pFreq = new int[26];
    int[] wFreq = new int[26];
    
    for (char c : p.toCharArray()) {
        pFreq[c - 'a']++;
    }
    
    int required = 0;
    for (int f : pFreq) if (f > 0) required++;
    
    int formed = 0;
    int left = 0;
    
    for (int right = 0; right < s.length(); right++) {
        char c = s.charAt(right);
        wFreq[c - 'a']++;
        
        if (pFreq[c - 'a'] > 0 && wFreq[c - 'a'] == pFreq[c - 'a']) {
            formed++;
        }
        
        if (right - left + 1 > k) {
            char leftChar = s.charAt(left);
            
            if (pFreq[leftChar - 'a'] > 0 && wFreq[leftChar - 'a'] == pFreq[leftChar - 'a']) {
                formed--;
            }
            
            wFreq[leftChar - 'a']--;
            left++;
        }
        
        if (right - left + 1 == k && formed == required) {
            result.add(left);
        }
    }
    
    return result;
}
```

---

### Template 5: Exactly K = At Most K minus At Most K-1

**Use when:** Count subarrays with EXACTLY K of something (distinct elements, odd numbers, etc.).

**The insight:** Directly counting "exactly K" is hard because the window can't maintain a simple valid/invalid state. But "at most K" is easy with Template 2. And:

```
count(exactly K) = count(at most K) - count(at most K-1)
```

```java
public int exactlyK(int[] arr, int k) {
    return atMostK(arr, k) - atMostK(arr, k - 1);
}

private int atMostK(int[] arr, int k) {
    int left = 0;
    int count = 0;
    Map<Integer, Integer> freq = new HashMap<>();
    
    for (int right = 0; right < arr.length; right++) {
        freq.merge(arr[right], 1, Integer::sum);
        
        while (freq.size() > k) {
            freq.merge(arr[left], -1, Integer::sum);
            if (freq.get(arr[left]) == 0) freq.remove(arr[left]);
            left++;
        }
        
        count += right - left + 1;
    }
    
    return count;
}
```

**Why does `count += right - left + 1` work?**

When the window is `[left, right]`, all subarrays ending at `right` with start index from `left` to `right` are valid. That's `right - left + 1` subarrays.

---

## 5. Real-World Applications

### 1. TCP Sliding Window Protocol
TCP uses a sliding window for flow control between sender and receiver. The receiver advertises a "window size" — the number of bytes it can buffer. As ACKs arrive, the window slides forward, allowing more data to be sent.

### 2. API Rate Limiting
AWS API Gateway, Cloudflare, and Redis-based rate limiters use sliding windows to count requests in the last N seconds. This prevents burst traffic at bucket boundaries.

### 3. Stream Processing
Apache Flink and Apache Kafka Streams use sliding windows for real-time analytics. A sliding window might compute "average CPU usage over the last 5 minutes, updated every 30 seconds."

### 4. Financial Systems
Moving averages are fundamental to technical analysis in trading. A Simple Moving Average (SMA) over 20 days is a fixed-size sliding window: sum the last 20 closing prices, divide by 20, slide forward one day.

---

## 6. Problem Categories and Solutions

### Category A: Fixed-Size Window

#### LC 643 — Maximum Average Subarray I (Easy)
```java
public double findMaxAverage(int[] nums, int k) {
    double windowSum = 0;
    for (int i = 0; i < k; i++) {
        windowSum += nums[i];
    }
    double maxSum = windowSum;
    
    for (int right = k; right < nums.length; right++) {
        windowSum += nums[right];
        windowSum -= nums[right - k];
        maxSum = Math.max(maxSum, windowSum);
    }
    
    return maxSum / k;
}
```
**Time:** O(n) | **Space:** O(1)

---
#### LC 1456 — Maximum Number of Vowels in a Substring of Given Length

**Difficulty:** Medium
**Companies:** Amazon, Google, Meta, Microsoft

**Problem:** Given a string `s` and an integer `k`, return the maximum number of vowel letters in any substring of `s` with length `k`.

**Why sliding window:** Fixed window size k, counting vowels. When the window slides, you add 1 if the new character is a vowel, subtract 1 if the leaving character was a vowel.

```java
class Solution {
    public int maxVowels(String s, int k) {
        Set<Character> vowels = Set.of('a', 'e', 'i', 'o', 'u');
        
        int vowelCount = 0;
        
        // Build first window
        for (int i = 0; i < k; i++) {
            if (vowels.contains(s.charAt(i))) vowelCount++;
        }
        
        int maxVowels = vowelCount;
        
        // Slide the window
        for (int right = k; right < s.length(); right++) {
            // Add new character
            if (vowels.contains(s.charAt(right))) vowelCount++;
            
            // Remove leaving character
            if (vowels.contains(s.charAt(right - k))) vowelCount--;
            
            maxVowels = Math.max(maxVowels, vowelCount);
        }
        
        return maxVowels;
    }
}
```

**Time:** O(n) | **Space:** O(1) — the vowel set is constant size

---

#### LC 567 — Permutation in String (Medium)
```java
public boolean checkInclusion(String s1, String s2) {
    if (s1.length() > s2.length()) return false;
    
    int k = s1.length();
    int[] pFreq = new int[26];
    int[] windowFreq = new int[26];
    
    for (char c : s1.toCharArray()) pFreq[c - 'a']++;
    
    int required = 0;
    for (int f : pFreq) if (f > 0) required++;
    
    int formed = 0;
    int left = 0;
    
    for (int right = 0; right < s2.length(); right++) {
        char c = s2.charAt(right);
        windowFreq[c - 'a']++;
        
        if (pFreq[c - 'a'] > 0 && windowFreq[c - 'a'] == pFreq[c - 'a']) {
            formed++;
        }
        
        if (right - left + 1 > k) {
            char leftChar = s2.charAt(left);
            if (pFreq[leftChar - 'a'] > 0 && windowFreq[leftChar - 'a'] == pFreq[leftChar - 'a']) {
                formed--;
            }
            windowFreq[leftChar - 'a']--;
            left++;
        }
        
        if (right - left + 1 == k && formed == required) {
            return true;
        }
    }
    
    return false;
}
```

---

#### LC 239 — Sliding Window Maximum (Hard)
```java
public int[] maxSlidingWindow(int[] nums, int k) {
    int n = nums.length;
    int[] result = new int[n - k + 1];
    Deque<Integer> deque = new ArrayDeque<>();
    
    for (int right = 0; right < n; right++) {
        while (!deque.isEmpty() && nums[deque.peekLast()] < nums[right]) {
            deque.pollLast();
        }
        deque.addLast(right);
        
        if (deque.peekFirst() < right - k + 1) {
            deque.pollFirst();
        }
        
        if (right >= k - 1) {
            result[right - k + 1] = nums[deque.peekFirst()];
        }
    }
    
    return result;
}
```
**Time:** O(n) | **Space:** O(k)

---

### Category B: Variable Window — Longest/Maximum

#### LC 3 — Longest Substring Without Repeating Characters (Medium)
```java
public int lengthOfLongestSubstring(String s) {
    Map<Character, Integer> lastSeen = new HashMap<>();
    int left = 0;
    int maxLen = 0;
    
    for (int right = 0; right < s.length(); right++) {
        char c = s.charAt(right);
        
        if (lastSeen.containsKey(c) && lastSeen.get(c) >= left) {
            left = lastSeen.get(c) + 1;
        }
        
        lastSeen.put(c, right);
        maxLen = Math.max(maxLen, right - left + 1);
    }
    
    return maxLen;
}
```

---

#### LC 209 — Minimum Size Subarray Sum (Medium)
```java
public int minSubArrayLen(int target, int[] nums) {
    int left = 0;
    int windowSum = 0;
    int result = Integer.MAX_VALUE;
    
    for (int right = 0; right < nums.length; right++) {
        windowSum += nums[right];
        
        while (windowSum >= target) {
            result = Math.min(result, right - left + 1);
            windowSum -= nums[left];
            left++;
        }
    }
    
    return result == Integer.MAX_VALUE ? 0 : result;
}
```

---

#### LC 76 — Minimum Window Substring (Hard)
```java
public String minWindow(String s, String t) {
    if (s.length() < t.length()) return "";
    
    Map<Character, Integer> tFreq = new HashMap<>();
    for (char c : t.toCharArray()) {
        tFreq.merge(c, 1, Integer::sum);
    }
    
    int required = tFreq.size();
    int formed = 0;
    Map<Character, Integer> windowFreq = new HashMap<>();
    int left = 0;
    int minLen = Integer.MAX_VALUE;
    int minLeft = 0;
    
    for (int right = 0; right < s.length(); right++) {
        char c = s.charAt(right);
        if (tFreq.containsKey(c)) {
            windowFreq.merge(c, 1, Integer::sum);
            if (windowFreq.get(c).equals(tFreq.get(c))) {
                formed++;
            }
        }
        
        while (formed == required) {
            int windowLen = right - left + 1;
            if (windowLen < minLen) {
                minLen = windowLen;
                minLeft = left;
            }
            
            char leftChar = s.charAt(left);
            if (tFreq.containsKey(leftChar)) {
                if (windowFreq.get(leftChar).equals(tFreq.get(leftChar))) {
                    formed--;
                }
                windowFreq.merge(leftChar, -1, Integer::sum);
            }
            left++;
        }
    }
    
    return minLen == Integer.MAX_VALUE ? "" : s.substring(minLeft, minLeft + minLen);
}
```

---

## 7. Common Mistakes and Edge Cases

1. **Using `if` instead of `while` for shrinking** — You might miss shrinking by multiple elements
2. **Not handling empty strings/arrays** — Always check for edge cases
3. **Off-by-one errors in window boundaries** — Remember: `right - left + 1` gives window size
4. **Forgetting to remove from frequency map when count becomes 0** — This can cause size() to be incorrect
5. **Negative numbers in sum problems** — Sliding window doesn't work with negatives in sum problems

---

## 8. Pattern Comparison

| Problem Type | Template | Key Condition |
|-------------|---------|--------------|
| Fixed size k | Template 1 | Window always has exactly k elements |
| Longest valid | Template 2 | Shrink while invalid, record after |
| Shortest valid | Template 3 | Shrink while valid, record inside loop |
| Anagrams | Template 4 | Track formed vs required |
| Exactly K | Template 5 | atMost(K) - atMost(K-1) |

---

## 9. Quick Reference Cheat Sheet

```
Fixed Window:
  for (right = k; right < n; right++)
      sum += arr[right] - arr[right - k]

Variable Window (Longest):
  while (condition invalid)
      shrink from left
  update result after while loop

Variable Window (Shortest):
  while (condition valid)
      update result
      shrink from left
```

---

## 10. Practice Roadmap

| Day | Problem | Difficulty | Focus |
|-----|---------|------------|-------|
| 1 | LC 643 | Easy | Fixed window basics |
| 2 | LC 1456 | Easy | Fixed window + char tracking |
| 3 | LC 3 | Medium | Variable window + HashMap |
| 4 | LC 209 | Medium | Shortest window (inverted!) |
| 5 | LC 424 | Medium | The maxFreq trick |
| 6 | LC 567 | Medium | Frequency map window |
| 7 | LC 438 | Medium | Collect all anagram positions |
| 8 | LC 930 | Medium | "Exactly K" = atMost - atMost |
| 9 | LC 76 | Hard | Minimum window substring |
| 10 | LC 239 | Hard | Monotonic deque |

**Checkpoint:** You can write all 5 templates from memory. You can identify "sliding window" within 30 seconds.