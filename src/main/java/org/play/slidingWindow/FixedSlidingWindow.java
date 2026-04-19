package org.play.slidingWindow;

import java.util.Arrays;
import java.util.Set;

public class FixedSlidingWindow {

    public static int maxSumOfSubarrayOfSizeK(int[] arr, int k){
        if(arr.length < k){
            return -1;
        }

        int sum = 0;
        for(int i = 0; i < k; i++){
            sum += arr[i];
        }
        int maxSum = sum;

        for(int right = k; right < arr.length; right++){
            sum = sum + arr[right] - arr[right - k];
            maxSum = Math.max(maxSum, sum);
        }

        return maxSum;
    }


    /*
     * LC 643 — Maximum Average Subarray I
     */
    public static double maxAverageSubArrayOfSizeK(int[] arr, int k){

        if(arr.length < k){
            return -1;
        }

        double sum = 0;
        for(int i = 0; i < k; i++){
            sum += arr[i];
        }
        double maxSum = sum;

        for(int right = k; right < arr.length; right++){
            sum = sum + arr[right] - arr[right - k];
            maxSum = Math.max(sum, maxSum);
        }

        return maxSum/k;
    }

    /*
     * LC 1456 — Maximum Number of Vowels in a Substring of Given Length
     */
    public static int maxVowelsInSubstringOfSizeK(String s, int k){
        s = s.toLowerCase();
        if(s.length() < k){
            return 0;
        }

        Set<Character> charSet = Set.of('a','e','i','o','u');
        int windowVowelCount = 0;

        for(int i = 0; i < k; i++){
            if(charSet.contains(s.charAt(i))){
                windowVowelCount++;
            }
        }

        int maxVowelsCount = windowVowelCount;

        for(int right = k; right < s.length(); right++){
            if(charSet.contains(s.charAt(right))){
                windowVowelCount++;
            }
            if(charSet.contains(s.charAt(right - k))){
                windowVowelCount--;
            }
            maxVowelsCount = Math.max(maxVowelsCount, windowVowelCount);
        }

        return maxVowelsCount;
    }

    public static void runMaxSumOfSubarrayOfSizeKTests() {
        System.out.println("=== testMaxSumOfSubarrayOfSizeK ===\n");

        testBasicMixedArray();
        testAllPositive();
        testAllNegative();
        testKEqualsArrayLength();
        testKEqualsOne();
        testSingleElementArray();
        testKGreaterThanArrayLength();
        testLargeArray();
    }

    private static void testBasicMixedArray() {
        int[] arr = {1, 12, -5, -6, 50, 3};
        int k = 4;
        int result = maxSumOfSubarrayOfSizeK(arr, k);
        System.out.println("Test 1 - Basic mixed array: arr=" + Arrays.toString(arr) + ", k=" + k);
        System.out.println("  Expected: 51, Got: " + result + ", " + (result == 51 ? "PASS" : "FAIL") + "\n");
    }

    private static void testAllPositive() {
        int[] arr = {2, 1, 5, 1, 3, 2};
        int k = 3;
        int result = maxSumOfSubarrayOfSizeK(arr, k);
        System.out.println("Test 2 - All positive: arr=" + Arrays.toString(arr) + ", k=" + k);
        System.out.println("  Expected: 9, Got: " + result + ", " + (result == 9 ? "PASS" : "FAIL") + "\n");
    }

    private static void testAllNegative() {
        int[] arr = {-2, -1, -3, -5, -1};
        int k = 2;
        int result = maxSumOfSubarrayOfSizeK(arr, k);
        System.out.println("Test 3 - All negative: arr=" + Arrays.toString(arr) + ", k=" + k);
        System.out.println("  Expected: -3, Got: " + result + ", " + (result == -3 ? "PASS" : "FAIL") + "\n");
    }

    private static void testKEqualsArrayLength() {
        int[] arr = {5, 10, 15, 20};
        int k = 4;
        int result = maxSumOfSubarrayOfSizeK(arr, k);
        System.out.println("Test 4 - k equals array length: arr=" + Arrays.toString(arr) + ", k=" + k);
        System.out.println("  Expected: 50, Got: " + result + ", " + (result == 50 ? "PASS" : "FAIL") + "\n");
    }

    private static void testKEqualsOne() {
        int[] arr = {10, 20, 30, 40};
        int k = 1;
        int result = maxSumOfSubarrayOfSizeK(arr, k);
        System.out.println("Test 5 - k=1 (single element): arr=" + Arrays.toString(arr) + ", k=" + k);
        System.out.println("  Expected: 40, Got: " + result + ", " + (result == 40 ? "PASS" : "FAIL") + "\n");
    }

    private static void testSingleElementArray() {
        int[] arr = {7};
        int k = 1;
        int result = maxSumOfSubarrayOfSizeK(arr, k);
        System.out.println("Test 6 - Single element array: arr=" + Arrays.toString(arr) + ", k=" + k);
        System.out.println("  Expected: 7, Got: " + result + ", " + (result == 7 ? "PASS" : "FAIL") + "\n");
    }

    private static void testKGreaterThanArrayLength() {
        int[] arr = {1, 2, 3};
        int k = 5;
        int result = maxSumOfSubarrayOfSizeK(arr, k);
        System.out.println("Test 7 - k > array length: arr=" + Arrays.toString(arr) + ", k=" + k);
        System.out.println("  Expected: -1, Got: " + result + ", " + (result == -1 ? "PASS" : "FAIL") + "\n");
    }

    private static void testLargeArray() {
        int[] arr = new int[10000];
        for (int i = 0; i < 10000; i++) arr[i] = (i % 2 == 0) ? 1000 : -500;
        int k = 5000;
        int result = maxSumOfSubarrayOfSizeK(arr, k);
        System.out.println("Test 8 - Large array (10000 elements, k=5000)");
        System.out.println("  Expected: 1250000, Got: " + result + ", " + (result == 1250000 ? "PASS" : "FAIL") + "\n");
    }

    public static void runMaxAverageSubArrayOfSizeKTests() {
        System.out.println("=== testMaxAverageSubArrayOfSizeK ===\n");

        testLC643Basic();
        testAllPositiveAverage();
        testAllSameAverage();
        testKEqualsOneAverage();
        testKEqualsArrayLengthAverage();
        testKGreaterThanArrayLengthAverage();
        testSingleElementAverage();
        testDecimalAverage();
    }

    private static void testLC643Basic() {
        int[] arr = {1, 12, -5, -6, 50, 3};
        int k = 4;
        double result = maxAverageSubArrayOfSizeK(arr, k);
        System.out.println("Test 1 - LC 643 Basic: arr=" + Arrays.toString(arr) + ", k=" + k);
        System.out.printf("  Expected: 12.75, Got: %.2f, %s%n%n", result, Math.abs(result - 12.75) < 0.01 ? "PASS" : "FAIL");
    }

    private static void testAllPositiveAverage() {
        int[] arr = {1, 2, 3, 4, 5, 6};
        int k = 3;
        double result = maxAverageSubArrayOfSizeK(arr, k);
        System.out.println("Test 2 - All positive: arr=" + Arrays.toString(arr) + ", k=" + k);
        System.out.printf("  Expected: 5.0, Got: %.2f, %s%n%n", result, Math.abs(result - 5.0) < 0.01 ? "PASS" : "FAIL");
    }

    private static void testAllSameAverage() {
        int[] arr = {5, 5, 5, 5, 5};
        int k = 2;
        double result = maxAverageSubArrayOfSizeK(arr, k);
        System.out.println("Test 3 - All same: arr=" + Arrays.toString(arr) + ", k=" + k);
        System.out.printf("  Expected: 5.0, Got: %.2f, %s%n%n", result, Math.abs(result - 5.0) < 0.01 ? "PASS" : "FAIL");
    }

    private static void testKEqualsOneAverage() {
        int[] arr = {10, 20, 30, 40, 50};
        int k = 1;
        double result = maxAverageSubArrayOfSizeK(arr, k);
        System.out.println("Test 4 - k=1: arr=" + Arrays.toString(arr) + ", k=" + k);
        System.out.printf("  Expected: 50.0, Got: %.2f, %s%n%n", result, Math.abs(result - 50.0) < 0.01 ? "PASS" : "FAIL");
    }

    private static void testKEqualsArrayLengthAverage() {
        int[] arr = {1, 2, 3, 4};
        int k = 4;
        double result = maxAverageSubArrayOfSizeK(arr, k);
        System.out.println("Test 5 - k equals array length: arr=" + Arrays.toString(arr) + ", k=" + k);
        System.out.printf("  Expected: 2.5, Got: %.2f, %s%n%n", result, Math.abs(result - 2.5) < 0.01 ? "PASS" : "FAIL");
    }

    private static void testKGreaterThanArrayLengthAverage() {
        int[] arr = {1, 2, 3};
        int k = 5;
        double result = maxAverageSubArrayOfSizeK(arr, k);
        System.out.println("Test 6 - k > array length: arr=" + Arrays.toString(arr) + ", k=" + k);
        System.out.printf("  Expected: -1.0, Got: %.2f, %s%n%n", result, Math.abs(result - (-1.0)) < 0.01 ? "PASS" : "FAIL");
    }

    private static void testSingleElementAverage() {
        int[] arr = {7};
        int k = 1;
        double result = maxAverageSubArrayOfSizeK(arr, k);
        System.out.println("Test 7 - Single element: arr=" + Arrays.toString(arr) + ", k=" + k);
        System.out.printf("  Expected: 7.0, Got: %.2f, %s%n%n", result, Math.abs(result - 7.0) < 0.01 ? "PASS" : "FAIL");
    }

    private static void testDecimalAverage() {
        int[] arr = {5, 1, 9, 2, 6, 3};
        int k = 2;
        double result = maxAverageSubArrayOfSizeK(arr, k);
        System.out.println("Test 8 - Decimal average: arr=" + Arrays.toString(arr) + ", k=" + k);
        System.out.printf("  Expected: 5.5, Got: %.2f, %s%n", result, Math.abs(result - 5.5) < 0.01 ? "PASS" : "FAIL");
    }

    public static void runMaxVowelsInSubstringOfSizeKTests() {
        System.out.println("=== maxVowelsInSubstringOfSizeKTests ===\n");

        testMaxVowelsInSubstringOfSizeK1();
        testMaxVowelsInSubstringOfSizeK2();
        testMaxVowelsInSubstringOfSizeK3();
        testMaxVowelsInSubstringOfSizeK4();
    }

    private static void testMaxVowelsInSubstringOfSizeK1() {
        String s = "hello";
        int k = 2;
        int result = maxVowelsInSubstringOfSizeK(s, k);
        System.out.println("Test 1 - Max vowels in substring of size 2: s=\"" + s + "\", k=" + k);
        System.out.println("  Expected: 1, Got: " + result + ", " + (result == 1 ? "PASS" : "FAIL") + "\n");
    }

    private static void testMaxVowelsInSubstringOfSizeK2() {
        String s = "hi";
        int k = 3;
        int result = maxVowelsInSubstringOfSizeK(s, k);
        System.out.println("Test 2 - String length < k: s=\"" + s + "\", k=" + k);
        System.out.println("  Expected: 0, Got: " + result + ", " + (result == 0 ? "PASS" : "FAIL") + "\n");
    }

    private static void testMaxVowelsInSubstringOfSizeK3() {
        String s = "bcdfg";
        int k = 2;
        int result = maxVowelsInSubstringOfSizeK(s, k);
        System.out.println("Test 3 - No vowels: s=\"" + s + "\", k=" + k);
        System.out.println("  Expected: 0, Got: " + result + ", " + (result == 0 ? "PASS" : "FAIL") + "\n");
    }

    private static void testMaxVowelsInSubstringOfSizeK4() {
        String s = "aeiou";
        int k = 3;
        int result = maxVowelsInSubstringOfSizeK(s, k);
        System.out.println("Test 4 - All vowels: s=\"" + s + "\", k=" + k);
        System.out.println("  Expected: 3, Got: " + result + ", " + (result == 3 ? "PASS" : "FAIL") + "\n");
    }
}