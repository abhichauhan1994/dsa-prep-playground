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
     * Problem: Given a string s and an integer k, you can replace at most k characters in the string.
     * Return the length of the longest substring containing the same letter after performing at most k
     * replacements.
     */
    public static Integer longestRepeatingCharacterReplacement(String s, int k){
            int[] freq = new int[26];
            int left = 0;
            int maxFreq = 0; // Maximum frequency of any character in the current window
            int result = 0;

            for (int right = 0; right < s.length(); right++) {
                // Add character at right
                freq[s.charAt(right) - 'A']++;

                // Update maxFreq — only need to check the newly added character
                maxFreq = Math.max(maxFreq, freq[s.charAt(right) - 'A']);

                // Window size = right - left + 1
                // Characters to replace = windowSize - maxFreq
                // If this exceeds k, shrink the window by one
                int windowSize = right - left + 1;
                if (windowSize - maxFreq > k) {
                    // Shrink: remove the leftmost character
                    freq[s.charAt(left) - 'A']--;
                    left++;
                    // Note: we don't update maxFreq here — see explanation below
                }

                // Window is now valid (or same size as before)
                result = Math.max(result, right - left + 1);
            }

            return result;
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
        System.out.println("Test 4 - Multiple elements with valid subarray: nums=" + java.util.Arrays.toString(nums) + ", target=" + target);
        System.out.println("  Expected: 2, Got: " + result + ", " + (result == 2 ? "PASS" : "FAIL") + "\n");
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

}