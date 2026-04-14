# Topic 14: Graph Traversal (BFS & DFS)

*Document 14 of 20 in the FAANG DSA Prep series.*

Graphs have cycles — you MUST track visited nodes. BFS = shortest path in unweighted graphs. DFS = cycle detection, components, exploration.

**Top askers:** Google, Amazon, Meta, Microsoft, Bloomberg

---

## Graph Representation

```java
// Adjacency list (default for interviews)
Map<Integer, List<Integer>> graph = new HashMap<>();
for (int[] e : edges) {
    graph.computeIfAbsent(e[0], k -> new ArrayList<>()).add(e[1]);
    graph.computeIfAbsent(e[1], k -> new ArrayList<>()).add(e[0]); // omit for directed
}
```

---

## BFS Template

```java
// Shortest path in unweighted graph
public void bfs(int start) {
    Queue<Integer> queue = new LinkedList<>();
    Set<Integer> visited = new HashSet<>();
    queue.offer(start);
    visited.add(start);
    while (!queue.isEmpty()) {
        int node = queue.poll();
        for (int neighbor : graph.getOrDefault(node, new ArrayList<>())) {
            if (!visited.contains(neighbor)) {
                visited.add(neighbor);
                queue.offer(neighbor);
            }
        }
    }
}
```

**Mark visited when adding to queue**, not when polling.

---

## DFS Template

```java
// Recursive
Set<Integer> visited = new HashSet<>();
public void dfs(int node) {
    visited.add(node);
    for (int neighbor : graph.getOrDefault(node, new ArrayList<>())) {
        if (!visited.contains(neighbor)) dfs(neighbor);
    }
}
```

---

## Grid as Implicit Graph

Each cell `(r, c)` is a node. Neighbors = 4-directional cells.

```java
int[][] dirs = {{0,1},{0,-1},{1,0},{-1,0}};
for (int[] d : dirs) {
    int nr = r + d[0], nc = c + d[1];
    if (inBounds(nr, nc, rows, cols) && !visited[nr][nc]) {
        // process neighbor
    }
}
```

---

## Multi-Source BFS

Start from ALL sources at once. Equivalent to a virtual super-source.

```java
Queue<int[]> queue = new LinkedList<>();
boolean[][] visited = new boolean[rows][cols];
for (all source cells) {
    queue.offer(src);
    visited[src.r][src.c] = true;
}
int steps = 0;
while (!queue.isEmpty()) {
    int size = queue.size();
    for (int i = 0; i < size; i++) {
        int[] curr = queue.poll();
        // process curr
        for (int[] d : dirs) {
            int nr = curr[0] + d[0], nc = curr[1] + d[1];
            if (inBounds(nr, nc) && !visited[nr][nc]) {
                visited[nr][nc] = true;
                queue.offer(new int[]{nr, nc});
            }
        }
    }
    steps++;
}
```

---

## Cycle Detection

**Undirected:** Track parent. If visited neighbor != parent → cycle.

**Directed (3-color DFS):**
- WHITE (0): not visited
- GRAY (1): in recursion stack → back edge = cycle
- BLACK (2): fully processed

```java
int[] color;  // 0=white, 1=gray, 2=black
boolean dfs(int node) {
    color[node] = 1;
    for (int neighbor : graph.get(node)) {
        if (color[neighbor] == 1) return true;  // back edge = cycle
        if (color[neighbor] == 0 && dfs(neighbor)) return true;
    }
    color[node] = 2;
    return false;
}
```

---

## Problem Solutions

### LC 200: Number of Islands (Medium)
```java
// DFS flood fill
public int numIslands(char[][] grid) {
    int count = 0;
    for (int r = 0; r < grid.length; r++)
        for (int c = 0; c < grid[0].length; c++)
            if (grid[r][c] == '1') {
                dfs(grid, r, c);
                count++;
            }
    return count;
}
private void dfs(char[][] grid, int r, int c) {
    if (r < 0 || c < 0 || r >= grid.length || c >= grid[0].length || grid[r][c] != '1') return;
    grid[r][c] = '0';
    dfs(grid, r+1, c); dfs(grid, r-1, c);
    dfs(grid, r, c+1); dfs(grid, r, c-1);
}
```

### LC 994: Rotting Oranges (Medium)
```java
// Multi-source BFS from all rotten oranges
public int orangesRotting(int[][] grid) {
    Queue<int[]> queue = new LinkedList<>();
    int fresh = 0, rows = grid.length, cols = grid[0].length;
    for (int r = 0; r < rows; r++)
        for (int c = 0; c < cols; c++) {
            if (grid[r][c] == 2) queue.offer(new int[]{r, c, 0});
            if (grid[r][c] == 1) fresh++;
        }
    int[][] dirs = {{1,0},{-1,0},{0,1},{0,-1}};
    int time = 0;
    while (!queue.isEmpty() && fresh > 0) {
        int[] curr = queue.poll();
        for (int[] d : dirs) {
            int nr = curr[0] + d[0], nc = curr[1] + d[1];
            if (nr >= 0 && nr < rows && nc >= 0 && nc < cols && grid[nr][nc] == 1) {
                grid[nr][nc] = 2;
                fresh--;
                queue.offer(new int[]{nr, nc, curr[2] + 1});
                time = curr[2] + 1;
            }
        }
    }
    return fresh == 0 ? time : -1;
}
```

### LC 542: 01 Matrix (Medium)
```java
// Multi-source BFS from all 0 cells
public int[][] updateMatrix(int[][] mat) {
    int m = mat.length, n = mat[0].length;
    int[][] result = new int[m][n];
    Queue<int[]> queue = new LinkedList<>();
    for (int r = 0; r < m; r++)
        for (int c = 0; c < n; c++) {
            if (mat[r][c] == 0) queue.offer(new int[]{r, c});
            else result[r][c] = -1;
        }
    int[][] dirs = {{1,0},{-1,0},{0,1},{0,-1}};
    while (!queue.isEmpty()) {
        int[] curr = queue.poll();
        for (int[] d : dirs) {
            int nr = curr[0] + d[0], nc = curr[1] + d[1];
            if (nr >= 0 && nr < m && nc >= 0 && nc < n && result[nr][nc] == -1) {
                result[nr][nc] = result[curr[0]][curr[1]] + 1;
                queue.offer(new int[]{nr, nc});
            }
        }
    }
    return result;
}
```

### LC 133: Clone Graph (Medium)
```java
// HashMap old -> new node
public Node cloneGraph(Node node) {
    if (node == null) return null;
    Map<Node, Node> map = new HashMap<>();
    Queue<Node> queue = new LinkedList<>();
    queue.offer(node);
    map.put(node, new Node(node.val));
    while (!queue.isEmpty()) {
        Node curr = queue.poll();
        for (Node neighbor : curr.neighbors) {
            if (!map.containsKey(neighbor)) {
                map.put(neighbor, new Node(neighbor.val));
                queue.offer(neighbor);
            }
            map.get(curr).neighbors.add(map.get(neighbor));
        }
    }
    return map.get(node);
}
```

### LC 207: Course Schedule (Medium)
```java
// Detect cycle in directed graph using 3-color DFS
public boolean canFinish(int numCourses, int[][] prerequisites) {
    List<List<Integer>> graph = new ArrayList<>();
    for (int i = 0; i < numCourses; i++) graph.add(new ArrayList<>());
    for (int[] p : prerequisites) graph.get(p[1]).add(p[0]);
    int[] color = new int[numCourses];  // 0=white, 1=gray, 2=black
    for (int i = 0; i < numCourses; i++)
        if (color[i] == 0 && hasCycle(i, graph, color)) return false;
    return true;
}
private boolean hasCycle(int node, List<List<Integer>> graph, int[] color) {
    color[node] = 1;
    for (int neighbor : graph.get(node)) {
        if (color[neighbor] == 1) return true;
        if (color[neighbor] == 0 && hasCycle(neighbor, graph, color)) return true;
    }
    color[node] = 2;
    return false;
}
```

### LC 417: Pacific Atlantic Water Flow (Medium)
```java
// BFS from both oceans (reverse flow)
public List<int[]> pacificAtlantic(int[][] heights) {
    int m = heights.length, n = heights[0].length;
    boolean[][] pac = new boolean[m][n], atl = new boolean[m][n];
    Queue<int[]> pacQ = new LinkedList<>(), atlQ = new LinkedList<>();
    for (int c = 0; c < n; c++) { pacQ.offer(new int[]{0, c}); pac[0][c] = true; }
    for (int r = 0; r < m; r++) { pacQ.offer(new int[]{r, 0}); pac[r][0] = true; }
    for (int c = 0; c < n; c++) { atlQ.offer(new int[]{m-1, c}); atl[m-1][c] = true; }
    for (int r = 0; r < m; r++) { atlQ.offer(new int[]{r, n-1}); atl[r][n-1] = true; }
    bfs(pacQ, pac, heights); bfs(atlQ, atl, heights);
    List<int[]> result = new ArrayList<>();
    for (int r = 0; r < m; r++)
        for (int c = 0; c < n; c++)
            if (pac[r][c] && atl[r][c]) result.add(new int[]{r, c});
    return result;
}
private void bfs(Queue<int[]> q, boolean[][] visited, int[][] h) {
    int[][] dirs = {{1,0},{-1,0},{0,1},{0,-1}};
    while (!q.isEmpty()) {
        int[] curr = q.poll();
        for (int[] d : dirs) {
            int nr = curr[0] + d[0], nc = curr[1] + d[1];
            if (nr >= 0 && nr < h.length && nc >= 0 && nc < h[0].length && !visited[nr][nc]
                && h[nr][nc] >= h[curr[0]][curr[1]]) {
                visited[nr][nc] = true;
                q.offer(new int[]{nr, nc});
            }
        }
    }
}
```

### LC 785: Is Graph Bipartite? (Medium)
```java
// 2-color BFS
public boolean isBipartite(int[][] graph) {
    int n = graph.length;
    int[] color = new int[n];  // 0=uncolored, 1=red, -1=blue
    for (int i = 0; i < n; i++) {
        if (color[i] != 0) continue;
        Queue<Integer> q = new LinkedList<>();
        q.offer(i);
        color[i] = 1;
        while (!q.isEmpty()) {
            int node = q.poll();
            for (int neighbor : graph[node]) {
                if (color[neighbor] == 0) {
                    color[neighbor] = -color[node];
                    q.offer(neighbor);
                } else if (color[neighbor] == color[node]) {
                    return false;
                }
            }
        }
    }
    return true;
}
```

---

## Practice Roadmap

1. **Foundation:** LC 200, LC 133, LC 785
2. **Intermediate:** LC 994, LC 542, LC 207
3. **Advanced:** LC 417, LC 127 (word ladder), LC 547 (provinces)
