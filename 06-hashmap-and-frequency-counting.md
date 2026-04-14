# Topic 6: HashMap & Frequency Counting

*Document 6 of 20 in the FAANG DSA Prep series.*

HashMap provides O(1) average-case lookup/insert. Five core patterns cover ~80% of HashMap interview problems.

**Top askers:** Google, Amazon, Meta, Microsoft, Bloomberg, Apple

---

## The Five Core Patterns

### 1. Frequency Counting
Build `Map<element, count>` by iterating once. Then query counts.

### 2. Two-Sum / Complement Lookup
For each element `x`, check if `target - x` exists. Store what you've seen.

### 3. Grouping / Bucketing
Map a canonical key to a list. Use `computeIfAbsent`.

### 4. HashSet Existence Check
O(1) "have I seen this?" — only keys matter, not values.

### 5. Index Mapping
Store first/last occurrence of a value for longest subarray problems.

---

## Java API Essentials

```java
// getOrDefault — avoid null checks
int count = map.getOrDefault(key, 0);

// merge — increment counter cleanly
map.merge(key, 1, Integer::sum);

// computeIfAbsent — create list on first encounter
map.computeIfAbsent(key, k -> new ArrayList<>()).add(value);

// entrySet — iterate key-value pairs
for (Map.Entry<K, V> e : map.entrySet()) { ... }

// putIfAbsent — only insert if key doesn't exist
map.putIfAbsent(key, defaultValue);
```

---

## Templates

### Template 1: Frequency Counter
```java
// Count occurrences of each element
Map<Integer, Integer> freq = new HashMap<>();
for (int num : nums) {
    freq.merge(num, 1, Integer::sum);
    // or: freq.put(num, freq.getOrDefault(num, 0) + 1);
}
```

### Template 2: Two-Sum (return indices)
```java
// One pass, store value -> index
public int[] twoSum(int[] nums, int target) {
    Map<Integer, Integer> seen = new HashMap<>();
    for (int i = 0; i < nums.length; i++) {
        int complement = target - nums[i];
        if (seen.containsKey(complement)) return new int[]{seen.get(complement), i};
        seen.put(nums[i], i);
    }
    return new int[]{-1, -1};
}
```

### Template 3: Two-Sum with Array Elements (LC 1 variant)
```java
// Find if two numbers sum to target — no indices needed
public boolean twoSumExists(int[] nums, int target) {
    Set<Integer> seen = new HashSet<>();
    for (int num : nums) {
        if (seen.contains(target - num)) return true;
        seen.add(num);
    }
    return false;
}
```

### Template 4: Grouping with Canonical Key
```java
// Group anagrams: canonical key = sorted string
public List<List<String>> groupAnagrams(String[] strs) {
    Map<String, List<String>> groups = new HashMap<>();
    for (String s : strs) {
        char[] ca = s.toCharArray();
        Arrays.sort(ca);
        String key = new String(ca);
        groups.computeIfAbsent(key, k -> new ArrayList<>()).add(s);
    }
    return new ArrayList<>(groups.values());
}
```

### Template 5: Array for Bounded Elements (faster than HashMap)
```java
// For lowercase letters: int[26]; for ASCII: int[128]
// No hashing overhead, no boxing/unboxing
int[] freq = new int[26];
for (char c : s.toCharArray()) freq[c - 'a']++;
```

---

## Problem Solutions

### LC 1: Two Sum (Easy)
```java
// Template 2 direct application
public int[] twoSum(int[] nums, int target) {
    Map<Integer, Integer> map = new HashMap<>();
    for (int i = 0; i < nums.length; i++) {
        int complement = target - nums[i];
        if (map.containsKey(complement)) return new int[]{map.get(complement), i};
        map.put(nums[i], i);
    }
    return new int[]{-1, -1};
}
```

### LC 242: Valid Anagram (Easy)
```java
// Frequency arrays for both strings, compare
public boolean isAnagram(String s, String t) {
    if (s.length() != t.length()) return false;
    int[] count = new int[26];
    for (char c : s.toCharArray()) count[c - 'a']++;
    for (char c : t.toCharArray()) if (--count[c - 'a'] < 0) return false;
    return true;
}
```

### LC 383: Ransom Note (Easy)
```java
// Frequency map from magazine, subtract from ransom
public boolean canConstruct(String ransomNote, String magazine) {
    int[] freq = new int[26];
    for (char c : magazine.toCharArray()) freq[c - 'a']++;
    for (char c : ransomNote.toCharArray()) if (--freq[c - 'a'] < 0) return false;
    return true;
}
```

### LC 49: Group Anagrams (Medium)
```java
// Template 4: group by sorted characters
public List<List<String>> groupAnagrams(String[] strs) {
    Map<String, List<String>> map = new HashMap<>();
    for (String s : strs) {
        char[] ca = s.toCharArray();
        Arrays.sort(ca);
        String key = String.valueOf(ca);
        map.computeIfAbsent(key, k -> new ArrayList<>()).add(s);
    }
    return new ArrayList<>(map.values());
}
```

### LC 128: Longest Consecutive Sequence (Medium)
```java
// HashSet for O(1) lookup, find sequence starts
public int longestConsecutive(int[] nums) {
    Set<Integer> set = new HashSet<>();
    for (int num : nums) set.add(num);
    int longest = 0;
    for (int num : nums) {
        if (!set.contains(num - 1)) {  // only start at beginning
            int current = num;
            while (set.contains(current)) current++;
            longest = Math.max(longest, current - num);
        }
    }
    return longest;
}
```

### LC 347: Top K Frequent Elements (Medium)
```java
// Bucket sort by frequency — O(n) instead of O(n log n)
public int[] topKFrequent(int[] nums, int k) {
    Map<Integer, Integer> freq = new HashMap<>();
    for (int num : nums) freq.merge(num, 1, Integer::sum);

    List<Integer>[] bucket = new List[nums.length + 1];
    for (int num : freq.keySet()) {
        int f = freq.get(num);
        if (bucket[f] == null) bucket[f] = new ArrayList<>();
        bucket[f].add(num);
    }

    int[] result = new int[k];
    int idx = 0;
    for (int f = nums.length; f >= 0 && idx < k; f--) {
        if (bucket[f] != null) {
            for (int num : bucket[f]) result[idx++] = num;
        }
    }
    return result;
}
```

### LC 380: Insert Delete GetRandom O(1) (Medium)
```java
// HashMap + ArrayList: O(1) everything
class RandomizedSet {
    Map<Integer, Integer> map = new HashMap<>();
    List<Integer> list = new ArrayList<>();

    public boolean insert(int val) {
        if (map.containsKey(val)) return false;
        map.put(val, list.size());
        list.add(val);
        return true;
    }

    public boolean remove(int val) {
        if (!map.containsKey(val)) return false;
        int idx = map.get(val);
        int last = list.get(list.size() - 1);
        list.set(idx, last);
        map.put(last, idx);
        list.remove(list.size() - 1);
        map.remove(val);
        return true;
    }

    public int getRandom() {
        return list.get((int)(Math.random() * list.size()));
    }
}
```

### LC 146: LRU Cache (Medium)
```java
// LinkedHashMap with accessOrder=true
class LRUCache extends LinkedHashMap<Integer, Integer> {
    private int capacity;
    public LRUCache(int capacity) {
        super(capacity, 0.75f, true);
        this.capacity = capacity;
    }
    protected boolean removeEldestEntry(Map.Entry eldest) {
        return size() > capacity;
    }
}
```

### LC 205: Isomorphic Strings (Easy)
```java
// Bidirectional mapping: s->t and t->s must both be consistent
public boolean isIsomorphic(String s, String t) {
    Map<Character, Character> sToT = new HashMap<>();
    Map<Character, Character> tToS = new HashMap<>();
    for (int i = 0; i < s.length(); i++) {
        char c1 = s.charAt(i), c2 = t.charAt(i);
        if (sToT.containsKey(c1) && sToT.get(c1) != c2) return false;
        if (tToS.containsKey(c2) && tToS.get(c2) != c1) return false;
        sToT.put(c1, c2);
        tToS.put(c2, c1);
    }
    return true;
}
```

### LC 387: First Unique Character (Easy)
```java
// Frequency + index mapping: find first char with freq=1
public int firstUniqChar(String s) {
    Map<Character, Integer> freq = new HashMap<>();
    Map<Character, Integer> firstIdx = new HashMap<>();
    for (int i = 0; i < s.length(); i++) {
        char c = s.charAt(i);
        freq.merge(c, 1, Integer::sum);
        firstIdx.putIfAbsent(c, i);
    }
    int result = Integer.MAX_VALUE;
    for (char c : freq.keySet()) {
        if (freq.get(c) == 1) result = Math.min(result, firstIdx.get(c));
    }
    return result == Integer.MAX_VALUE ? -1 : result;
}
```

### LC 36: Valid Sudoku (Medium)
```java
// Three HashSets: rows[9], cols[9], boxes[3][3]
public boolean isValidSudoku(char[][] board) {
    Set<Character>[] rows = new HashSet[9];
    Set<Character>[] cols = new HashSet[9];
    Set<Character>[][] boxes = new HashSet[3][3];
    for (int i = 0; i < 9; i++) {
        rows[i] = new HashSet<>();
        cols[i] = new HashSet<>();
        for (int j = 0; j < 3; j++) boxes[i][j] = new HashSet<>();
    }
    for (int i = 0; i < 9; i++) {
        for (int j = 0; j < 9; j++) {
            char val = board[i][j];
            if (val == '.') continue;
            if (!rows[i].add(val)) return false;
            if (!cols[j].add(val)) return false;
            if (!boxes[i/3][j/3].add(val)) return false;
        }
    }
    return true;
}
```

---

## When to Use HashMap

**Use HashMap when:**
- Need O(1) lookup by key
- Counting frequencies
- Finding pairs/triplets with target sums
- Grouping by a property
- Need to track "first/last seen" positions

**Don't use HashMap when:**
- Elements are bounded (use `int[26]` or `int[128]` instead — faster)
- Need sorted keys (use `TreeMap`)
- Only need existence (use `HashSet`)

---

## Practice Roadmap

1. **Foundation:** LC 1, LC 242, LC 383
2. **Grouping:** LC 49, LC 205
3. **Frequency:** LC 347, LC 451
4. **O(1) DS:** LC 380, LC 146
5. **Advanced:** LC 128, LC 170, LC 560 (with prefix sum)
