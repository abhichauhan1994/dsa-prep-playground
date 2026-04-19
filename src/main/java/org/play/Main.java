package org.play;

import org.play.slidingWindow.FixedSlidingWindow;
import org.play.slidingWindow.VariableSlidingWindow;
import org.play.twoPointers.TwoPointers;

public class Main {
    public static void main(String[] args) {
        FixedSlidingWindow.runMaxSumOfSubarrayOfSizeKTests();
        FixedSlidingWindow.runMaxAverageSubArrayOfSizeKTests();
        FixedSlidingWindow.runMaxVowelsInSubstringOfSizeKTests();
        TwoPointers.runMaxSumUsingTwoPointersTests();
        VariableSlidingWindow.runMinimumSizeSubarraySumTests();
        VariableSlidingWindow.runLongestSubstringTests();
        VariableSlidingWindow.runMaxFruitsIntoBasketOfSameColorTests();
    }
}