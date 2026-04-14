# Topic 17: Union-Find (Disjoint Set Union)

*Document 17 of 20 in the FAANG DSA Prep series.*

Track connected components. O(α(n)) amortized with path compression + union by rank. "α(n)" is effectively constant.

**Top askers:** Google, Amazon, Meta, Microsoft, Bloomberg

---

## Core Operations

- **find(x):** Find root/parent of x (which group)
- **union(x, y):** Merge groups containing x and y
- **connected(x, y):** Same group if find(x) == find(y)

---

## Template: Union-Find with Path Compression + Union by Rank

```java
class UnionFind {
    int[] parent;
    int[] rank;
    int components;

    UnionFind(int n) {
        parent = new int[n];
        rank = new int[n];
        for (int i = 0; i < n; i++) parent[i] = i;
        components = n;
    }

    int find(int x) {
        if (parent[x] != x) parent[x] = find(parent[x]);  // path compression
        return parent[x];
    }

    void union(int x, int y) {
        int rx = find(x), ry = find(y);
        if (rx == ry) return;
        if (rank[rx] < rank[ry]) parent[rx] = ry;
        else if (rank[rx] > rank[ry]) parent[ry] = rx;
        else { parent[ry] = rx; rank[rx]++; }
        components--;
    }

    boolean connected(int x, int y) { return find(x) == find(y); }
}
```

---

## Problem Solutions

### LC 200: Number of Islands (Medium)
```java
// Instead of DFS, use Union-Find to connect land cells
public int numIslands(char[][] grid) {
    int m = grid.length, n = grid[0].length;
    UnionFind uf = new UnionFind(m * n);
    int[][] dirs = {{1,0},{-1,0},{0,1},{0,-1}};
    for (int r = 0; r < m; r++)
        for (int c = 0; c < n; c++)
            if (grid[r][c] == '1') {
                int id = r * n + c;
                for (int[] d : dirs) {
                    int nr = r + d[0], nc = c + d[1];
                    if (nr >= 0 && nr < m && nc >= 0 && nc < n && grid[nr][nc] == '1')
                        uf.union(id, nr * n + nc);
                }
            }
    Set<Integer> islands = new HashSet<>();
    for (int r = 0; r < m; r++)
        for (int c = 0; c < n; c++)
            if (grid[r][c] == '1') islands.add(uf.find(r * n + c));
    return islands.size();
}
```

### LC 323: Number of Connected Components (Medium)
```java
// Count components from edge list
public int countComponents(int n, int[][] edges) {
    UnionFind uf = new UnionFind(n);
    for (int[] e : edges) uf.union(e[0], e[1]);
    return uf.components;
}
```

### LC 261: Graph Valid Tree (Medium)
```java
// Graph is valid tree iff: connected AND no cycles (edges = n-1)
public boolean validTree(int n, int[][] edges) {
    if (edges.length != n - 1) return false;
    UnionFind uf = new UnionFind(n);
    for (int[] e : edges) uf.union(e[0], e[1]);
    return uf.components == 1;
}
```

### LC 128: Longest Consecutive Sequence (Hard)
```java
// Union-Find with hash mapping — find max component size
public int longestConsecutive(int[] nums) {
    if (nums.length == 0) return 0;
    Map<Integer, Integer> map = new HashMap<>();
    UnionFind uf = new UnionFind(nums.length);
    for (int i = 0; i < nums.length; i++) map.put(nums[i], i);
    for (int num : nums) {
        int i = map.get(num);
        if (map.containsKey(num - 1)) uf.union(i, map.get(num - 1));
        if (map.containsKey(num + 1)) uf.union(i, map.get(num + 1));
    }
    return uf.maxComponentSize;
}
```

### LC 721: Accounts Merge (Medium)
```java
// Union accounts by shared email, then group
public List<List<String>> accountsMerge(List<List<String>> accounts) {
    int n = accounts.size();
    UnionFind uf = new UnionFind(n);
    Map<String, Integer> emailToIdx = new HashMap<>();
    for (int i = 0; i < n; i++)
        for (int j = 1; j < accounts.get(i).size(); j++) {
            String email = accounts.get(i).get(j);
            if (emailToIdx.containsKey(email)) uf.union(i, emailToIdx.get(email));
            else emailToIdx.put(email, i);
        }
    Map<Integer, TreeSet<String>> groups = new HashMap<>();
    for (int i = 0; i < n; i++) {
        int root = uf.find(i);
        groups.computeIfAbsent(root, k -> new TreeSet<>()).addAll(accounts.get(i).subList(1, accounts.get(i).size()));
    }
    List<List<String>> result = new ArrayList<>();
    for (int root : groups.keySet()) {
        List<String> account = new ArrayList<>();
        account.add(accounts.get(root).get(0));
        account.addAll(groups.get(root));
        result.add(account);
    }
    return result;
}
```

### LC 684: Redundant Connection (Medium)
```java
// Union-Find: first edge that creates a cycle is the redundant one
public int[] findRedundantConnection(int[][] edges) {
    UnionFind uf = new UnionFind(edges.length);
    for (int[] e : edges)
        if (!uf.unionSafe(e[0]-1, e[1]-1)) return e;
    return new int[]{};
}
```

### LC 685: Redundant Connection II (Hard)
```java
// Directed graph: find edge that makes it invalid
public int[] findRedundantDirectedConnection(int[][] edges) {
    int n = edges.length;
    int[] parent = new int[n + 1];
    Arrays.fill(parent, -1);
    int[] candA = null, candB = null;
    for (int[] e : edges) {
        int u = e[0], v = e[1];
        if (parent[v] != -1) { candA = new int[]{parent[v], v}; candB = e; }
        else parent[v] = u;
    }
    UnionFind uf = new UnionFind(n + 1);
    for (int i = 1; i <= n; i++) uf.parent[i] = i;
    for (int[] e : edges) {
        if (candB != null && e[0] == candB[0] && e[1] == candB[1]) continue;
        if (!uf.unionSafe(e[0], e[1])) return candA != null ? candA : e;
    }
    return candB;
}
```

### LC 947: Most Stones Removed (Medium)
```java
// Union-Find: connected stones can be reduced to 1 per component
public int removeStones(int[][] stones) {
    UnionFind uf = new UnionFind(stones.length);
    for (int i = 0; i < stones.length; i++)
        for (int j = i + 1; j < stones.length; j++)
            if (stones[i][0] == stones[j][0] || stones[i][1] == stones[j][1])
                uf.union(i, j);
    return stones.length - uf.components;
}
```

### LC 1135: Connecting Cities With Minimum Cost (Medium)
```java
// Minimum Spanning Tree using Union-Find (Kruskal's)
public int minimumCost(int n, int[][] connections) {
    Arrays.sort(connections, Comparator.comparingInt(a -> a[2]));
    UnionFind uf = new UnionFind(n + 1);
    int cost = 0, edges = 0;
    for (int[] c : connections) {
        if (uf.unionSafe(c[0], c[1])) { cost += c[2]; edges++; }
        if (edges == n - 1) break;
    }
    return edges == n - 1 ? cost : -1;
}
```

---

## Practice Roadmap

1. **Foundation:** LC 200, LC 323, LC 684
2. **Intermediate:** LC 261, LC 947, LC 721
3. **Advanced:** LC 1135, LC 128, LC 685
