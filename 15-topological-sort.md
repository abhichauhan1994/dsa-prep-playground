# Topic 15: Topological Sort

*Document 15 of 20 in the FAANG DSA Prep series.*

Orders vertices of a DAG so that u appears before v for every edge u→v. **Graph must be acyclic.**

**Top askers:** Google, Amazon, Meta, Microsoft, Bloomberg

---

## Two Algorithms

### Kahn's Algorithm (BFS)
1. Compute in-degree for all nodes
2. Add all nodes with in-degree = 0 to queue
3. While queue not empty: poll node, add to result, decrement neighbors' in-degrees, enqueue any that hit 0
4. If result size != n, cycle exists

### DFS-based
1. Run DFS on each node
2. Push node to stack AFTER all neighbors processed
3. Reverse stack = topological order
4. Cycle: if DFS encounters a GRAY node (currently in recursion stack)

Both O(V + E).

---

## Templates

### Template 1: Kahn's Algorithm
```java
public List<Integer> topologicalSort(int n, int[][] edges) {
    List<List<Integer>> adj = new ArrayList<>();
    for (int i = 0; i < n; i++) adj.add(new ArrayList<>());
    int[] inDegree = new int[n];
    for (int[] e : edges) { adj.get(e[0]).add(e[1]); inDegree[e[1]]++; }
    Queue<Integer> q = new LinkedList<>();
    for (int i = 0; i < n; i++) if (inDegree[i] == 0) q.offer(i);
    List<Integer> result = new ArrayList<>();
    while (!q.isEmpty()) {
        int node = q.poll();
        result.add(node);
        for (int neighbor : adj.get(node)) {
            if (--inDegree[neighbor] == 0) q.offer(neighbor);
        }
    }
    return result.size() == n ? result : new ArrayList<>();  // empty = cycle
}
```

### Template 2: DFS-based
```java
static final int WHITE = 0, GRAY = 1, BLACK = 2;
int[] color;
List<List<Integer>> adj;
Deque<Integer> stack;

public List<Integer> topologicalSortDFS(int n, int[][] edges) {
    adj = new ArrayList<>();
    for (int i = 0; i < n; i++) adj.add(new ArrayList<>());
    for (int[] e : edges) adj.get(e[0]).add(e[1]);
    color = new int[n];
    stack = new ArrayDeque<>();
    for (int i = 0; i < n; i++) if (color[i] == WHITE && hasCycle(i)) return new ArrayList<>();
    List<Integer> result = new ArrayList<>(stack);
    Collections.reverse(result);
    return result;
}
private boolean hasCycle(int node) {
    color[node] = GRAY;
    for (int neighbor : adj.get(node)) {
        if (color[neighbor] == GRAY) return true;
        if (color[neighbor] == WHITE && hasCycle(neighbor)) return true;
    }
    color[node] = BLACK;
    stack.push(node);
    return false;
}
```

---

## Problem Solutions

### LC 207: Course Schedule (Medium)
```java
// Kahn's: can finish all courses?
public boolean canFinish(int numCourses, int[][] prerequisites) {
    List<List<Integer>> adj = new ArrayList<>();
    for (int i = 0; i < numCourses; i++) adj.add(new ArrayList<>());
    int[] inDegree = new int[numCourses];
    for (int[] p : prerequisites) { adj.get(p[1]).add(p[0]); inDegree[p[0]]++; }
    Queue<Integer> q = new LinkedList<>();
    for (int i = 0; i < numCourses; i++) if (inDegree[i] == 0) q.offer(i);
    int count = 0;
    while (!q.isEmpty()) {
        int course = q.poll();
        count++;
        for (int next : adj.get(course))
            if (--inDegree[next] == 0) q.offer(next);
    }
    return count == numCourses;
}
```

### LC 210: Course Schedule II (Medium)
```java
// Kahn's + return order
public int[] findOrder(int numCourses, int[][] prerequisites) {
    List<List<Integer>> adj = new ArrayList<>();
    for (int i = 0; i < numCourses; i++) adj.add(new ArrayList<>());
    int[] inDegree = new int[numCourses];
    for (int[] p : prerequisites) { adj.get(p[1]).add(p[0]); inDegree[p[0]]++; }
    Queue<Integer> q = new LinkedList<>();
    for (int i = 0; i < numCourses; i++) if (inDegree[i] == 0) q.offer(i);
    int[] result = new int[numCourses];
    int idx = 0;
    while (!q.isEmpty()) {
        int course = q.poll();
        result[idx++] = course;
        for (int next : adj.get(course))
            if (--inDegree[next] == 0) q.offer(next);
    }
    return idx == numCourses ? result : new int[0];
}
```

### LC 269: Alien Dictionary (Hard)
```java
// Topological sort on character graph
public String alienOrder(String[] words) {
    Map<Character, Set<Character>> graph = new HashMap<>();
    Set<Character> allChars = new HashSet<>();
    for (String w : words) allChars.addAll(Arrays.asList(w.toCharArray()));
    for (String w : words) for (char c : w.toCharArray()) graph.putIfAbsent(c, new HashSet<>());
    for (int i = 0; i < words.length - 1; i++) {
        String w1 = words[i], w2 = words[i+1];
        if (w1.length() > w2.length() && w1.startsWith(w2)) return "";
        for (int j = 0; j < Math.min(w1.length(), w2.length()); j++) {
            if (w1.charAt(j) != w2.charAt(j)) {
                graph.get(w2.charAt(j)).add(w1.charAt(j));
                break;
            }
        }
    }
    Map<Character, Integer> inDegree = new HashMap<>();
    for (char c : allChars) inDegree.put(c, 0);
    for (char c : allChars)
        for (char neighbor : graph.get(c)) inDegree.put(neighbor, inDegree.get(neighbor) + 1);
    Queue<Character> q = new LinkedList<>();
    for (char c : allChars) if (inDegree.get(c) == 0) q.offer(c);
    StringBuilder sb = new StringBuilder();
    while (!q.isEmpty()) {
        char c = q.poll();
        sb.append(c);
        for (char neighbor : graph.get(c)) {
            inDegree.put(neighbor, inDegree.get(neighbor) - 1);
            if (inDegree.get(neighbor) == 0) q.offer(neighbor);
        }
    }
    return sb.length() == allChars.size() ? sb.toString() : "";
}
```

### LC 802: Find Eventual Safe States (Medium)
```java
// Reverse graph: safe states have no path to cycle (reverse edges)
public List<Integer> eventualSafeStates(int[][] graph) {
    int n = graph.length;
    List<List<Integer>> rev = new ArrayList<>();
    int[] outDegree = new int[n];
    for (int i = 0; i < n; i++) rev.add(new ArrayList<>());
    for (int i = 0; i < n; i++)
        for (int v : graph[i]) { rev.get(v).add(i); outDegree[i]++; }
    Queue<Integer> q = new LinkedList<>();
    for (int i = 0; i < n; i++) if (outDegree[i] == 0) q.offer(i);
    boolean[] safe = new boolean[n];
    while (!q.isEmpty()) {
        int node = q.poll();
        safe[node] = true;
        for (int prev : rev[node])
            if (--outDegree[prev] == 0) q.offer(prev);
    }
    List<Integer> result = new ArrayList<>();
    for (int i = 0; i < n; i++) if (safe[i]) result.add(i);
    return result;
}
```

### LC 1136: Parallel Courses (Medium)
```java
// Kahn's with level/term counting
public int minimumSemesters(int n, int[][] relations) {
    List<List<Integer>> adj = new ArrayList<>();
    for (int i = 0; i < n; i++) adj.add(new ArrayList<>());
    int[] inDegree = new int[n];
    for (int[] r : relations) { adj.get(r[0] - 1).add(r[1] - 1); inDegree[r[1] - 1]++; }
    Queue<Integer> q = new LinkedList<>();
    for (int i = 0; i < n; i++) if (inDegree[i] == 0) q.offer(i);
    int semesters = 0, coursesTaken = 0;
    while (!q.isEmpty()) {
        semesters++;
        int size = q.size();
        for (int i = 0; i < size; i++) {
            int course = q.poll();
            coursesTaken++;
            for (int next : adj.get(course))
                if (--inDegree[next] == 0) q.offer(next);
        }
    }
    return coursesTaken == n ? semesters : -1;
}
```

---

## Practice Roadmap

1. **Foundation:** LC 207, LC 210
2. **Intermediate:** LC 802, LC 1136
3. **Advanced:** LC 269, LC 444
