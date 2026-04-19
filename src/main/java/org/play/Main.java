package org.play;

import org.play.slidingWindow.FixedSlidingWindow;
import org.play.slidingWindow.VariableSlidingWindow;

public class Main {
    public static void main(String[] args) {
        FixedSlidingWindow.runMaxSumOfSubarrayOfSizeKTests();
        FixedSlidingWindow.runMaxAverageSubArrayOfSizeKTests();
        VariableSlidingWindow.runMinimumSizeSubarraySumTests();
        VariableSlidingWindow.runLongestSubstringTests();
        VariableSlidingWindow.runMaxFruitsIntoBasketOfSameColorTests();
    }
}