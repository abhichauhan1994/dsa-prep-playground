# Topic 5: Prefix Sum

*Document 5 of 20 in the FAANG DSA Prep series.*

Prefix sum trades O(n) space for O(1) range queries. The key extension: **Prefix Sum + HashMap** solves "count subarrays with sum = k" in O(n) including arrays with negatives — sliding window can't do this.

**Top askers:** Google, Amazon, Meta, Microsoft, Bloomberg

---

## Core Concept

### Basic Prefix Sum

```java
// prefix[i] = sum of first i elements (arr[0..i-1])
// prefix[0] = 0 (empty prefix)
// prefix has size n+1
// Range sum query: sum(l, r) = prefix[r+1] - prefix[l]
```

### Prefix Sum + HashMap

A subarray `arr[l..r]` has sum `k` iff:
```
prefixSum[r+1] - prefixSum[l] = k
→ prefixSum[l] = prefixSum[r+1] - k
```

So for each position, we need to know how many previous prefix sums equal `currentPrefix - k`. HashMap stores frequencies.

```java
// Count subarrays with sum = k
public int countSubarraysWithSumK(int[] arr, int k) {
    Map<Long, Integer> map = new HashMap<>();
    map.put(0L, 1);  // CRITICAL: empty prefix sum 0 exists once

    long prefixSum = 0;
    int count = 0;

    for (int num : arr) {
        prefixSum += num;
        count += map.getOrDefault(prefixSum - k, 0);  // look for matching prefix
        map.put(prefixSum, map.getOrDefault(prefixSum, 0) + 1);
    }

    return count;
}
```

**Why `map.put(0, 1)`?** For subarray starting at index 0: when `prefixSum = k`, we look for `k - k = 0`. If 0 isn't in the map, we miss it.

---

## When to Use

| Problem Type | Tool |
|---|---|
| Range sum queries (multiple) | Basic prefix sum |
| Count subarrays with sum = k | Prefix sum + HashMap |
| Arrays with negatives + sum | Prefix sum + HashMap (sliding window fails) |
| Range addition updates | Difference array |
| 2D range queries | 2D prefix sum |
| Longest subarray with sum = k | Prefix sum + HashMap (store earliest index) |

**Key rule:** All positives → sliding window simpler. Any negatives → must use prefix sum + HashMap.

---

## Templates

### Template 1: Basic Prefix Sum
```java
long[] prefix = new long[n + 1];
prefix[0] = 0;
for (int i = 0; i < n; i++) prefix[i + 1] = prefix[i] + arr[i];

// Query sum(l, r) inclusive:
long sum = prefix[r + 1] - prefix[l];
```

### Template 2: Prefix Sum + HashMap (Count)
```java
// Count subarrays with sum = k
Map<Long, Integer> map = new HashMap<>();
map.put(0L, 1);
long prefix = 0;
int count = 0;
for (int num : arr) {
    prefix += num;
    count += map.getOrDefault(prefix - k, 0);
    map.put(prefix, map.getOrDefault(prefix, 0) + 1);
}
return count;
```

### Template 3: Prefix Sum + HashMap (Longest)
```java
// Longest subarray with sum = k — store earliest index
Map<Long, Integer> map = new HashMap<>();
map.put(0L, -1);  // sum 0 seen at virtual index -1
long prefix = 0;
int longest = 0;
for (int i = 0; i < n; i++) {
    prefix += arr[i];
    if (map.containsKey(prefix - k)) {
        longest = Math.max(longest, i - map.get(prefix - k));
    }
    if (!map.containsKey(prefix)) {
        map.put(prefix, i);  // only store first occurrence for longest
    }
}
return longest;
```

### Template 4: Difference Array (Range Updates)
```java
// Add val to range [l, r] for all queries, then prefix sum to get final array
int[] diff = new int[n + 1];  // n = array length
void addRange(int l, int r, int val) {
    diff[l] += val;
    diff[r + 1] -= val;
}
// After all updates:
for (int i = 1; i < n; i++) diff[i] += diff[i - 1];
// diff[i] now holds the final value at index i
```

### Template 5: 2D Prefix Sum
```java
// Build
int[][] prefix = new int[m + 1][n + 1];
for (int i = 1; i <= m; i++)
    for (int j = 1; j <= n; j++)
        prefix[i][j] = prefix[i-1][j] + prefix[i][j-1] - prefix[i-1][j-1] + matrix[i-1][j-1];

// Query rectangle (r1,c1) to (r2,c2) inclusive:
int sum = prefix[r2+1][c2+1] - prefix[r1][c2+1] - prefix[r2+1][c1] + prefix[r1][c1];
```

---

## Problem Solutions

### LC 560: Subarray Sum Equals K (Medium)
```java
// Template 2: count subarrays with sum = k
public int subarraySum(int[] nums, int k) {
    Map<Integer, Integer> map = new HashMap<>();
    map.put(0, 1);
    int prefix = 0, count = 0;
    for (int num : nums) {
        prefix += num;
        count += map.getOrDefault(prefix - k, 0);
        map.put(prefix, map.getOrDefault(prefix, 0) + 1);
    }
    return count;
}
```

### LC 974: Subarray Sums Divisible by K (Medium)
```java
// Count subarrays where (prefix % K) is same as previous
public int subarraysDivByK(int[] nums, int k) {
    Map<Integer, Integer> map = new HashMap<>();
    map.put(0, 1);  // prefix % k = 0 seen once
    int prefix = 0, count = 0;
    for (int num : nums) {
        prefix = (prefix + num) % k;
        if (prefix < 0) prefix += k;  // Java negative modulo fix
        count += map.getOrDefault(prefix, 0);
        map.put(prefix, map.getOrDefault(prefix, 0) + 1);
    }
    return count;
}
```

### LC 523: Continuous Subarray Sum (Medium)
```java
// Same as LC 974 but check subarray length >= 2
public boolean checkSubarraySum(int[] nums, int k) {
    Map<Integer, Integer> map = new HashMap<>();
    map.put(0, -1);
    int prefix = 0;
    for (int i = 0; i < nums.length; i++) {
        prefix = (prefix + nums[i]) % k;
        if (prefix < 0) prefix += k;
        if (map.containsKey(prefix)) {
            if (i - map.get(prefix) > 1) return true;  // length >= 2
        } else {
            map.put(prefix, i);
        }
    }
    return false;
}
```

### LC 238: Product of Array Except Self (Medium)
```java
// Prefix product left and right (no division, no extra space)
public int[] productExceptSelf(int[] nums) {
    int n = nums.length;
    int[] result = new int[n];
    int prefix = 1;
    for (int i = 0; i < n; i++) {
        result[i] = prefix;
        prefix *= nums[i];
    }
    int suffix = 1;
    for (int i = n - 1; i >= 0; i--) {
        result[i] *= suffix;
        suffix *= nums[i];
    }
    return result;
}
```

### LC 724: Find Pivot Index (Easy)
```java
// Left sum = right sum
public int pivotIndex(int[] nums) {
    int total = Arrays.stream(nums).sum();
    int leftSum = 0;
    for (int i = 0; i < nums.length; i++) {
        if (leftSum == total - leftSum - nums[i]) return i;
        leftSum += nums[i];
    }
    return -1;
}
```

### LC 1314: Matrix Block Sum (Medium)
```java
// 2D prefix sum then query each cell
public int[][] matrixBlockSum(int[][] mat, int k) {
    int m = mat.length, n = mat[0].length;
    int[][] prefix = new int[m + 1][n + 1];
    for (int i = 1; i <= m; i++)
        for (int j = 1; j <= n; j++)
            prefix[i][j] = prefix[i-1][j] + prefix[i][j-1] - prefix[i-1][j-1] + mat[i-1][j-1];

    int[][] result = new int[m][n];
    for (int i = 0; i < m; i++) {
        for (int j = 0; j < n; j++) {
            int r1 = Math.max(0, i - k) + 1;
            int c1 = Math.max(0, j - k) + 1;
            int r2 = Math.min(m - 1, i + k) + 1;
            int c2 = Math.min(n - 1, j + k) + 1;
            result[i][j] = prefix[r2][c2] - prefix[r1 - 1][c2] - prefix[r2][c1 - 1] + prefix[r1 - 1][c1 - 1];
        }
    }
    return result;
}
```

### LC 1109: Corporate Flight Bookings (Medium)
```java
// Difference array for range updates
public int[] corpFlightBookings(int[][] bookings, int n) {
    int[] diff = new int[n];
    for (int[] booking : bookings) {
        int first = booking[0] - 1;
        int last = booking[1] - 1;
        int seats = booking[2];
        diff[first] += seats;
        if (last + 1 < n) diff[last + 1] -= seats;
    }
    for (int i = 1; i < n; i++) diff[i] += diff[i - 1];
    return diff;
}
```

### LC 525: Contiguous Array (Medium)
```java
// Transform 0→-1, then find longest subarray with sum = 0
public int findMaxLength(int[] nums) {
    Map<Integer, Integer> map = new HashMap<>();
    map.put(0, -1);
    int prefix = 0, longest = 0;
    for (int i = 0; i < nums.length; i++) {
        prefix += (nums[i] == 0) ? -1 : 1;
        if (map.containsKey(prefix)) {
            longest = Math.max(longest, i - map.get(prefix));
        } else {
            map.put(prefix, i);
        }
    }
    return longest;
}
```

---

## Common Mistakes

1. **Forgetting `map.put(0, 1)`** — misses subarrays starting at index 0
2. **Using sliding window on arrays with negatives** — window can't shrink/expand correctly
3. **`prefix[r] - prefix[l-1]` vs `prefix[r+1] - prefix[l]`** — both work but pick one and be consistent
4. **Integer overflow** — use `long` for prefix sums when values can be large
5. **Negative modulo** — `(prefix % k + k) % k` to handle Java's negative modulo

---

## Practice Roadmap

1. **Foundation:** LC 560 (count), LC 724 (pivot), LC 238 (product except self)
2. **Variations:** LC 523, LC 974 (modulo), LC 525 (transform)
3. **Advanced:** LC 1314 (2D), LC 1109 (difference array)
4. **Master:** LC 1074 (2D submatrix sum), LC 437 (path sum III with prefix)
