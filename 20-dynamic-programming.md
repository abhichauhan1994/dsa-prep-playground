# Topic 20: Dynamic Programming

*Document 20 of 20 in the FAANG DSA Prep series.*

DP = recursion + memoization. Two properties: optimal substructure + overlapping subproblems. Hardest part: defining state and transition.

**Top askers:** Google, Amazon, Meta, Microsoft, Bloomberg

---

## The 4-Step Framework

1. **Define State:** What does `dp[i]` or `dp[i][j]` represent?
2. **Write Transition:** How does state relate to smaller subproblems?
3. **Identify Base Cases:** Known values at boundaries
4. **Find Answer:** Which cell holds the final result?

---

## Templates

### Template 1: 1D DP
```java
// dp[i] = answer for subproblem of size i
int[] dp = new int[n + 1];
dp[0] = BASE_0; dp[1] = BASE_1;
for (int i = 2; i <= n; i++) dp[i] = transition(dp[i-1], dp[i-2]);
return dp[n];

// Space optimized:
int prev2 = BASE_0, prev1 = BASE_1;
for (int i = 2; i <= n; i++) { int curr = transition(prev1, prev2); prev2 = prev1; prev1 = curr; }
return prev1;
```

### Template 2: 2D DP
```java
// dp[i][j] = answer for first i elements and first j elements
int[][] dp = new int[m + 1][n + 1];
for (int i = 0; i <= m; i++) dp[i][0] = BASE_ROW;
for (int j = 0; j <= n; j++) dp[0][j] = BASE_COL;
for (int i = 1; i <= m; i++)
    for (int j = 1; j <= n; j++)
        dp[i][j] = transition(dp[i-1][j], dp[i][j-1], dp[i-1][j-1]);
return dp[m][n];
```

### Template 3: Top-Down Memoization
```java
Map<String, Type> memo = new HashMap<>();
Type solve(params) {
    String key = computeKey(params);
    if (memo.containsKey(key)) return memo.get(key);
    Type result = ...; // recursive computation
    memo.put(key, result);
    return result;
}
```

### Template 4: 0/1 Knapsack
```java
// dp[w] = max value with capacity w
int[] dp = new int[capacity + 1];
for (int i = 0; i < items.length; i++)
    for (int w = capacity; w >= weight[i]; w--)
        dp[w] = Math.max(dp[w], dp[w - weight[i]] + value[i]);
return dp[capacity];
```

---

## Problem Solutions

### LC 70: Climbing Stairs (Easy)
```java
public int climbStairs(int n) {
    if (n <= 2) return n;
    int prev2 = 1, prev1 = 2;
    for (int i = 3; i <= n; i++) { int cur = prev1 + prev2; prev2 = prev1; prev1 = cur; }
    return prev1;
}
```

### LC 198: House Robber (Medium)
```java
public int rob(int[] nums) {
    int prev2 = 0, prev1 = 0;
    for (int num : nums) { int cur = Math.max(prev1, prev2 + num); prev2 = prev1; prev1 = cur; }
    return prev1;
}
```

### LC 213: House Robber II (Medium)
```java
public int rob2(int[] nums) {
    if (nums.length == 1) return nums[0];
    return Math.max(robRange(nums, 0, nums.length - 2), robRange(nums, 1, nums.length - 1));
}
private int robRange(int[] nums, int start, int end) {
    int prev2 = 0, prev1 = 0;
    for (int i = start; i <= end; i++) { int cur = Math.max(prev1, prev2 + nums[i]); prev2 = prev1; prev1 = cur; }
    return prev1;
}
```

### LC 300: Longest Increasing Subsequence (Medium)
```java
// O(n log n) with binary search
public int lengthOfLIS(int[] nums) {
    int[] tails = new int[nums.length];
    int len = 0;
    for (int num : nums) {
        int i = Arrays.binarySearch(tails, 0, len, num);
        if (i < 0) i = -(i + 1);
        tails[i] = num;
        if (i == len) len++;
    }
    return len;
}
```

### LC 1143: Longest Common Subsequence (Medium)
```java
public int longestCommonSubsequence(String text1, String text2) {
    int m = text1.length(), n = text2.length();
    int[][] dp = new int[m + 1][n + 1];
    for (int i = 1; i <= m; i++)
        for (int j = 1; j <= n; j++)
            dp[i][j] = text1.charAt(i-1) == text2.charAt(j-1) ? dp[i-1][j-1] + 1 : Math.max(dp[i-1][j], dp[i][j-1]);
    return dp[m][n];
}
```

### LC 72: Edit Distance (Hard)
```java
public int minDistance(String word1, String word2) {
    int m = word1.length(), n = word2.length();
    int[][] dp = new int[m + 1][n + 1];
    for (int i = 0; i <= m; i++) dp[i][0] = i;
    for (int j = 0; j <= n; j++) dp[0][j] = j;
    for (int i = 1; i <= m; i++)
        for (int j = 1; j <= n; j++)
            dp[i][j] = word1.charAt(i-1) == word2.charAt(j-1) ? dp[i-1][j-1]
                : 1 + Math.min(dp[i-1][j-1], Math.min(dp[i-1][j], dp[i][j-1]));
    return dp[m][n];
}
```

### LC 322: Coin Change (Medium)
```java
public int coinChange(int[] coins, int amount) {
    int[] dp = new int[amount + 1];
    Arrays.fill(dp, amount + 1);
    dp[0] = 0;
    for (int i = 1; i <= amount; i++)
        for (int coin : coins)
            if (coin <= i) dp[i] = Math.min(dp[i], dp[i - coin] + 1);
    return dp[amount] > amount ? -1 : dp[amount];
}
```

### LC 416: Partition Equal Subset Sum (Medium)
```java
// 0/1 Knapsack
public boolean canPartition(int[] nums) {
    int sum = Arrays.stream(nums).sum();
    if (sum % 2 != 0) return false;
    sum /= 2;
    boolean[] dp = new boolean[sum + 1];
    dp[0] = true;
    for (int num : nums)
        for (int s = sum; s >= num; s--)
            dp[s] = dp[s] || dp[s - num];
    return dp[sum];
}
```

### LC 123: Best Time to Buy and Sell Stock III (Hard)
```java
public int maxProfit(int[] prices) {
    int buy1 = Integer.MIN_VALUE, sell1 = 0, buy2 = Integer.MIN_VALUE, sell2 = 0;
    for (int price : prices) {
        sell2 = Math.max(sell2, buy2 + price);
        buy2 = Math.max(buy2, sell1 - price);
        sell1 = Math.max(sell1, buy1 + price);
        buy1 = Math.max(buy1, -price);
    }
    return sell2;
}
```

### LC 309: Best Time to Buy and Sell Stock with Cooldown (Medium)
```java
public int maxProfit(int[] prices) {
    int prev2 = 0, prev1 = 0, curr = 0;
    int buy = Integer.MIN_VALUE;
    for (int price : prices) {
        int newPrev2 = prev1;
        prev1 = curr;
        curr = Math.max(prev1, buy + price);
        buy = Math.max(buy, prev2 - price);
        prev2 = newPrev2;
    }
    return curr;
}
```

### LC 64: Minimum Path Sum (Medium)
```java
public int minPathSum(int[][] grid) {
    int m = grid.length, n = grid[0].length;
    int[] dp = new int[n];
    Arrays.fill(dp, Integer.MAX_VALUE);
    dp[0] = 0;
    for (int r = 0; r < m; r++)
        for (int c = 0; c < n; c++) {
            dp[c] = c == 0 ? dp[c] + grid[r][c] : Math.min(dp[c], dp[c-1]) + grid[r][c];
        }
    return dp[n-1];
}
```

### LC 518: Coin Change 2 (Medium)
```java
// Unbounded knapsack (order matters)
public int change(int amount, int[] coins) {
    int[] dp = new int[amount + 1];
    dp[0] = 1;
    for (int coin : coins)
        for (int i = coin; i <= amount; i++)
            dp[i] += dp[i - coin];
    return dp[amount];
}
```

### LC 91: Decode Ways (Medium)
```java
public int numDecodings(String s) {
    int n = s.length();
    int[] dp = new int[n + 1];
    dp[0] = 1;
    for (int i = 1; i <= n; i++) {
        if (s.charAt(i-1) != '0') dp[i] += dp[i-1];
        if (i > 1) {
            int two = Integer.parseInt(s.substring(i-2, i));
            if (two >= 10 && two <= 26) dp[i] += dp[i-2];
        }
    }
    return dp[n];
}
```

---

## Practice Roadmap

1. **Foundation:** LC 70, LC 198, LC 322, LC 300
2. **Intermediate:** LC 1143, LC 416, LC 518, LC 91
3. **Advanced:** LC 72, LC 213, LC 309, LC 123
4. **Expert:** LC 312, LC 10, LC 44, LC 174, LC 878
