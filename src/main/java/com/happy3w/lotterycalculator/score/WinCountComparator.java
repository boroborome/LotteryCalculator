package com.happy3w.lotterycalculator.score;

import com.happy3w.lotterycalculator.TestScore;

public class WinCountComparator implements IBestScoreComparator {
    @Override
    public String getName() {
        return "count";
    }

    @Override
    public TestScore bestScore(TestScore s1, TestScore s2) {
        int c1 = s1.getWinCounts().size();
        int c2 = s2.getWinCounts().size();

        if (c1 > c2) {
            return s1;
        } else if (c2 > c1) {
            return s2;
        } else {
            return s1.getWinMoney() < s2.getWinMoney() ? s2 : s1;
        }
    }
}
