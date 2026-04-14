# Topic 4: Binary Search

*Document 4 of 20 in the FAANG DSA Prep series.*

Binary search finds a boundary in a monotonic predicate: `false, false, ..., true, true, ...`. It's not just "search a sorted array" — it's a thinking framework.

**Difficulty:** Easy 10%, Medium 55%, Hard 35%
**Top askers:** Google (191), Amazon (181), Microsoft (122), Meta (117), Bloomberg (107)

---

## The Core Insight

Binary search doesn't require a sorted array. It requires a **monotonic predicate** `f(x)`:

```
Index:     0     1     2     3     4     5     6
f(x):    false false false true  true  true  true
                            ^
                         boundary
```

You're finding where the predicate flips. The sorted-array case is just `f(index) = arr[index] >= target`.

**Three categories:**
1. **Classic:** Find target in sorted array → Template 1
2. **Modified:** Rotated arrays, peak finding → Template 1 variant
3. **Search on Answer:** "Minimum X such that condition(X) is true" → Template 3

---

## The Overflow Problem

```java
// WRONG — overflow if left + right > 2.1B:
int mid = (left + right) / 2;

// CORRECT:
int mid = left + (right - left) / 2;

// CORRECT for long answer spaces:
long mid = left + (right - left) / 2;
```

---

## Template 1: Standard Binary Search

**Use:** Exact match, return -1 if not found. Problems: LC 704, LC 33, LC 74.

```java
public int binarySearch(int[] arr, int target) {
    int left = 0, right = arr.length - 1;

    while (left <= right) {  // NOTE: <=, not <
        int mid = left + (right - left) / 2;

        if (arr[mid] == target) return mid;
        else if (arr[mid] < target) left = mid + 1;
        else right = mid - 1;
    }

    return -1;
}
```

**Key rules:**
- `while (left <= right)` — the `=` matters for single-element arrays
- `left = mid + 1`, `right = mid - 1` — NEVER use `mid` alone (causes infinite loop)
- Termination: `left > right`

**Trace on `arr=[1,3,5,7,9]`, target=7:**
```
Step1: left=0, right=4, mid=2 → 5<7 → left=3
Step2: left=3, right=4, mid=3 → 7==7 → return 3
```

**Trace target=6 (not found):**
```
Step1: left=0, right=4, mid=2 → 5<6 → left=3
Step2: left=3, right=4, mid=3 → 7>6 → right=2
Step3: left=3 > right=2 → return -1
```

---

## Template 2: Lower Bound / Upper Bound

**Use:** First/last occurrence, insertion point. Never returns -1. Problems: LC 34, LC 35.

```java
// Lower bound: first index where arr[i] >= target
public int lowerBound(int[] arr, int target) {
    int left = 0, right = arr.length;  // NOTE: right = length, not length-1

    while (left < right) {  // NOTE: <, not <=
        int mid = left + (right - left) / 2;

        if (arr[mid] < target) {
            left = mid + 1;
        } else {
            right = mid;  // NOTE: mid, not mid-1
        }
    }

    return left;  // left == right
}

// Upper bound: first index where arr[i] > target
public int upperBound(int[] arr, int target) {
    int left = 0, right = arr.length;

    while (left < right) {
        int mid = left + (right - left) / 2;

        if (arr[mid] <= target) {
            left = mid + 1;
        } else {
            right = mid;
        }
    }

    return left;
}
```

**Why `right = arr.length`?** If target > all elements, insertion point is `arr.length`.

**Count occurrences:** `upperBound(x) - lowerBound(x)`

**Trace `arr=[1,2,2,2,3,4]`, target=2, lower bound:**
```
left=0,right=6
Step1: mid=3, arr[3]=2>=2 → right=3
Step2: left=0,right=3, mid=1, arr[1]=2>=2 → right=1
Step3: left=0,right=1, mid=0, arr[0]=1<2 → left=1
Step4: left=1,right=1 → return 1 ✓ (first 2 is at index 1)
```

**Trace upper bound:**
```
Step1: mid=3, arr[3]=2<=2 → left=4
Step2: left=4,right=6, mid=5, arr[5]=4>2 → right=5
Step3: left=4,right=5, mid=4, arr[4]=3>2 → right=4
Step4: left=4,right=4 → return 4 ✓
Count of 2s = 4-1 = 3 (indices 1,2,3) ✓
```

---

## Template 3: Binary Search on Answer

**Use:** "Minimum/maximum X such that condition(X) is true." Problems: LC 875, LC 1011, LC 410.

**The four-step method:**
1. Define answer space `[lo, hi]`
2. Write `boolean canAchieve(int x, ...)` — O(n) check
3. Verify monotonicity: if `canAchieve(x)` is true, `canAchieve(x+1)` must also be true
4. Apply the template

```java
// Find MINIMUM: predicate = [false,...,true,true]
public int findMinAnswer(int lo, int hi) {
    while (lo < hi) {
        int mid = lo + (hi - lo) / 2;  // FLOOR

        if (canAchieve(mid)) {
            hi = mid;        // mid works, try smaller
        } else {
            lo = mid + 1;    // mid fails, need bigger
        }
    }
    return lo;
}

// Find MAXIMUM: predicate = [true,true,...,false]
public int findMaxAnswer(int lo, int hi) {
    while (lo < hi) {
        int mid = lo + (hi - lo + 1) / 2;  // CEILING (not floor!)

        if (canAchieve(mid)) {
            lo = mid;        // mid works, try bigger
        } else {
            hi = mid - 1;    // mid fails, need smaller
        }
    }
    return lo;
}
```

**Why ceiling for max search?** Test `lo=3, hi=4`. Floor mid=3. If `canAchieve(3)=true → lo=mid=3`. Same state. Infinite loop. Ceiling mid=4 fixes this.

---

## Template Comparison

| Aspect | Template 1 | Template 2 | Template 3a (min) | Template 3b (max) |
|--------|-----------|-----------|-------------------|-------------------|
| Loop | `left <= right` | `left < right` | `lo < hi` | `lo < hi` |
| right init | `arr.length-1` | `arr.length` | `maxAns` | `maxAns` |
| Mid | floor | floor | floor | CEILING |
| Update on true | return | `right=mid` | `hi=mid` | `lo=mid` |
| Update on false | return | `left=mid+1` | `lo=mid+1` | `hi=mid-1` |

---

## Problem Solutions

### LC 704: Binary Search (Easy)
```java
// Template 1 direct application
public int search(int[] nums, int target) {
    int left = 0, right = nums.length - 1;
    while (left <= right) {
        int mid = left + (right - left) / 2;
        if (nums[mid] == target) return mid;
        else if (nums[mid] < target) left = mid + 1;
        else right = mid - 1;
    }
    return -1;
}
```

### LC 34: Find First and Last Position (Medium)
```java
// Two binary searches: lower bound + upper bound
public int[] searchRange(int[] nums, int target) {
    int first = lowerBound(nums, target);
    if (first == nums.length || nums[first] != target) return new int[]{-1, -1};
    return new int[]{first, upperBound(nums, target) - 1};
}
```

### LC 35: Search Insert Position (Easy)
```java
// Exactly lower bound
public int searchInsert(int[] nums, int target) {
    int left = 0, right = nums.length;
    while (left < right) {
        int mid = left + (right - left) / 2;
        if (nums[mid] < target) left = mid + 1;
        else right = mid;
    }
    return left;
}
```

### LC 74: Search 2D Matrix (Medium)
```java
// Treat m×n matrix as flat sorted array
public boolean searchMatrix(int[][] matrix, int target) {
    int m = matrix.length, n = matrix[0].length;
    int left = 0, right = m * n - 1;
    while (left <= right) {
        int mid = left + (right - left) / 2;
        int value = matrix[mid / n][mid % n];  // index → row/col
        if (value == target) return true;
        else if (value < target) left = mid + 1;
        else right = mid - 1;
    }
    return false;
}
```

### LC 33: Search in Rotated Sorted Array (Medium)
```java
// Key: one half is always sorted. Check which half target falls in.
public int search(int[] nums, int target) {
    int left = 0, right = nums.length - 1;
    while (left <= right) {
        int mid = left + (right - left) / 2;
        if (nums[mid] == target) return mid;

        if (nums[left] <= nums[mid]) {
            // Left half sorted
            if (nums[left] <= target && target < nums[mid]) right = mid - 1;
            else left = mid + 1;
        } else {
            // Right half sorted
            if (nums[mid] < target && target <= nums[right]) left = mid + 1;
            else right = mid - 1;
        }
    }
    return -1;
}
```

### LC 153: Find Minimum in Rotated Array (Medium)
```java
// Compare with right (not left) — handles fully-sorted case
public int findMin(int[] nums) {
    int left = 0, right = nums.length - 1;
    while (left < right) {
        int mid = left + (right - left) / 2;
        if (nums[mid] > nums[right]) left = mid + 1;  // min in right half
        else right = mid;                              // mid could be min
    }
    return nums[left];
}
```

### LC 162: Find Peak Element (Medium)
```java
// Follow the ascending slope — it must lead to a peak
public int findPeakElement(int[] nums) {
    int left = 0, right = nums.length - 1;
    while (left < right) {
        int mid = left + (right - left) / 2;
        if (nums[mid] < nums[mid + 1]) left = mid + 1;  // peak to the right
        else right = mid;                               // peak at mid or left
    }
    return left;
}
```

### LC 875: Koko Eating Bananas (Medium)
```java
// Template 3: minimum speed such that Koko finishes in h hours
public int minEatingSpeed(int[] piles, int h) {
    int lo = 1, hi = 0;
    for (int p : piles) hi = Math.max(hi, p);

    while (lo < hi) {
        int mid = lo + (hi - lo) / 2;
        if (canFinish(mid, piles, h)) hi = mid;
        else lo = mid + 1;
    }
    return lo;
}

private boolean canFinish(int speed, int[] piles, int h) {
    int totalHours = 0;
    for (int pile : piles) {
        totalHours += (pile + speed - 1) / speed;  // ceiling division
        if (totalHours > h) return false;
    }
    return true;
}
```

### LC 1011: Capacity To Ship Packages Within D Days (Medium)
```java
// Template 3: minimum daily capacity
public int shipWithinDays(int[] weights, int days) {
    int lo = 0, hi = 0;
    for (int w : weights) {
        lo = Math.max(lo, w);
        hi += w;
    }

    while (lo < hi) {
        int mid = lo + (hi - lo) / 2;
        if (canShip(mid, weights, days)) hi = mid;
        else lo = mid + 1;
    }
    return lo;
}

private boolean canShip(int capacity, int[] weights, int days) {
    int daysNeeded = 1, currentLoad = 0;
    for (int w : weights) {
        if (currentLoad + w > capacity) {
            daysNeeded++;
            currentLoad = 0;
            if (daysNeeded > days) return false;
        }
        currentLoad += w;
    }
    return true;
}
```

---

## When to Use Binary Search

**Green flags:**
- Sorted array + find target/position → Template 1/2
- "Find first/last position" → Template 2
- "Minimum X such that condition(X) is possible" → Template 3
- Monotonic predicate exists (if X works, X+1 works)

**NOT a fit:**
- Unsorted data without a monotonic predicate
- Need ALL matching elements (binary search finds one boundary)
- Predicate evaluation itself is O(n log n) — total cost becomes worse than brute force

**The monotonicity test:**
```
canAchieve(1) = false
canAchieve(5) = true
canAchieve(10) = true
```
If you ever see `false, true, false` → binary search won't work.

---

## Practice Roadmap

1. **Foundation:** LC 704, LC 35
2. **Range queries:** LC 34
3. **Matrix search:** LC 74
4. **Rotated arrays:** LC 33, LC 153, LC 81
5. **Peak finding:** LC 162
6. **Search on answer:** LC 875, LC 1011, LC 410
7. **Advanced:** LC 1283, LC 1552, LC 1891

**Core skills to internalize:**
- Which template for which problem
- `left + (right - left) / 2` to prevent overflow
- `right = arr.length` for lower/upper bound
- Ceiling vs floor mid for max vs min answer search
