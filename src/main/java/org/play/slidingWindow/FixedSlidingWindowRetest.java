package org.play.slidingWindow;

import java.util.HashSet;
import java.util.Set;

/**
 * This class contains retest/duplicate implementations of sliding window solutions.
 * These are alternative approaches to the same problems offered in FixedSlidingWindow.
 * Used for testing different implementation styles and optimization techniques.
 */
public class FixedSlidingWindowRetest {

    /**
     * LC 567 — Permutation in String - ALTERNATIVE IMPLEMENTATION
     * Difficulty: Medium Companies: Google, Amazon, Microsoft, Meta, Bloomberg
     *
     * <p>Problem: Given strings s1 and s2, return true if s2 contains a permutation of s1. In other
     * words, one of s1's permutations is a substring of s2.
     *
     * <p>This is an alternative implementation using windowCount instead of formed variable, with
     * slightly different variable naming conventions.
     *
     * @param s1
     * @param s2
     * @return
     */
    public static Boolean checkPermutationInclusion(String s1, String s2) {
        if (s1.length() > s2.length()) {
            return false;
        }
        s1 = s1.toLowerCase();
        s2 = s2.toLowerCase();

        int[] s1Arr = new int[26];
        int[] s2Arr = new int[26];

        for (char ch : s1.toCharArray()) {
            s1Arr[ch - 'a']++;
        }

        int required = 0;
        for (int val : s1Arr) {
            if (val > 0) {
                required++;
            }
        }

        int left = 0;
        int windowCount = 0;

        for (int right = 0; right < s2.length(); right++) {
            char rightChar = s2.charAt(right);
            s2Arr[rightChar - 'a']++;

            if (s1Arr[rightChar - 'a'] > 0 && s1Arr[rightChar - 'a'] == s2Arr[rightChar - 'a']) {
                windowCount++;
            }

            if (right - left + 1 > s1.length()) {
                char leftChar = s2.charAt(left);
                if (s2Arr[leftChar - 'a'] > 0 && s1Arr[leftChar - 'a'] == s2Arr[leftChar - 'a']) {
                    windowCount--;
                }
                left++;
                s2Arr[leftChar - 'a']--;
            }

            if (right - left + 1 == s1.length() && windowCount == required) {
                return true;
            }
        }

        return false;
    }

    /**
     * LC 1456 — Maximum Number of Vowels in a Substring of Given Length
     * Difficulty: Medium Companies: Amazon, Google, Meta, Microsoft
     *
     * Problem: Given a string s and an integer k, return the maximum number of vowel letters in any substring of s with length k.
     */
    public static int maxVowelsInWindow(String s, int k){
        if (s.length() < k){
            return 0;
        }

        int maxVowels = Integer.MIN_VALUE;
        int windowVowels = 0;
        int left = 0;

        var vowels = Set.of('a','e','i','o','u');

        for(int right = 0; right < s.length(); right++){
            char rightChar = s.charAt(right);

            if(vowels.contains(rightChar)){
                windowVowels++;
            }

            if(right - left + 1 > k){
                char leftChar = s.charAt(left);
                if(vowels.contains(leftChar)){
                    windowVowels--;
                }
                left++;
            }

            if(right - left + 1 == k){
                maxVowels = Math.max(maxVowels, windowVowels);
            }
        }

        return maxVowels;
    }
}

