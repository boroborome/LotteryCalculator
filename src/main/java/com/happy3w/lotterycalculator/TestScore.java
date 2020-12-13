package com.happy3w.lotterycalculator;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class TestScore {
    private RememberFunInfo rememberFunInfo;
    private double forgetRate;
    private Map<String, Integer> winCounts;
    private int winMoney;
    LotteryInfo nextLottery;
}
