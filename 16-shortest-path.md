# Topic 16: Shortest Path Algorithms

*Document 16 of 20 in the FAANG DSA Prep series.*

**Unweighted → BFS.** **Non-negative weights → Dijkstra.** **Negative weights → Bellman-Ford.**

**Top askers:** Google, Amazon, Meta, Microsoft, Bloomberg

---

## Algorithm Selection

| Graph Type | Algorithm |
|---|---|
| Unweighted | BFS |
| Non-negative weights | Dijkstra |
| Negative weights | Bellman-Ford |
| All pairs | Floyd-Warshall |

---

## Dijkstra's Algorithm

```java
// Standard Dijkstra with min-heap
public int[] dijkstra(int n, List<int[]>[] graph, int src) {
    int[] dist = new int[n];
    Arrays.fill(dist, Integer.MAX_VALUE);
    dist[src] = 0;
    PriorityQueue<int[]> pq = new PriorityQueue<>((a, b) -> a[0] - b[0]);
    pq.offer(new int[]{0, src});
    while (!pq.isEmpty()) {
        int[] curr = pq.poll();
        int d = curr[0], node = curr[1];
        if (d > dist[node]) continue;  // lazy deletion: skip stale entry
        for (int[] edge : graph[node]) {
            int next = edge[0], weight = edge[1];
            if (dist[node] + weight < dist[next]) {
                dist[next] = dist[node] + weight;
                pq.offer(new int[]{dist[next], next});
            }
        }
    }
    return dist;
}
```

---

## Bellman-Ford

```java
// Relax all edges V-1 times. Detect negative cycles with one extra pass.
public int[] bellmanFord(int n, int[][] edges, int src) {
    int[] dist = new int[n];
    Arrays.fill(dist, Integer.MAX_VALUE);
    dist[src] = 0;
    for (int i = 0; i < n - 1; i++) {
        for (int[] e : edges) {
            if (dist[e[0]] != Integer.MAX_VALUE && dist[e[0]] + e[2] < dist[e[1]])
                dist[e[1]] = dist[e[0]] + e[2];
        }
    }
    // Negative cycle detection: if dist can still improve, negative cycle exists
    return dist;
}
```

---

## Problem Solutions

### LC 743: Network Delay Time (Medium)
```java
// Dijkstra: time for signal to reach all nodes
public int networkDelayTime(int[][] times, int n, int k) {
    List<int[]>[] graph = new ArrayList[n];
    for (int i = 0; i < n; i++) graph[i] = new ArrayList<>();
    for (int[] t : times) graph[t[0]-1].add(new int[]{t[1]-1, t[2]});
    int[] dist = new int[n];
    Arrays.fill(dist, Integer.MAX_VALUE);
    dist[k-1] = 0;
    PriorityQueue<int[]> pq = new PriorityQueue<>((a, b) -> a[0] - b[0]);
    pq.offer(new int[]{0, k-1});
    while (!pq.isEmpty()) {
        int[] curr = pq.poll();
        if (curr[0] > dist[curr[1]]) continue;
        for (int[] e : graph[curr[1]]) {
            if (dist[curr[1]] + e[1] < dist[e[0]]) {
                dist[e[0]] = dist[curr[1]] + e[1];
                pq.offer(new int[]{dist[e[0]], e[0]});
            }
        }
    }
    int max = 0;
    for (int d : dist) max = Math.max(max, d);
    return max == Integer.MAX_VALUE ? -1 : max;
}
```

### LC 787: Cheapest Flights Within K Stops (Medium)
```java
// Modified Bellman-Ford: limit to k+1 edges
public int findCheapestPrice(int n, int[][] flights, int src, int dst, int k) {
    int[] dist = new int[n];
    Arrays.fill(dist, Integer.MAX_VALUE);
    dist[src] = 0;
    for (int i = 0; i <= k; i++) {
        int[] next = dist.clone();
        for (int[] f : flights) {
            if (dist[f[0]] != Integer.MAX_VALUE)
                next[f[1]] = Math.min(next[f[1]], dist[f[0]] + f[2]);
        }
        dist = next;
    }
    return dist[dst] == Integer.MAX_VALUE ? -1 : dist[dst];
}
```

### LC 399: Evaluate Division (Medium)
```java
// Build graph, run DFS for each query
public double[] calcEquation(List<List<String>> equations, double[] values, List<List<String>> queries) {
    Map<String, List<String>> nodes = new HashMap<>();
    Map<String, Map<String, Double>> graph = new HashMap<>();
    for (int i = 0; i < equations.size(); i++) {
        String a = equations.get(i).get(0), b = equations.get(i).get(1);
        graph.computeIfAbsent(a, k -> new HashMap<>()).put(b, values[i]);
        graph.computeIfAbsent(b, k -> new HashMap<>()).put(a, 1.0 / values[i]);
    }
    double[] result = new double[queries.size()];
    for (int i = 0; i < queries.size(); i++) {
        String s = queries.get(i).get(0), t = queries.get(i).get(1);
        Set<String> visited = new HashSet<>();
        result[i] = dfs(s, t, 1.0, visited, graph);
    }
    return result;
}
private double dfs(String src, String dst, double acc, Set<String> visited, Map<String, Map<String, Double>> graph) {
    if (!graph.containsKey(src) || !visited.add(src)) return -1.0;
    if (src.equals(dst)) return acc;
    for (String neighbor : graph.getOrDefault(src, new HashMap<>()).keySet()) {
        double result = dfs(neighbor, dst, acc * graph.get(src).get(neighbor), visited, graph);
        if (result != -1.0) return result;
    }
    return -1.0;
}
```

### LC 1334: Find the City With the Smallest Number (Medium)
```java
// Floyd-Warshall or Dijkstra from each node
public int findTheCity(int n, int[][] edges, int distanceThreshold) {
    int[][] dist = new int[n][n];
    for (int[] row : dist) Arrays.fill(row, Integer.MAX_VALUE);
    for (int i = 0; i < n; i++) dist[i][i] = 0;
    for (int[] e : edges) { dist[e[0]][e[1]] = e[2]; dist[e[1]][e[0]] = e[2]; }
    for (int k = 0; k < n; k++)
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                if (dist[i][k] != Integer.MAX_VALUE && dist[k][j] != Integer.MAX_VALUE)
                    dist[i][j] = Math.min(dist[i][j], dist[i][k] + dist[k][j]);
    int result = 0, minCount = n;
    for (int i = 0; i < n; i++) {
        int count = 0;
        for (int j = 0; j < n; j++) if (dist[i][j] <= distanceThreshold) count++;
        if (count <= minCount) { minCount = count; result = i; }
    }
    return result;
}
```

---

## Practice Roadmap

1. **Foundation:** LC 743, LC 787
2. **Intermediate:** LC 399, LC 1334
3. **Advanced:** LC 1631, LC 1514
