package org.play.slidingWindow;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * This class contains retest/duplicate implementations of sliding window solutions.
 * These are alternative approaches to the same problems offered in VariableSlidingWindow.
 * Used for testing different implementation styles and optimization techniques.
 */
public class VariableSlidingWindowRetest {

    /**
     * LC 3 — Longest Substring Without Repeating Characters - UNOPTIMIZED VERSION
     * Less Optimized as it traverses the left window one by one.
     *
     * <p>Problem: Given a string s, find the length of the longest substring without repeating
     * characters.
     *
     * @param s
     * @return
     */
    public static Integer longestSubstringWithNonRepeatingCharsUnOptimized(String s) {
        if (s.isEmpty()) {
            return 0;
        }

        if (s.length() == 1) {
            return 1;
        }

        int left = 0;
        int right = 0;
        int maxLength = Integer.MIN_VALUE;
        int windowLength = 0;

        Map<Character, Integer> charCount = new HashMap<>();

        // ababc

        for (right = 0; right < s.length(); right++) {
            Character c = s.charAt(right);
            windowLength++;
            charCount.put(c, charCount.getOrDefault(c, 0) + 1);

            while (isInvalid(charCount, c)) {
                charCount.put(c, charCount.get(c) - 1);
                if (charCount.get(c) == 0) {
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
     * LC 3 — Longest Substring Without Repeating Characters - ALTERNATIVE IMPLEMENTATION 1
     *
     * <p>Problem: Given a string s, find the length of the longest substring without repeating
     * characters.
     */
    private static int longestSubstringWithoutRepeatingChars(String s) {

        if (s.isEmpty()) return 0;
        if (s.length() == 1) return 1;

        int left = 0;
        int windowLength = 0;

        int maxLength = Integer.MIN_VALUE;

        var map = new HashMap<Character, Integer>();

        for (int right = 0; right < s.length(); right++) {

            // String s = "abcabcbb";

            windowLength++;
            map.put(s.charAt(right), map.getOrDefault(s.charAt(right), 0) + 1);

            while (map.get(s.charAt(right)) > 1) {
                map.put(s.charAt(left), map.get(s.charAt(left)) - 1);
                if (map.get(s.charAt(left)) == 0) {
                    map.remove(s.charAt(left));
                }
                left++;
                windowLength--;
            }

            maxLength = Math.max(windowLength, maxLength);

        }

        return maxLength == Integer.MIN_VALUE ? 0 : maxLength;

    }

    /**
     * LC 3 — Longest Substring Without Repeating Characters - ALTERNATIVE IMPLEMENTATION 2
     *
     * <p>Problem: Given a string s, find the length of the longest substring without repeating
     * characters.
     */
    private static int longestSubstringWithRepeatingChars(String s) {
        if (s.isEmpty()) return 0;
        if (s.length() == 1) return 1;

        Map<Character, Integer> withoutRepeatingChars = new HashMap<>();

        int left = 0;
        int window = 0;
        int maxLength = Integer.MIN_VALUE;

        for (int right = 0; right < s.length(); right++) {
            window++;

            while (s.charAt(left) != s.charAt(right)) {
                window--;
                left++;
            }

            maxLength = Math.max(maxLength, window);

        }

        return maxLength == Integer.MIN_VALUE ? 0 : maxLength;
    }

    /**
     * LC 209 — Minimum Size Subarray Sum - ALTERNATIVE IMPLEMENTATION
     *
     * <p>Problem: Given an array of positive integers nums and a positive integer target, return the
     * minimal length of a subarray whose sum is greater than or equal to target. Return 0 if no such
     * subarray exists.
     *
     * @param arr
     * @param target
     * @return
     */
    public static int minLenSubArrWithGreaterThanEqualToTarget(int arr[], int target) {

        if (arr.length == 0 || (arr.length == 1 && arr[0] < target)) {
            return 0;
        }

        if (arr.length == 1 && arr[0] > target) {
            return 1;
        }

        int minLength = Integer.MAX_VALUE;

        int left = 0;
        int minWindowSum = 0;

        for (int right = 0; right < arr.length; right++) {
            int val = arr[right];
            minWindowSum += val;

            while (minWindowSum >= target) {
                minLength = Math.min(minLength, right - left + 1);
                minWindowSum -= arr[left];
                left++;
            }
        }

        return minLength == Integer.MAX_VALUE ? 0 : minLength;
    }

    /**
     * LC 424 — Longest Repeating Character Replacement - RETEST VERSION
     *
     * <p>Problem: Given a string and an integer atmostCharReplacement, you can replace at most
     * atmostCharReplacement characters in the string. Return the length of the longest substring
     * containing the same letter after performing at mostatmostCharReplacementk replacements.
     *
     * @param string
     * @param atmostCharReplacement
     * @return
     */
    public static Integer longestRepeatingCharReplacementRetest(
            String string, int atmostCharReplacement) {

        if (string.isEmpty()) {
            return 0;
        }

        if (string.length() == 1) {
            return 1;
        }

        string = string.toLowerCase();

        int[] charIntArr = new int[26];
        int maxFreq = 0;

        int left = 0;
        int longestSubstring = Integer.MIN_VALUE;
        for (int right = 0; right < string.length(); right++) {

            char charRight = string.charAt(right);
            charIntArr[charRight - 'a']++;
            maxFreq = Math.max(maxFreq, charIntArr[charRight - 'a']);

            int slidingWindow = right - left + 1;
            if (slidingWindow - maxFreq > atmostCharReplacement) {
                charIntArr[string.charAt(left) - 'a']--;
                left++;
            }

            longestSubstring = Math.max(longestSubstring, right - left + 1);
        }

        return longestSubstring;
    }

    /**
     * LC 930 — Binary Subarrays With Sum - ALTERNATIVE IMPLEMENTATION
     *
     * <p>Problem: Given a binary array nums and an integer goal, return the number of non-empty
     * subarrays with a sum equal to goal.
     */
    public static int totalSumEqualToGoal(int[] arr, int goal) {
        return atmostGoal(arr, goal) - atmostGoal(arr, goal - 1);
    }

    /**
     * Helper method for totalSumEqualToGoal
     */
    public static int atmostGoal(int[] arr, int goal) {
        int count = 0;
        int left = 0;
        int windowSum = 0;

        for (int right = 0; right < arr.length; right++) {
            var val = arr[right];
            windowSum += val;

            while (windowSum > goal) {
                windowSum -= arr[left];
                left++;
            }

            count = right - left + 1;

        }

        return count;
    }

  /**
   * LC 209 — Minimum Size Subarray Sum
   * Difficulty: Medium Companies: Google, Amazon, Microsoft, Meta, Bloomberg, Apple
   *
   * Problem: Given an array of positive integers nums and a positive integer target,
   * return the minimal length of a subarray whose sum is greater than or equal to target.
   * Return 0 if no such subarray exists.
   */
  public static int minSubArrLenGreaterThanEqualToT(int[] arr, int t){

      if(arr.length == 0 || (arr.length == 1 && arr[0] < t)){
          return 0;
      }

      int minLength = Integer.MAX_VALUE;
      int left = 0;
      int windowSum = 0;

      for(int right = 0; right < arr.length; right++){
          windowSum += arr[right];

          while(windowSum >= t){
            int windowLength = right - left +1;
            minLength = Math.min(windowLength, minLength);
            windowSum -= arr[left];
            left++;
          }
      }

      return minLength == Integer.MAX_VALUE? 0: minLength;
  }

  /**
   * LC 930 — Binary Subarrays With Sum
   * Difficulty: Medium Companies: Google, Amazon, Meta
   *
   * Problem: Given a binary array nums and an integer goal,
   * return the number of non-empty subarrays with a sum equal to goal.
   *
   */
    private static int nonEmptySubArrSumEqualGoal(int[] arr, int goal){
        return atmostSumEqualGoal(arr, goal) - atmostSumEqualGoal(arr, goal -1);
    }

    private static int atmostSumEqualGoal(int[] arr, int goal){
        if(goal < 0){
            return 0;
        }

        int left = 0;
        int count = 0;
        int windowSum = 0;

        for (int right = 0; right < arr.length; right++) {
            windowSum += arr[right];

            while (windowSum >= goal){
                windowSum -= arr[left];
                left++;
            }

            count += right - left + 1;
        }

        return count;
    }

  /**
   * LC 438 — Find All Anagrams in a String
   * Difficulty: Medium Companies: Amazon, Google, Meta, Microsoft, Bloomberg
   *
   * Problem: Given strings s and p, return an array of all the start indices of p's anagrams in s.
   */
  public static Integer[] anagramArr(String s, String p){
      if(s.length() < p.length()){
          return new Integer[0];
      }

      var prr = new int[26];
      var wrr = new int[26];

      for(char c: p.toCharArray()){
          prr[c - 'a']++;
      }

      var required = 0;
      for(int val : prr){
          if(val>0){
              required++;
          }
      }

      int left = 0;
      int formed = 0;
      var startIndices = new ArrayList<Integer>();

      // s = abcbacd, p = cab, return = 3
      for(int right = 0; right < s.length(); right++){
          char rightChar = s.charAt(right);

          wrr[rightChar - 'a']++;
          if(wrr[rightChar - 'a'] > 0 && wrr[rightChar - 'a'] == prr[rightChar - 'a']){
              formed++;
          }

       if (right - left + 1 > p.length()){
               char leftChar = s.charAt(left);
              if(wrr[leftChar - 'a'] > 0 && wrr[leftChar - 'a'] == prr[leftChar - 'a']){
                  formed--;
              }
              wrr[leftChar - 'a']--;
              left++;
          }

          if(right - left + 1 == p.length() && formed == required){
              startIndices.add(left);
          }
      }

      return startIndices.toArray(new Integer[0]);
  }

}
