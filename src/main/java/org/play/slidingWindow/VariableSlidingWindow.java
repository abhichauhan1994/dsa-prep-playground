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