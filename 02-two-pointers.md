# Topic 2: Two Pointers

Two Pointers is not a single technique. It's a family of four distinct sub-patterns, each solving a different class of problems, all sharing one idea: use two index variables that traverse a data structure in coordinated fashion to eliminate redundant work.

The family includes: converging pointers that close in from both ends, read/write pointers that compact arrays in-place, merge pointers that combine two sorted sequences, and fix-one-plus-sweep that reduces k-sum problems by one dimension.

---

## Table of Contents

1. Core Concept
2. ELI5 — The Intuition
3. When to Use — Recognition Signals
4. Core Templates in Java
5. Real-World Applications
6. Problem Categories and Solutions
7. Common Mistakes and Edge Cases
8. Pattern Comparison
9. Quick Reference Cheat Sheet
10. Practice Roadmap

---

## 1. Core Concept

### What is Two Pointers?

Two pointers is a technique where you maintain two index variables — typically called `left` and `right`, or `read` and `write` — and move them through a data structure according to a set of rules.

### The Four Sub-Patterns

**Sub-pattern 1: Opposite Direction (Converging)**
```
left = 0, right = n-1
Move toward each other until left >= right
```
Used when the array is sorted and you're looking for a pair satisfying some condition.

**Sub-pattern 2: Same Direction — Read/Write**
```
write = 0, read = 0
read scans every element; write only advances when we keep an element
```
Used for in-place array compaction.

**Sub-pattern 3: Fast/Slow (covered in Topic 3)**
Used for cycle detection and finding the middle of a linked list.

**Sub-pattern 4: Two Arrays (Merge)**
```
i = 0 (pointer into array A), j = 0 (pointer into array B)
Advance the pointer pointing to the smaller element
```
Used when merging or comparing two sorted sequences.

**Sub-pattern 5: Fix One + Sweep (k-Sum)**
```
for (int i = 0; i < n; i++) {
    int left = i + 1, right = n - 1;
    while (left < right) { ... }
}
```
Used for 3Sum, 4Sum, and similar problems.

### Time Complexity Improvement

| Problem Type | Brute Force | Two Pointers |
|---|---|---|
| Find pair with target sum (sorted) | O(n^2) | O(n) |
| Find triplet with target sum | O(n^3) | O(n^2) |
| Remove duplicates in-place | O(n^2) with shifting | O(n) |
| Merge two sorted arrays | O(n log n) re-sort | O(n+m) |

---

## 2. Templates in Java

### Template 1: Opposite Direction (Converging)

```java
public int[] twoSumSorted(int[] arr, int target) {
    int left = 0;
    int right = arr.length - 1;

    while (left < right) {
        int sum = arr[left] + arr[right];

        if (sum == target) {
            return new int[]{left + 1, right + 1};
        } else if (sum < target) {
            left++;
        } else {
            right--;
        }
    }

    return new int[]{-1, -1};
}
```

**Key insight:** In a sorted array, if `arr[left] + arr[right]` is too small, moving `left` right increases the sum. If too large, moving `right` left decreases it.

---

### Template 2: Same Direction — Read/Write Pointer

```java
public int removeElement(int[] arr, int val) {
    int write = 0;

    for (int read = 0; read < arr.length; read++) {
        if (arr[read] != val) {
            arr[write] = arr[read];
            write++;
        }
    }

    return write;
}
```

**Key insight:** The `read` pointer scans everything. The `write` pointer only advances when we keep an element.

---

### Template 3: Merge Two Sorted Arrays

```java
public int[] mergeSorted(int[] A, int[] B) {
    int i = 0, j = 0, k = 0;
    int[] result = new int[A.length + B.length];

    while (i < A.length && j < B.length) {
        if (A[i] <= B[j]) {
            result[k++] = A[i++];
        } else {
            result[k++] = B[j++];
        }
    }

    while (i < A.length) result[k++] = A[i++];
    while (j < B.length) result[k++] = B[j++];

    return result;
}
```

---

### Template 4: Fix One + Two Pointer Sweep (3Sum)

```java
public List<List<Integer>> threeSum(int[] nums) {
    Arrays.sort(nums);
    List<List<Integer>> result = new ArrayList<>();

    for (int i = 0; i < nums.length - 2; i++) {
        if (i > 0 && nums[i] == nums[i - 1]) continue;
        if (nums[i] > 0) break;

        int left = i + 1;
        int right = nums.length - 1;

        while (left < right) {
            int sum = nums[i] + nums[left] + nums[right];

            if (sum == 0) {
                result.add(Arrays.asList(nums[i], nums[left], nums[right]));
                while (left < right && nums[left] == nums[left + 1]) left++;
                while (left < right && nums[right] == nums[right - 1]) right--;
                left++;
                right--;
            } else if (sum < 0) {
                left++;
            } else {
                right--;
            }
        }
    }

    return result;
}
```

---

## 3. When to Use — Recognition Signals

### Opposite Direction:
- "sorted array" + "find pair" + "target sum"
- "palindrome check"
- "container with most water"

### Same Direction (Read/Write):
- "in-place" modification required
- "remove duplicates"
- "move zeros to end"

### Merge (Two Arrays):
- "merge two sorted arrays/lists"
- "intersection of two sorted arrays"

### Fix + Sweep (k-Sum):
- "3Sum", "4Sum", "triplets", "quadruplets"

### When NOT to Use Two Pointers:
- Use **HashMap** when array is unsorted and you need O(n)
- Use **Binary Search** when you need O(log n) per query
- Use **Backtracking** when you need ALL combinations

---

## 4. Problem Solutions

### LC 167 — Two Sum II (Easy)
```java
public int[] twoSum(int[] numbers, int target) {
    int left = 0;
    int right = numbers.length - 1;

    while (left < right) {
        int sum = numbers[left] + numbers[right];
        if (sum == target) {
            return new int[]{left + 1, right + 1};
        } else if (sum < target) {
            left++;
        } else {
            right--;
        }
    }

    return new int[]{-1, -1};
}
```

### LC 283 — Move Zeroes (Easy)
```java
public void moveZeroes(int[] nums) {
    int write = 0;

    for (int read = 0; read < nums.length; read++) {
        if (nums[read] != 0) {
            nums[write] = nums[read];
            write++;
        }
    }

    while (write < nums.length) {
        nums[write] = 0;
        write++;
    }
}
```

### LC 26 — Remove Duplicates from Sorted Array (Easy)
```java
public int removeDuplicates(int[] nums) {
    if (nums.length == 0) return 0;

    int write = 1;
    for (int read = 1; read < nums.length; read++) {
        if (nums[read] != nums[read - 1]) {
            nums[write] = nums[read];
            write++;
        }
    }

    return write;
}
```

### LC 11 — Container With Most Water (Medium)
```java
public int maxArea(int[] height) {
    int left = 0;
    int right = height.length - 1;
    int maxArea = 0;

    while (left < right) {
        int width = right - left;
        int h = Math.min(height[left], height[right]);
        maxArea = Math.max(maxArea, width * h);

        if (height[left] < height[right]) {
            left++;
        } else {
            right--;
        }
    }

    return maxArea;
}
```

### LC 15 — 3Sum (Medium)
```java
public List<List<Integer>> threeSum(int[] nums) {
    Arrays.sort(nums);
    List<List<Integer>> result = new ArrayList<>();

    for (int i = 0; i < nums.length - 2; i++) {
        if (i > 0 && nums[i] == nums[i - 1]) continue;
        if (nums[i] > 0) break;

        int left = i + 1;
        int right = nums.length - 1;

        while (left < right) {
            int sum = nums[i] + nums[left] + nums[right];

            if (sum == 0) {
                result.add(Arrays.asList(nums[i], nums[left], nums[right]));
                while (left < right && nums[left] == nums[left + 1]) left++;
                while (left < right && nums[right] == nums[right - 1]) right--;
                left++;
                right--;
            } else if (sum < 0) {
                left++;
            } else {
                right--;
            }
        }
    }

    return result;
}
```

### LC 42 — Trapping Rain Water (Hard)
```java
public int trap(int[] height) {
    int left = 0, right = height.length - 1;
    int leftMax = 0, rightMax = 0;
    int water = 0;

    while (left < right) {
        if (height[left] < height[right]) {
            if (height[left] >= leftMax) {
                leftMax = height[left];
            } else {
                water += leftMax - height[left];
            }
            left++;
        } else {
            if (height[right] >= rightMax) {
                rightMax = height[right];
            } else {
                water += rightMax - height[right];
            }
            right--;
        }
    }

    return water;
}
```

### LC 75 — Sort Colors (Medium)
```java
public void sortColors(int[] nums) {
    int left = 0;
    int right = nums.length - 1;
    int i = 0;

    while (i <= right) {
        if (nums[i] == 0) {
            swap(nums, i, left);
            left++;
            i++;
        } else if (nums[i] == 2) {
            swap(nums, i, right);
            right--;
        } else {
            i++;
        }
    }
}

private void swap(int[] nums, int i, int j) {
    int temp = nums[i];
    nums[i] = nums[j];
    nums[j] = temp;
}
```

---

## 5. Common Mistakes

1. **Forgetting to sort** before using two pointers on sorted arrays
2. **Not handling duplicates** in 3Sum/4Sum problems
3. **Off-by-one errors** — watch your loop conditions (`left < right` vs `left <= right`)
4. **Wrong pointer movement** — increase vs decrease based on comparison result
5. **Not using HashMap** when array is unsorted

---

## 6. Quick Reference Cheat Sheet

| Problem Type | Template | Key |
|-------------|---------|-----|
| Two Sum (sorted) | Opposite Direction | `left++` if sum too small |
| Remove Element | Read/Write | write only advances on keep |
| 3Sum | Fix One + Sweep | Skip duplicates carefully |
| Container Water | Opposite + greedy | Move smaller pointer |
| Move Zeros | Read/Write | Same as remove element |
| Merge Sorted | Merge | Pick smaller front element |

---

## 7. Practice Roadmap

| Day | Problem | Difficulty | Focus |
|-----|---------|------------|-------|
| 1 | LC 344 (Reverse String) | Easy | Opposite direction basics |
| 2 | LC 125 (Valid Palindrome) | Easy | Character filtering |
| 3 | LC 26 (Remove Duplicates) | Easy | Read/write pointer |
| 4 | LC 283 (Move Zeros) | Easy | Read/write variant |
| 5 | LC 167 (Two Sum II) | Medium | Canonical pair sum |
| 6 | LC 11 (Container Water) | Medium | Greedy insight |
| 7 | LC 75 (Sort Colors) | Medium | Three pointers |
| 8 | LC 15 (3Sum) | Medium | Duplicate handling! |
| 9 | LC 88 (Merge Sorted) | Easy | Merge from end |
| 10 | LC 42 (Trapping Rain) | Hard | Two pointer greedy |

**Checkpoint:** You know when to use opposite-direction, read/write, merge, or fix+sweep. You can handle duplicate skipping in 3Sum without hesitation.