# Topic 9: Linked List Patterns

*Document 9 of 20 in the FAANG DSA Prep series.*

Linked lists test pointer manipulation: no indexing, must hold references and update in exact order. Five core patterns cover most interview problems.

**Top askers:** Amazon, Google, Meta, Microsoft, Bloomberg

---

## Core Patterns

1. **In-Place Reversal** — full, partial, k-groups
2. **Merge / Weave** — merge two or k sorted lists
3. **Partition / Rearrange**
4. **Dummy Node** — simplifies all head operations
5. **Deep Copy** — clone with random pointers

---

## Templates

### Template 1: Full List Reversal (Iterative)
```java
public ListNode reverseList(ListNode head) {
    ListNode prev = null, curr = head;
    while (curr != null) {
        ListNode next = curr.next;  // SAVE before breaking
        curr.next = prev;
        prev = curr;
        curr = next;
    }
    return prev;  // prev is new head
}
```

**Trace [1→2→3]:**
```
Step1: next=2, 1->null, prev=1, curr=2
Step2: next=3, 2->1, prev=2, curr=3
Step3: next=null, 3->2, prev=3, curr=null
Return 3 → [3→2→1]
```

### Template 2: Reverse Between m and n
```java
public ListNode reverseBetween(ListNode head, int left, int right) {
    ListNode dummy = new ListNode(0);
    dummy.next = head;
    ListNode prev = dummy;
    for (int i = 1; i < left; i++) prev = prev.next;
    ListNode curr = prev.next;
    for (int i = 0; i < right - left; i++) {
        ListNode next = curr.next;
        curr.next = next.next;
        next.next = prev.next;
        prev.next = next;
    }
    return dummy.next;
}
```

### Template 3: Merge Two Sorted Lists
```java
public ListNode mergeTwoLists(ListNode l1, ListNode l2) {
    ListNode dummy = new ListNode(0), curr = dummy;
    while (l1 != null && l2 != null) {
        if (l1.val <= l2.val) { curr.next = l1; l1 = l1.next; }
        else { curr.next = l2; l2 = l2.next; }
        curr = curr.next;
    }
    curr.next = (l1 != null) ? l1 : l2;
    return dummy.next;
}
```

### Template 4: Merge K Sorted Lists (Priority Queue)
```java
public ListNode mergeKLists(ListNode[] lists) {
    PriorityQueue<ListNode> pq = new PriorityQueue<>(Comparator.comparingInt(n -> n.val));
    for (ListNode node : lists) if (node != null) pq.offer(node);
    ListNode dummy = new ListNode(0), curr = dummy;
    while (!pq.isEmpty()) {
        curr.next = pq.poll();
        curr = curr.next;
        if (curr.next != null) pq.offer(curr.next);
    }
    return dummy.next;
}
```

### Template 5: Deep Copy with Random Pointers
```java
// 3-pass: copy values, set random pointers via map, reconnect next
public ListNode copyRandomList(Node head) {
    if (head == null) return null;
    Map<Node, Node> map = new HashMap<>();
    Node curr = head;
    while (curr != null) { map.put(curr, new Node(curr.val)); curr = curr.next; }
    curr = head;
    while (curr != null) {
        map.get(curr).next = map.get(curr.next);
        map.get(curr).random = map.get(curr.random);
        curr = curr.next;
    }
    return map.get(head);
}
```

### Template 6: Add Two Numbers
```java
// Each node stores one digit (reversed order)
public ListNode addTwoNumbers(ListNode l1, ListNode l2) {
    ListNode dummy = new ListNode(0), curr = dummy;
    int carry = 0;
    while (l1 != null || l2 != null || carry > 0) {
        int sum = carry + (l1 != null ? l1.val : 0) + (l2 != null ? l2.val : 0);
        carry = sum / 10;
        curr.next = new ListNode(sum % 10);
        curr = curr.next;
        if (l1 != null) l1 = l1.next;
        if (l2 != null) l2 = l2.next;
    }
    return dummy.next;
}
```

---

## Problem Solutions

### LC 206: Reverse Linked List (Easy)
```java
// Template 1
public ListNode reverseList(ListNode head) {
    ListNode prev = null, curr = head;
    while (curr != null) {
        ListNode next = curr.next;
        curr.next = prev;
        prev = curr;
        curr = next;
    }
    return prev;
}
```

### LC 21: Merge Two Sorted Lists (Easy)
```java
// Template 3
public ListNode mergeTwoLists(ListNode l1, ListNode l2) {
    ListNode dummy = new ListNode(0), curr = dummy;
    while (l1 != null && l2 != null) {
        if (l1.val <= l2.val) { curr.next = l1; l1 = l1.next; }
        else { curr.next = l2; l2 = l2.next; }
        curr = curr.next;
    }
    curr.next = (l1 != null) ? l1 : l2;
    return dummy.next;
}
```

### LC 19: Remove Nth Node From End (Medium)
```java
// Two pointers: advance fast by n+1, then move both
public ListNode removeNthFromEnd(ListNode head, int n) {
    ListNode dummy = new ListNode(0);
    dummy.next = head;
    ListNode fast = dummy, slow = dummy;
    for (int i = 0; i <= n; i++) fast = fast.next;
    while (fast != null) { fast = fast.next; slow = slow.next; }
    slow.next = slow.next.next;
    return dummy.next;
}
```

### LC 92: Reverse Linked List II (Medium)
```java
// Template 2
public ListNode reverseBetween(ListNode head, int left, int right) {
    ListNode dummy = new ListNode(0);
    dummy.next = head;
    ListNode prev = dummy;
    for (int i = 1; i < left; i++) prev = prev.next;
    ListNode curr = prev.next;
    for (int i = 0; i < right - left; i++) {
        ListNode next = curr.next;
        curr.next = next.next;
        next.next = prev.next;
        prev.next = next;
    }
    return dummy.next;
}
```

### LC 24: Swap Nodes in Pairs (Medium)
```java
// Dummy node, then swap pairs iteratively
public ListNode swapPairs(ListNode head) {
    ListNode dummy = new ListNode(0);
    dummy.next = head;
    ListNode prev = dummy;
    while (prev.next != null && prev.next.next != null) {
        ListNode first = prev.next;
        ListNode second = first.next;
        first.next = second.next;
        second.next = first;
        prev.next = second;
        prev = first;
    }
    return dummy.next;
}
```

### LC 25: Reverse Nodes in K-Group (Hard)
```java
// Count k nodes, reverse them, reconnect, repeat
public ListNode reverseKGroup(ListNode head, int k) {
    ListNode dummy = new ListNode(0);
    dummy.next = head;
    ListNode prevGroup = dummy;
    while (true) {
        ListNode kth = prevGroup;
        for (int i = 0; i < k && kth != null; i++) kth = kth.next;
        if (kth == null) break;
        ListNode groupStart = prevGroup.next;
        ListNode nextGroupStart = kth.next;
        kth.next = null;
        prevGroup.next = reverseList(groupStart);
        groupStart.next = nextGroupStart;
        prevGroup = groupStart;
    }
    return dummy.next;
}
```

### LC 23: Merge K Sorted Lists (Hard)
```java
// Template 4: priority queue
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

### LC 328: Odd Even Linked List (Medium)
```java
// Separate odd and even, then connect
public ListNode oddEvenList(ListNode head) {
    if (head == null) return head;
    ListNode odd = head, even = head.next, evenHead = even;
    while (even != null && even.next != null) {
        odd.next = even.next;
        odd = odd.next;
        even.next = odd.next;
        even = even.next;
    }
    odd.next = evenHead;
    return head;
}
```

### LC 138: Copy List with Random Pointer (Medium)
```java
// Template 5: 3-pass with HashMap
public Node copyRandomList(Node head) {
    Map<Node, Node> map = new HashMap<>();
    Node curr = head;
    while (curr != null) { map.put(curr, new Node(curr.val)); curr = curr.next; }
    curr = head;
    while (curr != null) {
        map.get(curr).next = map.get(curr.next);
        map.get(curr).random = map.get(curr.random);
        curr = curr.next;
    }
    return map.get(head);
}
```

### LC 2: Add Two Numbers (Medium)
```java
// Template 6
public ListNode addTwoNumbers(ListNode l1, ListNode l2) {
    ListNode dummy = new ListNode(0), curr = dummy;
    int carry = 0;
    while (l1 != null || l2 != null || carry > 0) {
        int sum = carry + (l1 != null ? l1.val : 0) + (l2 != null ? l2.val : 0);
        curr.next = new ListNode(sum % 10);
        carry = sum / 10;
        curr = curr.next;
        if (l1 != null) l1 = l1.next;
        if (l2 != null) l2 = l2.next;
    }
    return dummy.next;
}
```

### LC 160: Intersection of Two Linked Lists (Easy)
```java
// Two pointers: traverse both, swap at end
public ListNode getIntersectionNode(ListNode headA, ListNode headB) {
    ListNode a = headA, b = headB;
    while (a != b) {
        a = (a == null) ? headB : a.next;
        b = (b == null) ? headA : b.next;
    }
    return a;
}
```

---

## Practice Roadmap

1. **Foundation:** LC 206, LC 21, LC 160, LC 234
2. **Manipulation:** LC 19, LC 92, LC 24
3. **Advanced:** LC 25, LC 23, LC 138
4. **Expert:** LC 143, LC 61, LC 86
