# Topic 10: Trees — DFS (Depth-First Search)

*Document 10 of 20 in the FAANG DSA Prep series.*

Trees appear in ~25% of FAANG interviews. DFS explores as deep as possible before backtracking. Three traversal orders differ only in when you process the current node.

**Top askers:** Google, Amazon, Meta, Microsoft, Bloomberg

---

## The Three Traversal Orders

```
        4
       / \
      2   6
     / \ / \
    1  3 5  7

Inorder   (L-Root-R): 1, 2, 3, 4, 5, 6, 7  ← BST sorted order
Preorder  (Root-L-R):  4, 2, 1, 3, 6, 5, 7  ← root first, serialization
Postorder (L-R-Root):  1, 3, 2, 5, 7, 6, 4  ← children before parent
```

**When to use each:**
- **Inorder:** BST sorted sequence, kth smallest, validate BST
- **Preorder:** Serialization, tree construction, copy tree
- **Postorder:** Delete tree, compute height/diameter, any bottom-up computation

---

## Two Core Patterns

### Pattern 1: Top-Down (pass info DOWN via parameters)

Carry context from parent to child. Use for path sum, min/max bounds.

```java
void dfs(TreeNode node, int currentSum, List<Integer> path) {
    if (node == null) return;
    currentSum += node.val;
    path.add(node.val);
    dfs(node.left, currentSum, path);
    dfs(node.right, currentSum, path);
    path.remove(path.size() - 1); // backtrack
}
```

### Pattern 2: Bottom-Up (return info UP via return value)

Each call returns subtree info to parent. Use for height, diameter, max path sum.

```java
int dfs(TreeNode node) {
    if (node == null) return 0;
    int left = dfs(node.left);
    int right = dfs(node.right);
    // combine left + right + node
    return 1 + Math.max(left, right);
}
```

### Pattern 3: Global Variable (cross-branch answers)

Return single-branch value, update class variable with both branches. Use for diameter, max path sum.

```java
int answer = 0;
int dfs(TreeNode node) {
    if (node == null) return 0;
    int left = dfs(node.left);
    int right = dfs(node.right);
    answer = Math.max(answer, left + right);  // update global with both
    return Math.max(left, right) + 1;         // return only one branch
}
```

---

## Templates

### Template 1: Recursive Traversal Orders
```java
public List<Integer> inorder(TreeNode root) {
    List<Integer> result = new ArrayList<>();
    inorderDFS(root, result);
    return result;
}
private void inorderDFS(TreeNode node, List<Integer> result) {
    if (node == null) return;
    inorderDFS(node.left, result);
    result.add(node.val);
    inorderDFS(node.right, result);
}

private void preorderDFS(TreeNode node, List<Integer> result) {
    if (node == null) return;
    result.add(node.val);
    preorderDFS(node.left, result);
    preorderDFS(node.right, result);
}

private void postorderDFS(TreeNode node, List<Integer> result) {
    if (node == null) return;
    postorderDFS(node.left, result);
    postorderDFS(node.right, result);
    result.add(node.val);
}
```

### Template 2: Iterative DFS (explicit stack)
```java
// Preorder iterative
public List<Integer> preorderIterative(TreeNode root) {
    List<Integer> result = new ArrayList<>();
    Deque<TreeNode> stack = new ArrayDeque<>();
    stack.push(root);
    while (!stack.isEmpty()) {
        TreeNode node = stack.pop();
        if (node == null) continue;
        result.add(node.val);
        if (node.right != null) stack.push(node.right);  // right first (popped first)
        if (node.left != null) stack.push(node.left);
    }
    return result;
}
```

---

## Problem Solutions

### LC 104: Maximum Depth of Binary Tree (Easy)
```java
// Bottom-up: return height
public int maxDepth(TreeNode root) {
    if (root == null) return 0;
    return 1 + Math.max(maxDepth(root.left), maxDepth(root.right));
}
```

### LC 110: Balanced Binary Tree (Easy)
```java
// Bottom-up: return height, check balance at each node
public boolean isBalanced(TreeNode root) {
    return getHeight(root) != -1;
}
private int getHeight(TreeNode node) {
    if (node == null) return 0;
    int left = getHeight(node.left);
    if (left == -1) return -1;
    int right = getHeight(node.right);
    if (right == -1) return -1;
    if (Math.abs(left - right) > 1) return -1;
    return Math.max(left, right) + 1;
}
```

### LC 543: Diameter of Binary Tree (Easy)
```java
// Global variable pattern
int diameter = 0;
public int diameterOfBinaryTree(TreeNode root) {
    dfs(root);
    return diameter;
}
private int dfs(TreeNode node) {
    if (node == null) return 0;
    int left = dfs(node.left);
    int right = dfs(node.right);
    diameter = Math.max(diameter, left + right);
    return Math.max(left, right) + 1;
}
```

### LC 124: Binary Tree Maximum Path Sum (Hard)
```java
// Max path sum — can bend at any node
int maxSum = Integer.MIN_VALUE;
public int maxPathSum(TreeNode root) {
    dfs(root);
    return maxSum;
}
private int dfs(TreeNode node) {
    if (node == null) return 0;
    int left = Math.max(0, dfs(node.left));
    int right = Math.max(0, dfs(node.right));
    maxSum = Math.max(maxSum, left + right + node.val);
    return Math.max(left, right) + node.val;
}
```

### LC 226: Invert Binary Tree (Easy)
```java
// Preorder: swap children at each node
public TreeNode invertTree(TreeNode root) {
    if (root == null) return null;
    TreeNode temp = root.left;
    root.left = root.right;
    root.right = temp;
    invertTree(root.left);
    invertTree(root.right);
    return root;
}
```

### LC 112: Path Sum (Easy)
```java
// Top-down: pass running sum down
public boolean hasPathSum(TreeNode root, int targetSum) {
    if (root == null) return false;
    if (root.left == null && root.right == null) return root.val == targetSum;
    return hasPathSum(root.left, targetSum - root.val) || hasPathSum(root.right, targetSum - root.val);
}
```

### LC 113: Path Sum II (Medium)
```java
// Top-down: collect all paths
public List<List<Integer>> pathSum(TreeNode root, int targetSum) {
    List<List<Integer>> result = new ArrayList<>();
    dfs(root, targetSum, new ArrayList<>(), result);
    return result;
}
private void dfs(TreeNode node, int remaining, List<Integer> path, List<List<Integer>> result) {
    if (node == null) return;
    path.add(node.val);
    if (node.left == null && node.right == null && node.val == remaining) result.add(new ArrayList<>(path));
    dfs(node.left, remaining - node.val, path, result);
    dfs(node.right, remaining - node.val, path, result);
    path.remove(path.size() - 1);
}
```

### LC 236: Lowest Common Ancestor (Medium)
```java
// Bottom-up: find p and q, return node
public TreeNode lowestCommonAncestor(TreeNode root, TreeNode p, TreeNode q) {
    if (root == null || root == p || root == q) return root;
    TreeNode left = lowestCommonAncestor(root.left, p, q);
    TreeNode right = lowestCommonAncestor(root.right, p, q);
    if (left != null && right != null) return root;
    return left != null ? left : right;
}
```

### LC 105: Construct Tree from Preorder and Inorder (Medium)
```java
// Preorder: [root, left, right]; Inorder: [left, root, right]
Map<Integer, Integer> map = new HashMap<>();
public TreeNode buildTree(int[] preorder, int[] inorder) {
    for (int i = 0; i < inorder.length; i++) map.put(inorder[i], i);
    return build(0, 0, inorder.length - 1);
}
private TreeNode build(int preStart, int inStart, int inEnd) {
    if (preStart >= preorder.length || inStart > inEnd) return null;
    TreeNode root = new TreeNode(preorder[preStart]);
    int inRoot = map.get(root.val);
    root.left = build(preStart + 1, inStart, inRoot - 1);
    root.right = build(preStart + inRoot - inStart + 1, inRoot + 1, inEnd);
    return root;
}
```

### LC 98: Validate BST (Medium)
```java
// Top-down with min/max bounds
public boolean isValidBST(TreeNode root) {
    return validate(root, null, null);
}
private boolean validate(TreeNode node, Integer min, Integer max) {
    if (node == null) return true;
    if ((min != null && node.val <= min) || (max != null && node.val >= max)) return false;
    return validate(node.left, min, node.val) && validate(node.right, node.val, max);
}
```

### LC 230: Kth Smallest in BST (Medium)
```java
// Inorder traversal (sorted) — just collect k elements
public int kthSmallest(TreeNode root, int k) {
    List<Integer> result = new ArrayList<>();
    inorder(root, result);
    return result.get(k - 1);
}
private void inorder(TreeNode node, List<Integer> result) {
    if (node == null || result.size() >= k) return;
    inorder(node.left, result);
    if (result.size() < k) result.add(node.val);
    inorder(node.right, result);
}
```

### LC 114: Flatten Binary Tree to Linked List (Medium)
```java
// Reverse postorder: right, left, root — produces preorder
TreeNode prev = null;
public void flatten(TreeNode root) {
    if (root == null) return;
    flatten(root.right);
    flatten(root.left);
    root.right = prev;
    root.left = null;
    prev = root;
}
```

---

## When to Use DFS vs BFS

| Use DFS | Use BFS |
|---|---|
| Depth/path/structure | Levels/layers/breadth |
| Path sum | Level order traversal |
| BST validation | Shortest path |
| LCA | Right side view |
| Height/diameter | Zigzag traversal |
| Serialization | Nodes at distance K |

---

## Practice Roadmap

1. **Foundation:** LC 104, LC 226, LC 112
2. **Core:** LC 543, LC 110, LC 98, LC 236
3. **Advanced:** LC 124, LC 105, LC 114
4. **Expert:** LC 297 (serialize), LC 99 (recover BST)
