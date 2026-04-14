# Topic 8: Stacks & Queues

*Document 8 of 20 in the FAANG DSA Prep series.*

Stack = LIFO (most recent first). Queue = FIFO (oldest first). Use `ArrayDeque`, not `Stack` or `LinkedList`.

**Top askers:** Google, Amazon, Meta, Microsoft, Bloomberg

---

## Key Rules

- Use `Deque<T> stack = new ArrayDeque<>()` — never `Stack` (synchronized, slow)
- Stack: `push()`, `pop()`, `peek()`
- Queue: `offer()`, `poll()`, `peek()`
- Prefer `offer/poll` over `add/remove` — they return null/false instead of throwing

---

## Templates

### Template 1: Valid Parentheses
```java
public boolean isValid(String s) {
    Deque<Character> stack = new ArrayDeque<>();
    Map<Character, Character> map = Map.of(')', '(', ']', '[', '}', '{');

    for (char c : s.toCharArray()) {
        if (map.containsKey(c)) {  // closing bracket
            if (stack.isEmpty() || stack.peek() != map.get(c)) return false;
            stack.pop();
        } else {
            stack.push(c);  // opening bracket
        }
    }
    return stack.isEmpty();
}
```

**Trace `"({[]})"`:**
```
'(' push → '('
'{' push → '(', '{'
'[' push → '(', '{', '['
']' pop '[' ✓ → '(', '{'
'}' pop '{' ✓ → '('
')' pop '(' ✓ → empty → VALID
```

### Template 2: Min Stack
```java
class MinStack {
    Deque<Integer> stack = new ArrayDeque<>();
    Deque<Integer> minStack = new ArrayDeque<>();

    public void push(int x) {
        stack.push(x);
        minStack.push(Math.min(x, minStack.peekOrNull()));
    }
    public void pop() { stack.pop(); minStack.pop(); }
    public int top() { return stack.peek(); }
    public int getMin() { return minStack.peek(); }
}
```

### Template 3: Implement Queue from Stacks
```java
class MyQueue {
    Deque<Integer> in = new ArrayDeque<>();
    Deque<Integer> out = new ArrayDeque<>();

    public void push(int x) { in.push(x); }

    public int pop() {
        if (out.isEmpty()) transfer();
        return out.pop();
    }

    public int peek() {
        if (out.isEmpty()) transfer();
        return out.peek();
    }

    private void transfer() {
        while (!in.isEmpty()) out.push(in.pop());
    }
}
```

### Template 4: Implement Stack from Queues
```java
class MyStack {
    Queue<Integer> q = new ArrayDeque<>();

    public void push(int x) {
        q.offer(x);
        for (int i = 0; i < q.size() - 1; i++) q.offer(q.poll());
    }
    public int pop() { return q.poll(); }
    public int top() { return q.peek(); }
}
```

### Template 5: Asteroid Collision
```java
public int[] asteroidCollision(int[] asteroids) {
    Deque<Integer> stack = new ArrayDeque<>();
    for (int a : asteroids) {
        boolean alive = true;
        while (alive && !stack.isEmpty() && stack.peek() > 0 && a < 0) {
            if (stack.peek() < -a) stack.pop();
            else if (stack.peek() == -a) { stack.pop(); alive = false; }
            else alive = false;
        }
        if (alive) stack.push(a);
    }
    int[] result = new int[stack.size()];
    for (int i = result.length - 1; i >= 0; i--) result[i] = stack.pop();
    return result;
}
```

### Template 6: Simplify Path
```java
public String simplifyPath(String path) {
    Deque<String> stack = new ArrayDeque<>();
    for (String dir : path.split("/")) {
        if (dir.equals(".") || dir.isEmpty()) continue;
        if (dir.equals("..")) stack.pop();
        else stack.push(dir);
    }
    StringBuilder sb = new StringBuilder("/");
    Iterator<String> it = stack.descendingIterator();
    while (it.hasNext()) sb.append(it.next()).append("/");
    if (sb.length() > 1) sb.deleteCharAt(sb.length() - 1);
    return sb.toString();
}
```

---

## Problem Solutions

### LC 20: Valid Parentheses (Easy)
```java
// Template 1
public boolean isValid(String s) {
    Deque<Character> stack = new ArrayDeque<>();
    for (char c : s.toCharArray()) {
        if (c == '(' || c == '[' || c == '{') stack.push(c);
        else {
            if (stack.isEmpty()) return false;
            char top = stack.pop();
            if ((c == ')' && top != '(') || (c == ']' && top != '[') || (c == '}' && top != '{')) return false;
        }
    }
    return stack.isEmpty();
}
```

### LC 155: Min Stack (Easy)
```java
// Template 2
class MinStack {
    Deque<Integer> stack = new ArrayDeque<>();
    Deque<Integer> minStack = new ArrayDeque<>();
    public void push(int x) {
        stack.push(x);
        minStack.push(Math.min(x, minStack.isEmpty() ? x : minStack.peek()));
    }
    public void pop() { stack.pop(); minStack.pop(); }
    public int top() { return stack.peek(); }
    public int getMin() { return minStack.peek(); }
}
```

### LC 225: Implement Stack using Queues (Easy)
```java
// Template 4
class MyStack {
    Queue<Integer> q = new ArrayDeque<>();
    public void push(int x) { q.offer(x); for (int i = 0; i < q.size() - 1; i++) q.offer(q.poll()); }
    public int pop() { return q.poll(); }
    public int top() { return q.peek(); }
    public boolean empty() { return q.isEmpty(); }
}
```

### LC 232: Implement Queue using Stacks (Easy)
```java
// Template 3
class MyQueue {
    Deque<Integer> in = new ArrayDeque<>(), out = new ArrayDeque<>();
    public void push(int x) { in.push(x); }
    public int pop() { if (out.isEmpty()) while (!in.isEmpty()) out.push(in.pop()); return out.pop(); }
    public int peek() { if (out.isEmpty()) while (!in.isEmpty()) out.push(in.pop()); return out.peek(); }
    public boolean empty() { return in.isEmpty() && out.isEmpty(); }
}
```

### LC 844: Backspace String Compare (Easy)
```java
public boolean backspaceCompare(String s, String t) {
    return build(s).equals(build(t));
}
private String build(String s) {
    Deque<Character> stack = new ArrayDeque<>();
    for (char c : s.toCharArray()) {
        if (c == '#') { if (!stack.isEmpty()) stack.pop(); }
        else stack.push(c);
    }
    return String.valueOf(stack);
}
```

### LC 71: Simplify Path (Medium)
```java
// Template 6
public String simplifyPath(String path) {
    Deque<String> stack = new ArrayDeque<>();
    for (String p : path.split("/")) {
        if (p.equals(".") || p.isEmpty()) continue;
        if (p.equals("..")) stack.pop();
        else stack.push(p);
    }
    return "/" + String.join("/", stack.descendingIterator().toList().reversed());
}
```

### LC 735: Asteroid Collision (Medium)
```java
// Template 5
public int[] asteroidCollision(int[] asteroids) {
    Deque<Integer> stack = new ArrayDeque<>();
    for (int a : asteroids) {
        boolean alive = true;
        while (alive && !stack.isEmpty() && stack.peek() > 0 && a < 0) {
            if (stack.peek() < -a) stack.pop();
            else if (stack.peek() == -a) { stack.pop(); alive = false; }
            else alive = false;
        }
        if (alive) stack.push(a);
    }
    int[] res = new int[stack.size()];
    for (int i = res.length - 1; i >= 0; i--) res[i] = stack.pop();
    return res;
}
```

### LC 227: Basic Calculator II (Medium)
```java
// Two stacks: numbers + operators. Apply * and / immediately.
public int calculate(String s) {
    Deque<Integer> nums = new ArrayDeque<>();
    Deque<Character> ops = new ArrayDeque<>();
    int num = 0;
    char op = '+';
    for (int i = 0; i <= s.length(); i++) {
        char c = i < s.length() ? s.charAt(i) : '+';
        if (Character.isDigit(c)) num = num * 10 + (c - '0');
        else if (!Character.isWhitespace(c)) {
            if (op == '+') nums.push(num);
            else if (op == '-') nums.push(-num);
            else if (op == '*') nums.push(nums.pop() * num);
            else if (op == '/') nums.push(nums.pop() / num);
            num = 0; op = c;
        }
    }
    int result = 0;
    while (!nums.isEmpty()) result += nums.pop();
    return result;
}
```

### LC 394: Decode String (Medium)
```java
// Stack of (previous result, current index) for nested []
public String decodeString(String s) {
    Deque<Integer> countStack = new ArrayDeque<>();
    Deque<String> resultStack = new ArrayDeque<>();
    int curNum = 0;
    StringBuilder cur = new StringBuilder();
    for (char c : s.toCharArray()) {
        if (Character.isDigit(c)) curNum = curNum * 10 + c - '0';
        else if (c == '[') {
            countStack.push(curNum);
            resultStack.push(cur.toString());
            cur = new StringBuilder();
            curNum = 0;
        } else if (c == ']') {
            int k = countStack.pop();
            String prev = resultStack.isEmpty() ? "" : resultStack.pop();
            cur = new StringBuilder(prev + cur.toString().repeat(k));
        } else {
            cur.append(c);
        }
    }
    return cur.toString();
}
```

---

## Practice Roadmap

1. **Foundation:** LC 20, LC 155, LC 232, LC 225
2. **Intermediate:** LC 844, LC 71, LC 735
3. **Advanced:** LC 227, LC 394, LC 224 (with parentheses)
