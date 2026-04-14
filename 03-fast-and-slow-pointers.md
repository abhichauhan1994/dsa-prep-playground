# Topic 3: Fast & Slow Pointers (Floyd's Tortoise and Hare)

## Overview

Fast & Slow Pointers is a pattern where two pointers traverse a sequence at different speeds (1 step vs 2 steps). This creates three powerful capabilities:

1. **Cycle detection** — fast laps slow inside a cycle
2. **Midpoint finding** — when fast reaches end, slow is at the middle
3. **Cycle entry detection** — Floyd's Phase 2 finds where the cycle begins

**Top companies:** Amazon, Google, Meta, Microsoft, Bloomberg, Apple

---

## Core Templates

### Template 1: Cycle Detection

```java
public boolean hasCycle(ListNode head) {
    if (head == null || head.next == null) return false;
    ListNode slow = head, fast = head;
    while (fast != null && fast.next != null) {
        slow = slow.next;
        fast = fast.next.next;
        if (slow == fast) return true;
    }
    return false;
}
```

### Template 2: Find Middle

- **Variant A (right-middle):** `while (fast != null && fast.next != null)` — returns second middle for even length
- **Variant B (left-middle):** `while (fast.next != null && fast.next.next != null)` — returns first middle, used for splitting lists

```java
// Variant A - right middle
public ListNode middleNode(ListNode head) {
    ListNode slow = head, fast = head;
    while (fast != null && fast.next != null) {
        slow = slow.next;
        fast = fast.next.next;
    }
    return slow;
}
```

### Template 3: Find Cycle Entry Point (Floyd's Phase 2)

```java
public ListNode detectCycle(ListNode head) {
    if (head == null) return null;
    ListNode slow = head, fast = head;
    // Phase 1: Find meeting point
    while (fast != null && fast.next != null) {
        slow = slow.next;
        fast = fast.next.next;
        if (slow == fast) break;
    }
    if (fast == null) return null;
    // Phase 2: Find entry
    slow = head;
    while (slow != fast) {
        slow = slow.next;
        fast = fast.next;
    }
    return slow;
}
```

### Template 4: Functional Graph Cycle Detection

For problems like Happy Number (LC 202) and Find Duplicate (LC 287).

```java
public int findDuplicate(int[] nums) {
    int slow = 0, fast = 0;
    // Phase 1: Find meeting point
    do {
        slow = nums[slow];
        fast = nums[nums[fast]];
    } while (slow != fast);
    // Phase 2: Find entry
    slow = 0;
    while (slow != fast) {
        slow = nums[slow];
        fast = nums[fast];
    }
    return slow;
}
```

---

## Key Problems

| Problem | Difficulty | Template |
|---------|-----------|----------|
| LC 141 — Linked List Cycle | Easy | 1 |
| LC 142 — Linked List Cycle II | Medium | 3 |
| LC 876 — Middle of Linked List | Easy | 2 |
| LC 234 — Palindrome Linked List | Easy | 2 + reverse |
| LC 143 — Reorder List | Medium | 2 + reverse + merge |
| LC 148 — Sort List | Medium | 2 + merge sort |
| LC 19 — Remove Nth From End | Medium | gap technique |
| LC 202 — Happy Number | Easy | 4 |
| LC 287 — Find Duplicate Number | Medium | 4 + Phase 2 |

---

## Common Mistakes

| Mistake | Fix |
|---------|-----|
| NPE on `fast.next` | Use TWO null checks: `fast != null && fast.next != null` |
| Wrong middle variant | Variant A for right-middle, Variant B for splitting lists |
| Forgetting Phase 2 | After meeting, reset one pointer to head |
| Wrong speed in Phase 2 | Use `fast = fast.next` (speed 1), NOT 2 |
| Using `while (slow != fast)` in Phase 1 | Use `do-while` or advance once first |

---

## Pattern Comparison

**Fast & Slow vs HashSet for cycle detection:**
- Fast & Slow: O(n) time, O(1) space
- HashSet: O(n) time, O(n) space

**Fast & Slow vs Sliding Window (Topic 1):**
- Same direction but different speeds vs same speed with window
- Fast/slow for cycles/midpoints; sliding window for subarrays with constraints

---

## Recognition Signals

**Use fast/slow when problem says:**
- "detect a cycle/loop"
- "find the middle of a linked list"
- "find cycle entry"
- "happy number" or "sequence eventually repeats"
- "find duplicate in [1,n] array"

**Don't use when:**
- Need to know which specific nodes are in the cycle (use HashMap)
- Non-linked list without functional graph structure

---

## Mathematical Proof (Floyd's Phase 2)

Let `a` = distance from head to cycle entry, `b` = distance from entry to meeting point, `c` = cycle length.

After Phase 1: meeting at distance `b` into cycle.

From the equation: `a = k*c - b`

This means distance from head to entry equals distance from meeting point to entry (modulo full cycles). So when both pointers move at speed 1 from head and meeting point, they meet at the cycle entry.
