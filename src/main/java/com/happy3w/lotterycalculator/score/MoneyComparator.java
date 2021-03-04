package com.happy3w.lotterycalculator.score;

import com.happy3w.lotterycalculator.TestScore;

public class MoneyComparator implements IBestScoreComparator {
    @Override
    public String getName() {
        return "money";
    }

    @Override
    public TestScore bestScore(TestScore s1, TestScore s2) {
        return s1.getWinMoney() < s2.getWinMoney() ? s2 : s1;
    }
}
