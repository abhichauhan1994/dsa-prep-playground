# Topic 13: Backtracking

*Document 13 of 20 in the FAANG DSA Prep series.*

Backtracking = controlled recursion. You explore all possibilities, then undo choices. Three-step loop: **choose → explore → unchoose**.

**Top askers:** Google, Amazon, Meta, Microsoft, Bloomberg

---

## The Three Problem Categories

### 1. Subsets (include/exclude each element)
- 2^n subsets (each element has 2 choices)
- `recurse(i+1)` — no reuse

### 2. Permutations (all orderings)
- n! permutations
- `used[]` array + `recurse(0)` — restart from beginning

### 3. Combinations (choose k from n)
- C(n,k) combinations
- `recurse(i+1)` + size check

---

## The Core Loop

```java
void backtrack(params) {
    if (base_case) { record_result(); return; }
    for (choice : candidates) {
        make_choice(choice);      // CHOOSE
        backtrack(updated_params); // EXPLORE
        undo_choice(choice);       // UNCHOOSE (backtrack)
    }
}
```

---

## Templates

### Template 1: Subsets
```java
public List<List<Integer>> subsets(int[] nums) {
    List<List<Integer>> result = new ArrayList<>();
    backtrack(0, new ArrayList<>(), result, nums);
    return result;
}
private void backtrack(int start, List<Integer> path, List<List<Integer>> result, int[] nums) {
    result.add(new ArrayList<>(path));
    for (int i = start; i < nums.length; i++) {
        path.add(nums[i]);
        backtrack(i + 1, path, result, nums);
        path.remove(path.size() - 1);
    }
}
```

### Template 2: Subsets with Duplicates (skip duplicates)
```java
// Sort first, skip duplicates by checking if nums[i]==nums[i-1]
public List<List<Integer>> subsetsWithDup(int[] nums) {
    Arrays.sort(nums);
    List<List<Integer>> result = new ArrayList<>();
    backtrack(0, new ArrayList<>(), result, nums);
    return result;
}
private void backtrack(int start, List<Integer> path, List<List<Integer>> result, int[] nums) {
    result.add(new ArrayList<>(path));
    for (int i = start; i < nums.length; i++) {
        if (i > start && nums[i] == nums[i - 1]) continue;  // skip duplicates
        path.add(nums[i]);
        backtrack(i + 1, path, result, nums);
        path.remove(path.size() - 1);
    }
}
```

### Template 3: Permutations
```java
public List<List<Integer>> permute(int[] nums) {
    List<List<Integer>> result = new ArrayList<>();
    backtrack(new boolean[nums.length], new ArrayList<>(), result, nums);
    return result;
}
private void backtrack(boolean[] used, List<Integer> path, List<List<Integer>> result, int[] nums) {
    if (path.size() == nums.length) { result.add(new ArrayList<>(path)); return; }
    for (int i = 0; i < nums.length; i++) {
        if (used[i]) continue;
        used[i] = true;
        path.add(nums[i]);
        backtrack(used, path, result, nums);
        path.remove(path.size() - 1);
        used[i] = false;
    }
}
```

### Template 4: Permutations with Duplicates
```java
// Sort, then skip if same as previous unused
public List<List<Integer>> permuteUnique(int[] nums) {
    Arrays.sort(nums);
    List<List<Integer>> result = new ArrayList<>();
    backtrack(new boolean[nums.length], new ArrayList<>(), result, nums);
    return result;
}
private void backtrack(boolean[] used, List<Integer> path, List<List<Integer>> result, int[] nums) {
    if (path.size() == nums.length) { result.add(new ArrayList<>(path)); return; }
    for (int i = 0; i < nums.length; i++) {
        if (used[i] || (i > 0 && nums[i] == nums[i-1] && !used[i-1])) continue;
        used[i] = true;
        path.add(nums[i]);
        backtrack(used, path, result, nums);
        path.remove(path.size() - 1);
        used[i] = false;
    }
}
```

### Template 5: Combinations (choose k)
```java
public List<List<Integer>> combine(int n, int k) {
    List<List<Integer>> result = new ArrayList<>();
    backtrack(1, new ArrayList<>(), result, n, k);
    return result;
}
private void backtrack(int start, List<Integer> path, List<List<Integer>> result, int n, int k) {
    if (path.size() == k) { result.add(new ArrayList<>(path)); return; }
    for (int i = start; i <= n; i++) {
        path.add(i);
        backtrack(i + 1, path, result, n, k);
        path.remove(path.size() - 1);
    }
}
```

### Template 6: Combination Sum
```java
// Can reuse elements
public List<List<Integer>> combinationSum(int[] candidates, int target) {
    List<List<Integer>> result = new ArrayList<>();
    backtrack(0, target, new ArrayList<>(), result, candidates);
    return result;
}
private void backtrack(int start, int remaining, List<Integer> path, List<List<Integer>> result, int[] candidates) {
    if (remaining == 0) { result.add(new ArrayList<>(path)); return; }
    for (int i = start; i < candidates.length; i++) {
        if (candidates[i] > remaining) continue;
        path.add(candidates[i]);
        backtrack(i, remaining - candidates[i], path, result, candidates);
        path.remove(path.size() - 1);
    }
}
```

---

## Problem Solutions

### LC 78: Subsets (Medium)
```java
// Template 1
public List<List<Integer>> subsets(int[] nums) {
    List<List<Integer>> result = new ArrayList<>();
    backtrack(0, new ArrayList<>(), result, nums);
    return result;
}
private void backtrack(int start, List<Integer> path, List<List<Integer>> result, int[] nums) {
    result.add(new ArrayList<>(path));
    for (int i = start; i < nums.length; i++) {
        path.add(nums[i]);
        backtrack(i + 1, path, result, nums);
        path.remove(path.size() - 1);
    }
}
```

### LC 46: Permutations (Medium)
```java
// Template 3
public List<List<Integer>> permute(int[] nums) {
    List<List<Integer>> result = new ArrayList<>();
    backtrack(new boolean[nums.length], new ArrayList<>(), result, nums);
    return result;
}
private void backtrack(boolean[] used, List<Integer> path, List<List<Integer>> result, int[] nums) {
    if (path.size() == nums.length) { result.add(new ArrayList<>(path)); return; }
    for (int i = 0; i < nums.length; i++) {
        if (used[i]) continue;
        used[i] = true; path.add(nums[i]);
        backtrack(used, path, result, nums);
        path.remove(path.size() - 1); used[i] = false;
    }
}
```

### LC 17: Letter Combinations of a Phone Number (Medium)
```java
String[] map = {"","","abc","def","ghi","jkl","mno","pqrs","tuv","wxyz"};
public List<String> letterCombinations(String digits) {
    List<String> result = new ArrayList<>();
    if (digits.isEmpty()) return result;
    backtrack(0, new StringBuilder(), result, digits);
    return result;
}
private void backtrack(int idx, StringBuilder sb, List<String> result, String digits) {
    if (sb.length() == digits.length()) { result.add(sb.toString()); return; }
    for (char c : map[digits.charAt(idx) - '0'].toCharArray()) {
        sb.append(c);
        backtrack(idx + 1, sb, result, digits);
        sb.deleteCharAt(sb.length() - 1);
    }
}
```

### LC 79: Word Search (Medium)
```java
public boolean exist(char[][] board, String word) {
    for (int r = 0; r < board.length; r++)
        for (int c = 0; c < board[0].length; c++)
            if (backtrack(r, c, 0, board, word)) return true;
    return false;
}
private boolean backtrack(int r, int c, int idx, char[][] board, String word) {
    if (idx == word.length()) return true;
    if (r < 0 || c < 0 || r >= board.length || c >= board[0].length || board[r][c] != word.charAt(idx))
        return false;
    char temp = board[r][c];
    board[r][c] = '#';
    boolean found = backtrack(r+1,c,idx+1,board,word) || backtrack(r-1,c,idx+1,board,word)
                 || backtrack(r,c+1,idx+1,board,word) || backtrack(r,c-1,idx+1,board,word);
    board[r][c] = temp;
    return found;
}
```

### LC 51: N-Queens (Hard)
```java
public List<List<String>> solveNQueens(int n) {
    List<List<String>> result = new ArrayList<>();
    char[][] board = new char[n][n];
    for (char[] row : board) Arrays.fill(row, '.');
    backtrack(0, board, result, new boolean[n], new boolean[2*n], new boolean[2*n]);
    return result;
}
private void backtrack(int row, char[][] board, List<List<String>> result,
                       boolean[] cols, boolean[] d1, boolean[] d2) {
    if (row == board.length) { result.add(boardToList(board)); return; }
    for (int col = 0; col < board.length; col++) {
        int d1Idx = row + col, d2Idx = row - col + board.length;
        if (cols[col] || d1[d1Idx] || d2[d2Idx]) continue;
        cols[col] = d1[d1Idx] = d2[d2Idx] = true;
        board[row][col] = 'Q';
        backtrack(row + 1, board, result, cols, d1, d2);
        board[row][col] = '.';
        cols[col] = d1[d1Idx] = d2[d2Idx] = false;
    }
}
```

### LC 131: Palindrome Partitioning (Medium)
```java
public List<List<String>> partition(String s) {
    List<List<String>> result = new ArrayList<>();
    backtrack(0, new ArrayList<>(), result, s);
    return result;
}
private void backtrack(int start, List<String> path, List<List<String>> result, String s) {
    if (start == s.length()) { result.add(new ArrayList<>(path)); return; }
    for (int end = start; end < s.length(); end++) {
        if (isPalindrome(s, start, end)) {
            path.add(s.substring(start, end + 1));
            backtrack(end + 1, path, result, s);
            path.remove(path.size() - 1);
        }
    }
}
private boolean isPalindrome(String s, int l, int r) {
    while (l < r) if (s.charAt(l++) != s.charAt(r--)) return false;
    return true;
}
```

---

## Practice Roadmap

1. **Foundation:** LC 78, LC 46, LC 17
2. **Intermediate:** LC 79, LC 131, LC 40
3. **Advanced:** LC 51, LC 37 (Sudoku), LC 212
4. **Expert:** LC 679, LC 488
