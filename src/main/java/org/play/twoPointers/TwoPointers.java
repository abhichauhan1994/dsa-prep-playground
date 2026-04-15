package org.play.twoPointers;

public class TwoPointers {

    public static int maxSumUsingTwoPointers(int arr[], int k){
        if(arr.length < k){
            return -1;
        }

        int maxSum = Integer.MIN_VALUE;
        int leftPointer = 0;
        int rightPointer = 0;
        int pointerSum = 0;

        while(rightPointer < arr.length){
            pointerSum += arr[rightPointer];

            if(rightPointer - leftPointer + 1 == k){
                maxSum = Math.max(pointerSum, maxSum);
                pointerSum -= arr[leftPointer];
                leftPointer++;
            }

            rightPointer++;
        }

        return maxSum;
    }

    public static void runMaxSumUsingTwoPointersTests() {
        System.out.println("=== maxSumUsingTwoPointersTests ===\n");

        testMaxSumUsingTwoPointers1();
        testMaxSumUsingTwoPointers2();
        testMaxSumUsingTwoPointers3();
        testMaxSumUsingTwoPointers4();
    }

    private static void testMaxSumUsingTwoPointers1() {
        int[] arr = {1, 2, 3, 4, 5};
        int k = 3;
        int result = maxSumUsingTwoPointers(arr, k);
        System.out.println("Test 1 - Max sum of subarray of size 3: arr=" + java.util.Arrays.toString(arr) + ", k=" + k);
        System.out.println("  Expected: 12, Got: " + result + ", " + (result == 12 ? "PASS" : "FAIL") + "\n");
    }

    private static void testMaxSumUsingTwoPointers2() {
        int[] arr = {1, 2};
        int k = 3;
        int result = maxSumUsingTwoPointers(arr, k);
        System.out.println("Test 2 - Array length < k: arr=" + java.util.Arrays.toString(arr) + ", k=" + k);
        System.out.println("  Expected: -1, Got: " + result + ", " + (result == -1 ? "PASS" : "FAIL") + "\n");
    }

    private static void testMaxSumUsingTwoPointers3() {
        int[] arr = {-1, -2, -3, -4};
        int k = 2;
        int result = maxSumUsingTwoPointers(arr, k);
        System.out.println("Test 3 - Negative numbers: arr=" + java.util.Arrays.toString(arr) + ", k=" + k);
        System.out.println("  Expected: -3, Got: " + result + ", " + (result == -3 ? "PASS" : "FAIL") + "\n");
    }

    private static void testMaxSumUsingTwoPointers4() {
        int[] arr = {10, 20, 30, 40, 50};
        int k = 1;
        int result = maxSumUsingTwoPointers(arr, k);
        System.out.println("Test 4 - k=1: arr=" + java.util.Arrays.toString(arr) + ", k=" + k);
        System.out.println("  Expected: 50, Got: " + result + ", " + (result == 50 ? "PASS" : "FAIL") + "\n");
    }

}
