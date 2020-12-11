package com.happy3w.lotterycalculator;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
@AllArgsConstructor
public class TestScore {
    private double forgetRate;
    private Map<String, Integer> winCounts;
    private int winMoney;
}
