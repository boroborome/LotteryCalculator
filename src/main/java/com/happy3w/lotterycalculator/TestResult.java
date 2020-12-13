package com.happy3w.lotterycalculator;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Map;

@Getter
@AllArgsConstructor
public class TestResult {
    Map<String, Integer> winCounts;
    LotteryInfo nextLottery;
}
