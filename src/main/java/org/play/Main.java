package org.play;

import org.play.slidingWindow.FixedSlidingWindow;
import org.play.slidingWindow.VariableSlidingWindow;
import org.play.twoPointers.TwoPointers;

public class Main {
    public static void main(String[] args) {
        FixedSlidingWindow.runMaxSumOfSubarrayOfSizeKTests();
        FixedSlidingWindow.runMaxAverageSubArrayOfSizeKTests();
        FixedSlidingWindow.runMaxVowelsInSubstringOfSizeKTests();
        FixedSlidingWindow.runtestPermutationInStringTests();
        TwoPointers.runMaxSumUsingTwoPointersTests();
        VariableSlidingWindow.runMaxFruitsIntoBasketOfSameColorTests();
        VariableSlidingWindow.runLongestSubstringTests();
        VariableSlidingWindow.runMinimumSizeSubarraySumTests();
        VariableSlidingWindow.runLongestRepeatingCharReplacementTests();

    }



//    public static void main(String[] args) {
//        int [] arr = { 1, 2, 3, 4, 2, 3, 5};
//        TestClass.findDuplicates(arr);
//
//    }
}