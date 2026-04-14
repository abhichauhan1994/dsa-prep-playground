# Topic 11: Trees — BFS (Level-Order Traversal)

*Document 11 of 20 in the FAANG DSA Prep series.*

BFS processes trees level by level using a queue (FIFO). The "snapshot size" technique is the backbone of every BFS tree problem.

**Top askers:** Google, Amazon, Meta, Microsoft, Bloomberg

---

## Key Template: Snapshot the Queue Size

```java
// Process one level at a time
while (!queue.isEmpty()) {
    int size = queue.size();  // snapshot: nodes on THIS level
    for (int i = 0; i < size; i++) {
        TreeNode node = queue.poll();
        // process node (it belongs to current level)
        if (node.left != null) queue.offer(node.left);
        if (node.right != null) queue.offer(node.right);
    }
    // after inner loop, queue holds exactly the next level's nodes
}
```

---

## When to Use BFS vs DFS

| Use BFS | Use DFS |
|---|---|
| Level/layer/breadth problems | Path/depth/structure problems |
| Level order traversal | Path sum |
| Right side view | BST validation |
| Zigzag traversal | LCA |
| Minimum depth | Height/diameter |
| Average per level | Serialization |
| Connect nodes at same level | Subtree |

---

## Templates

### Template 1: Level Order Traversal (List of Lists)
```java
public List<List<Integer>> levelOrder(TreeNode root) {
    List<List<Integer>> result = new ArrayList<>();
    if (root == null) return result;
    Queue<TreeNode> queue = new LinkedList<>();
    queue.offer(root);
    while (!queue.isEmpty()) {
        int size = queue.size();
        List<Integer> level = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            TreeNode node = queue.poll();
            level.add(node.val);
            if (node.left != null) queue.offer(node.left);
            if (node.right != null) queue.offer(node.right);
        }
        result.add(level);
    }
    return result;
}
```

### Template 2: Right Side View (last node per level)
```java
public List<Integer> rightSideView(TreeNode root) {
    List<Integer> result = new ArrayList<>();
    if (root == null) return result;
    Queue<TreeNode> queue = new LinkedList<>();
    queue.offer(root);
    while (!queue.isEmpty()) {
        int size = queue.size();
        for (int i = 0; i < size; i++) {
            TreeNode node = queue.poll();
            if (i == size - 1) result.add(node.val);  // last = rightmost
            if (node.left != null) queue.offer(node.left);
            if (node.right != null) queue.offer(node.right);
        }
    }
    return result;
}
```

### Template 3: Zigzag Level Order (alternate direction)
```java
public List<List<Integer>> zigzagLevelOrder(TreeNode root) {
    List<List<Integer>> result = new ArrayList<>();
    if (root == null) return result;
    Queue<TreeNode> queue = new LinkedList<>();
    queue.offer(root);
    boolean leftToRight = true;
    while (!queue.isEmpty()) {
        int size = queue.size();
        List<Integer> level = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            TreeNode node = queue.poll();
            if (leftToRight) level.add(node.val);
            else level.add(0, node.val);  // add to front
            if (node.left != null) queue.offer(node.left);
            if (node.right != null) queue.offer(node.right);
        }
        result.add(level);
        leftToRight = !leftToRight;
    }
    return result;
}
```

### Template 4: Minimum Depth (BFS finds it faster)
```java
// BFS returns at first leaf — faster than DFS
public int minDepth(TreeNode root) {
    if (root == null) return 0;
    Queue<TreeNode> queue = new LinkedList<>();
    queue.offer(root);
    int depth = 0;
    while (!queue.isEmpty()) {
        depth++;
        int size = queue.size();
        for (int i = 0; i < size; i++) {
            TreeNode node = queue.poll();
            if (node.left == null && node.right == null) return depth;
            if (node.left != null) queue.offer(node.left);
            if (node.right != null) queue.offer(node.right);
        }
    }
    return depth;
}
```

---

## Problem Solutions

### LC 102: Binary Tree Level Order Traversal (Medium)
```java
// Template 1
public List<List<Integer>> levelOrder(TreeNode root) {
    List<List<Integer>> result = new ArrayList<>();
    Queue<TreeNode> q = new LinkedList<>();
    q.offer(root);
    while (!q.isEmpty() && root != null) {
        int size = q.size();
        List<Integer> level = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            TreeNode node = q.poll();
            level.add(node.val);
            if (node.left != null) q.offer(node.left);
            if (node.right != null) q.offer(node.right);
        }
        result.add(level);
    }
    return result;
}
```

### LC 103: Binary Tree Zigzag Level Order (Medium)
```java
// Template 3
public List<List<Integer>> zigzagLevelOrder(TreeNode root) {
    List<List<Integer>> result = new ArrayList<>();
    if (root == null) return result;
    Queue<TreeNode> q = new LinkedList<>();
    q.offer(root);
    boolean leftToRight = true;
    while (!q.isEmpty()) {
        int size = q.size();
        List<Integer> level = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            TreeNode node = q.poll();
            if (leftToRight) level.add(node.val);
            else level.add(0, node.val);
            if (node.left != null) q.offer(node.left);
            if (node.right != null) q.offer(node.right);
        }
        result.add(level);
        leftToRight = !leftToRight;
    }
    return result;
}
```

### LC 199: Binary Tree Right Side View (Medium)
```java
// Template 2
public List<Integer> rightSideView(TreeNode root) {
    List<Integer> result = new ArrayList<>();
    if (root == null) return result;
    Queue<TreeNode> q = new LinkedList<>();
    q.offer(root);
    while (!q.isEmpty()) {
        int size = q.size();
        for (int i = 0; i < size; i++) {
            TreeNode node = q.poll();
            if (i == size - 1) result.add(node.val);
            if (node.left != null) q.offer(node.left);
            if (node.right != null) q.offer(node.right);
        }
    }
    return result;
}
```

### LC 111: Minimum Depth of Binary Tree (Easy)
```java
// Template 4: BFS finds first leaf (shallowest)
public int minDepth(TreeNode root) {
    if (root == null) return 0;
    Queue<TreeNode> q = new LinkedList<>();
    q.offer(root);
    int depth = 0;
    while (!q.isEmpty()) {
        depth++;
        int size = q.size();
        for (int i = 0; i < size; i++) {
            TreeNode node = q.poll();
            if (node.left == null && node.right == null) return depth;
            if (node.left != null) q.offer(node.left);
            if (node.right != null) q.offer(node.right);
        }
    }
    return depth;
}
```

### LC 637: Average of Levels in Binary Tree (Easy)
```java
public List<Double> averageOfLevels(TreeNode root) {
    List<Double> result = new ArrayList<>();
    Queue<TreeNode> q = new LinkedList<>();
    q.offer(root);
    while (!q.isEmpty()) {
        int size = q.size();
        long sum = 0;
        for (int i = 0; i < size; i++) {
            TreeNode node = q.poll();
            sum += node.val;
            if (node.left != null) q.offer(node.left);
            if (node.right != null) q.offer(node.right);
        }
        result.add((double) sum / size);
    }
    return result;
}
```

### LC 116: Populating Next Right Pointers (Medium)
```java
// Connect nodes at same level using next pointer
public Node connect(Node root) {
    if (root == null) return root;
    Queue<Node> q = new LinkedList<>();
    q.offer(root);
    while (!q.isEmpty()) {
        int size = q.size();
        Node prev = null;
        for (int i = 0; i < size; i++) {
            Node node = q.poll();
            if (prev != null) prev.next = node;
            prev = node;
            if (node.left != null) q.offer(node.left);
            if (node.right != null) q.offer(node.right);
        }
    }
    return root;
}
```

---

## Practice Roadmap

1. **Foundation:** LC 102, LC 107, LC 111
2. **Intermediate:** LC 103, LC 199, LC 637
3. **Advanced:** LC 116, LC 117, LC 662 (width)
