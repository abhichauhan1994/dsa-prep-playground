package org.play.slidingWindow;

import java.util.HashMap;
import java.util.Map;

public class VariableSlidingWindow {

    public static int maxFruitsIntoBasketOfSameColor(int arr[]){
        if(arr.length == 0){
            return 0;
        }

        if(arr.length == 1){
            return 1;
        }

        int maxFruitsIntoBasket = Integer.MIN_VALUE;
        int left = 0;
        int right = 0;

        Map<Integer, Integer> countMap = new HashMap<>();

        while(right < arr.length){
            countMap.put(arr[right], countMap.getOrDefault(arr[right], 0) +1);

            while(isInvalid(countMap)){
                countMap.put(arr[left], countMap.get(arr[left]) -1);
                if(countMap.get(arr[left]) == 0){
                    countMap.remove(arr[left]);
                }
                left++;
            }

            maxFruitsIntoBasket = Math.max(maxFruitsIntoBasket, right - left + 1);
            right++;
        }

        return maxFruitsIntoBasket;
    }

    public static boolean isInvalid(Map<Integer, Integer> map){
        return map.size() > 1;
    }


    /**
     *  less Optimized as it traverses the left window one by one. Check @longestSubstringWithNonRepeatingCharsOptimized
     * LC 3 — Longest Substring Without Repeating Characters
     * Difficulty: Medium Companies: Amazon, Google, Meta, Microsoft, Bloomberg, Adobe, Apple, Uber, Goldman Sachs, TikTok, Yandex (112 companies total)
     *
     * Problem: Given a string s, find the length of the longest substring without repeating characters.
     *
     * @param s
     * @return
     */
    public static Integer longestSubstringWithNonRepeatingCharsUnOptimized(String s){
        if(s.isEmpty()){
            return 0;
        }

        if(s.length() == 1){
            return 1;
        }

        int left = 0;
        int right = 0;
        int maxLength = Integer.MIN_VALUE;
        int windowLength = 0;

        Map<Character, Integer> charCount = new HashMap<>();

        // ababc

        for(right = 0; right < s.length(); right++){
            Character c = s.charAt(right);
            windowLength++;
            charCount.put(c, charCount.getOrDefault(c, 0) + 1);

            while(isInvalid(charCount, c)){
                charCount.put(c, charCount.get(c) - 1);
                if(charCount.get(c) == 0){
                    charCount.remove(c);
                }

                windowLength--;
                left++;
            }

            maxLength = Math.max(maxLength, windowLength);
        }

        return maxLength;
    }

    private static boolean isInvalid(Map<Character, Integer> charCount, Character c) {
        return charCount.get(c) > 1;
    }


  /**
   *  More Optimized as it left window jumps directly. Check @longestSubstringWithNonRepeatingCharsUnOptimized
   * LC 3 — Longest Substring Without Repeating Characters
   * Difficulty: Medium Companies: Amazon, Google, Meta, Microsoft, Bloomberg, Adobe, Apple, Uber, Goldman Sachs, TikTok, Yandex (112 companies total)
   *
   * Problem: Given a string s, find the length of the longest substring without repeating characters.
   *
   * @param s
   * @return
   */
  public static Integer longestSubstringWithNonRepeatingCharsOptimized(String s) {
        if(s.isEmpty()){
            return 0;
        }

        if(s.length() == 1){
            return 1;
        }

        int left = 0;
        int maxLength = Integer.MIN_VALUE;

        Map<Character, Integer> charLastIndexMap = new HashMap<>();

        // abcabcbb
        for(int right = 0; right < s.length(); right++){
            Character c = s.charAt(right);

            if(charLastIndexMap.containsKey(c)){
                left = charLastIndexMap.get(c) + 1;
            }

            charLastIndexMap.put(c, right);
            maxLength = Math.max(maxLength, right - left + 1);
        }

        return maxLength;
    }

  /**
   * LC 209 — Minimum Size Subarray Sum
   * Difficulty: Medium Companies: Google, Amazon, Microsoft, Meta, Bloomberg, Apple
   *
   * Problem: Given an array of positive integers nums and a positive integer target, return the minimal length of a subarray whose sum is greater than or equal to target. Return 0 if no such subarray exists.
   * @param target
   * @param nums
   * @return
   */
  public static Integer minimumSizeSubarraySum(int target, int[] nums) {
        if(nums.length == 0 ||(nums.length == 1 && nums[0] < target)){
            return 0;
        }

        if(nums.length == 1){
            return 1;
        }

        int minimumSizeSubarraySum = Integer.MAX_VALUE;
        int windowSum = 0;
        int left = 0;

      for (int right = 0; right < nums.length; right++) {
          windowSum += nums[right];

          while (windowSum >= target) {
              minimumSizeSubarraySum = Math.min(minimumSizeSubarraySum, right - left +1);
              windowSum -= nums[left];
              left++;
          }
      }

      return minimumSizeSubarraySum == Integer.MAX_VALUE ? 0 : minimumSizeSubarraySum;
    }

  /**
   * LC 424 — Longest Repeating Character Replacement
   * Difficulty: Medium Companies: Google, Amazon, Microsoft, Meta, Bloomberg
   *
   * Problem: Given a string string and an integer atmostCharReplacement, you can replace at most atmostCharReplacement characters in the string. Return the length of the longest substring containing the same letter after performing at mostatmostCharReplacementk replacements.
   *
   * @param string
   * @param atmostCharReplacement
   * @return
   */
  public static Integer longestRepeatingCharReplacement(String string, int atmostCharReplacement) {

      if(string.isEmpty()){
          return 0;
      }

      int maxLength = Integer.MIN_VALUE;

      int left = 0;
      int[] freq = new int[26];
      int maxFreq = 0;

      for(int right = 0; right < string.length(); right++){
          char ch = string.charAt(right);
          freq[ch - 'A']++;
          maxFreq = Math.max(maxFreq, freq[ch - 'A']);

          int slidingWindow = right - left + 1;

          if(slidingWindow - maxFreq > atmostCharReplacement){
              freq[string.charAt(left) - 'A']--;
              left++;
          }

          maxLength = Math.max(maxLength, right-left+1);
      }

      return maxLength;
    }

    public static void runMinimumSizeSubarraySumTests() {
        System.out.println("=== runMinimumSizeSubarraySumTests ===\n");

        testEmptyArray209();
        testSingleElementGreaterOrEqual();
        testSingleElementLess();
        testMultipleElementsWithValidSubarray();
        testNoValidSubarray();
    }

    private static void testEmptyArray209() {
        int[] nums = {};
        int target = 1;
        int result = minimumSizeSubarraySum(target, nums);
        System.out.println("Test 1 - Empty array: nums=" + java.util.Arrays.toString(nums) + ", target=" + target);
        System.out.println("  Expected: 0, Got: " + result + ", " + (result == 0 ? "PASS" : "FAIL") + "\n");
    }

    private static void testSingleElementGreaterOrEqual() {
        int[] nums = {5};
        int target = 5;
        int result = minimumSizeSubarraySum(target, nums);
        System.out.println("Test 2 - Single element >= target: nums=" + java.util.Arrays.toString(nums) + ", target=" + target);
        System.out.println("  Expected: 1, Got: " + result + ", " + (result == 1 ? "PASS" : "FAIL") + "\n");
    }

    private static void testSingleElementLess() {
        int[] nums = {3};
        int target = 5;
        int result = minimumSizeSubarraySum(target, nums);
        System.out.println("Test 3 - Single element < target: nums=" + java.util.Arrays.toString(nums) + ", target=" + target);
        System.out.println("  Expected: 0, Got: " + result + ", " + (result == 0 ? "PASS" : "FAIL") + "\n");
    }

    private static void testMultipleElementsWithValidSubarray() {
        int[] nums = {2, 3, 1, 2, 4, 3};
        int target = 7;
        int result = minimumSizeSubarraySum(target, nums);
        int retest1 = minLenSubArrWithGreaterThanEqualToTarget(nums, target);
        System.out.println("Test 4 - Multiple elements with valid subarray: nums=" + java.util.Arrays.toString(nums) + ", target=" + target);
        System.out.println("  Expected: 2, Got: " + result + ", " + (result == 2 ? "PASS" : "FAIL") + "\n");

        System.out.println("Test 4 Retest - Multiple elements with valid subarray: nums=" + java.util.Arrays.toString(nums) + ", target=" + target);
        System.out.println("  Expected: 2, Got: " + retest1 + ", " + (retest1 == 2 ? "PASS" : "FAIL") + "\n");
    }

    private static void testNoValidSubarray() {
        int[] nums = {1, 2, 3};
        int target = 10;
        int result = minimumSizeSubarraySum(target, nums);
        System.out.println("Test 5 - No valid subarray: nums=" + java.util.Arrays.toString(nums) + ", target=" + target);
        System.out.println("  Expected: 0, Got: " + result + ", " + (result == 0 ? "PASS" : "FAIL") + "\n");
    }


    public static void runLongestSubstringTests() {
        System.out.println("=== runLongestSubstringTests ===\n");

        testEmptyString();
        testSingleChar();
        testAllUnique();
        testWithRepeats();
    }

    private static void testEmptyString() {
        String s = "";
        int result = longestSubstringWithNonRepeatingCharsOptimized(s);
        System.out.println("Test 1 - Empty string: s=\"" + s + "\"");
        System.out.println(
            "  Expected: 0, Got: " + result + ", " + (result == 0 ? "PASS" : "FAIL") + "\n");
    }

    private static void testSingleChar() {
        String s = "a";
        int result = longestSubstringWithNonRepeatingCharsOptimized(s);
        System.out.println("Test 2 - Single char: s=\"" + s + "\"");
        System.out.println(
            "  Expected: 1, Got: " + result + ", " + (result == 1 ? "PASS" : "FAIL") + "\n");
    }

    private static void testAllUnique() {
        String s = "abc";
        int result = longestSubstringWithNonRepeatingCharsOptimized(s);
        System.out.println("Test 3 - All unique: s=\"" + s + "\"");
        System.out.println(
            "  Expected: 3, Got: " + result + ", " + (result == 3 ? "PASS" : "FAIL") + "\n");
    }

    private static void testWithRepeats() {
        String s = "abcabcbb";
        int result = longestSubstringWithNonRepeatingCharsOptimized(s);
        System.out.println("Test 4 - With repeats: s=\"" + s + "\"");
        System.out.println(
            "  Expected: 3, Got: " + result + ", " + (result == 3 ? "PASS" : "FAIL") + "\n");
    }

    public static void runMaxFruitsIntoBasketOfSameColorTests() {
        System.out.println("=== maxFruitsIntoBasketOfSameColorTests ===\n");

        testSingleColorOnly();
        testMultipleColorsWithLongestSingle();
        testEmptyArray();
        testSingleElement();
    }

    private static void testSingleColorOnly() {
        int[] arr = {1, 1, 1, 1, 1};
        int result = maxFruitsIntoBasketOfSameColor(arr);
        System.out.println("Test 1 - Single color only: arr=" + java.util.Arrays.toString(arr));
        System.out.println("  Expected: 5, Got: " + result + ", " + (result == 5 ? "PASS" : "FAIL") + "\n");
    }

    private static void testMultipleColorsWithLongestSingle() {
        int[] arr = {1, 2, 1, 1, 1, 2, 2, 2, 2 ,2};
        int result = maxFruitsIntoBasketOfSameColor(arr);
        System.out.println("Test 2 - Multiple colors, longest single: arr=" + java.util.Arrays.toString(arr));
        System.out.println("  Expected: 5, Got: " + result + ", " + (result == 5 ? "PASS" : "FAIL") + "\n");
    }

    private static void testEmptyArray() {
        int[] arr = {};
        int result = maxFruitsIntoBasketOfSameColor(arr);
        System.out.println("Test 3 - Empty array: arr=" + java.util.Arrays.toString(arr));
        System.out.println("  Expected: 0, Got: " + result + ", " + (result == 0 ? "PASS" : "FAIL") + "\n");
    }

    private static void testSingleElement() {
        int[] arr = {5};
        int result = maxFruitsIntoBasketOfSameColor(arr);
        System.out.println("Test 4 - Single element: arr=" + java.util.Arrays.toString(arr));
        System.out.println("  Expected: 1, Got: " + result + ", " + (result == 1 ? "PASS" : "FAIL") + "\n");
    }

    public static void runLongestRepeatingCharReplacementTests() {
        System.out.println("=== runLongestRepeatingCharReplacementTests ===\n");

        testEmptyStringCharReplacement();
        testSingleCharCharReplacement();
        testAllSameCharacter();
        testMixedCharactersWithK2();
        testMixedCharactersWithK1();
        testNoReplacementNeeded();
        testHighKValue();
    }

    private static void testEmptyStringCharReplacement() {
        String s = "";
        int k = 0;
        int result = longestRepeatingCharReplacement(s, k);
        System.out.println("Test 1 - Empty string: s=\"" + s + "\", k=" + k);
        System.out.println("  Expected: 0, Got: " + result + ", " + (result == 0 ? "PASS" : "FAIL") + "\n");
    }

    private static void testSingleCharCharReplacement() {
        String s = "A";
        int k = 0;
        int result = longestRepeatingCharReplacement(s, k);
        System.out.println("Test 2 - Single character: s=\"" + s + "\", k=" + k);
        System.out.println("  Expected: 1, Got: " + result + ", " + (result == 1 ? "PASS" : "FAIL") + "\n");
    }

    private static void testAllSameCharacter() {
        String s = "AAAA";
        int k = 2;
        int result = longestRepeatingCharReplacement(s, k);
        System.out.println("Test 3 - All same character: s=\"" + s + "\", k=" + k);
        System.out.println("  Expected: 4, Got: " + result + ", " + (result == 4 ? "PASS" : "FAIL") + "\n");
    }

    private static void testMixedCharactersWithK2() {
        String s = "ABAB";
        int k = 2;
        int result = longestRepeatingCharReplacement(s, k);
        System.out.println("Test 4 - Mixed characters (ABAB with k=2): s=\"" + s + "\", k=" + k);
        System.out.println("  Expected: 4, Got: " + result + ", " + (result == 4 ? "PASS" : "FAIL") + "\n");
    }

    private static void testMixedCharactersWithK1() {
        String s = "ABAB";
        int k = 1;
        int result = longestRepeatingCharReplacement(s, k);
        System.out.println("Test 5 - Mixed characters (ABAB with k=1): s=\"" + s + "\", k=" + k);
        System.out.println("  Expected: 3, Got: " + result + ", " + (result == 3 ? "PASS" : "FAIL") + "\n");
    }

    private static void testNoReplacementNeeded() {
        String s = "AABBA";
        int k = 0;
        int result = longestRepeatingCharReplacement(s, k);
        System.out.println("Test 6 - No replacement needed (consecutive same): s=\"" + s + "\", k=" + k);
        System.out.println("  Expected: 2, Got: " + result + ", " + (result == 2 ? "PASS" : "FAIL") + "\n");
    }

    private static void testHighKValue() {
        String s = "ABCDE";
        int k = 5;
        int result = longestRepeatingCharReplacement(s, k);

        System.out.println("Test 7 - High k value (k >= length): s=\"" + s + "\", k=" + k);
        System.out.println("  Expected: 5, Got: " + result + ", " + (result == 5 ? "PASS" : "FAIL") + "\n");

        String s1 = "ABCDEFG";
        int k1 = 5;
        int resultReTest = longestRepeatingCharReplacementRetest(s1,k1);


        System.out.println("Test 7 - High k value (k >= length): s=\"" + s1 + "\", k=" + k1);
        System.out.println("  Expected: 5, Got: " + resultReTest + ", " + (resultReTest == 5 ? "PASS" : "FAIL") + "\n");


    }

    public static int minLenSubArrWithGreaterThanEqualToTarget(int arr[], int target){

      if(arr.length == 0 || (arr.length == 1 && arr[0] < target)){
          return 0;
      }

      if(arr.length == 1 && arr[0] > target){
          return 1;
      }

      int minLength = Integer.MAX_VALUE;

      int left = 0;
      int minWindowSum = 0;
      
      for(int right = 0; right < arr.length; right++){
          int val = arr[right];
          minWindowSum += val;

          while(minWindowSum >= target){
              minLength = Math.min(minLength, right - left +1);
              minWindowSum -= arr[left];
              left++;
          }

      }

      return minLength == Integer.MAX_VALUE? 0 : minLength;
    }

    /**
     * LC 424 — Longest Repeating Character Replacement
     * Difficulty: Medium Companies: Google, Amazon, Microsoft, Meta, Bloomberg
     *
     * Problem: Given a string and an integer atmostCharReplacement, you can replace at most atmostCharReplacement characters in the string. Return the length of the longest substring containing the same letter after performing at mostatmostCharReplacementk replacements.
     *
     * @param string
     * @param atmostCharReplacement
     * @return
     */
    public static Integer longestRepeatingCharReplacementRetest(String string, int atmostCharReplacement) {

        if(string.isEmpty()){
            return 0;
        }

        if(string.length() == 1){
            return 1;
        }

        string = string.toLowerCase();

        int[] charIntArr = new int[26];
        int maxFreq = 0;

        int left = 0;
        int longestSubstring = Integer.MIN_VALUE;
        for(int right = 0; right < string.length(); right++){

            char charRight = string.charAt(right);
            charIntArr[charRight - 'a']++;
            maxFreq = Math.max(maxFreq, charIntArr[charRight - 'a']);

            int slidingWindow = right - left + 1;
            if(slidingWindow - maxFreq > atmostCharReplacement){
                charIntArr[string.charAt(left) - 'a']--;
                left++;
            }

            longestSubstring = Math.max(longestSubstring, right - left + 1);
        }

        return longestSubstring;
    }

  /**
   * LC 930 — Binary Subarrays With Sum
   * Difficulty: Medium Companies: Google, Amazon, Meta
   *
   * Problem: Given a binary array nums and an integer goal, return the number of non-empty subarrays with a sum equal to goal.
   *
   * Why sliding window: Count subarrays with EXACTLY goal sum. Use Template 5: atMost(goal) - atMost(goal - 1).
   *
   * @param nums
   * @param goal
   * @return
   */
  public int numSubarraysWithSum(int[] nums, int goal) {
      return atMost(nums, goal) - atMost(nums, goal - 1);
  }

    private int atMost(int[] nums, int goal) {
        if (goal < 0) return 0; // Edge case: goal-1 when goal=0

        int left = 0;
        int windowSum = 0;
        int count = 0;

        for (int right = 0; right < nums.length; right++) {
            windowSum += nums[right];

            // Shrink while sum exceeds goal
            while (windowSum > goal) {
                windowSum -= nums[left];
                left++;
            }

            // All subarrays ending at right with start from left to right are valid
            count += right - left + 1;
        }

        return count;
    }

}


//public class Vehicle {
//        static {
//            System.out.println("Inside Vehicle static block");//1
//        }
//        Vehicle() {
//            System.out.println("Inside Vehicle constructor");//2
//        }
//
//        {
//            System.out.println("Inside Vehicle instance block");//3
//        }
//    }
//    public class Car extends Vehicle {
//        static {
//            System.out.println("Inside car static block");//4
//        }
//
//        Car() {
//            System.out.println("Inside car constructor");//5
//        }
//
//        {
//            System.out.println("Inside car instance block");//6
//        }
//    }
//    public class Runner {
//        public static void main(String[] args){
//            Car car = new Car();
//        }


        // empId
        // depId
        // highestSalary

        // select e.Id as empId, id as depId, max(e.salary) as maxSalary from dept
        // LEFT JOIN employee e ON e.deptId = dept.Id
        // group by empId, depId;

//        SELECT
//        d.id AS depId,
//        e.id AS empId,
//        e.salary AS maxSalary
//        FROM dept d
//        LEFT JOIN (SELECT *, ROW_NUMBER() OVER (PARTITION BY dept_id ORDER BY salary DESC)
//        AS rn FROM employee) e ON d.id = e.dept_id AND e.rn = 1;

//        SELECT
//        d.id AS depId,
//        d.name AS depName,
//        e.id AS empId,
//        e.name AS empName,
//        e.salary AS maxSalary
//        FROM dept d
//        LEFT JOIN (
//                SELECT *,
//                ROW_NUMBER() OVER (PARTITION BY dept_id ORDER BY salary DESC) AS rn
//        FROM employee
//) e ON d.id = e.dept_id AND e.rn = 1;


//        SELECT
//        e.id AS empId,
//        e.dept_id AS depId,
//        e.salary AS maxSalary
//        FROM employee e
//        WHERE e.salary = (
//        SELECT MAX(e2.salary)
//        FROM employee e2
//        WHERE e2.dept_id = e.dept_id );


        // db.collection_name.find({empId = "xyz"});


//        String s1 = new String("hello");
//        String s2 = new String("hello");
//        String s3 = s1;
//
//        System.out.println(s1 == s2);
//        System.out.println(s1 == s3);

        // true
//    }