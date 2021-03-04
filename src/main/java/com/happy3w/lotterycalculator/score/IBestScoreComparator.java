package com.happy3w.lotterycalculator.score;

import com.happy3w.lotterycalculator.TestScore;

public interface IBestScoreComparator {
    String getName();
    TestScore bestScore(TestScore s1, TestScore s2);
}
