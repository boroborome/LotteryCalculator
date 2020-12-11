package com.happy3w.lotterycalculator;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.function.Function;

public class LotteryCalculator {
    private NumWeight[] redWeights;
    private NumWeight[] blueWeights;

    private List<NumWeight> orderedRed;
    private List<NumWeight> orderedBlue;

    private double forgetRate;
    private Function<Double, Double> rememberLogic;

    public LotteryCalculator(Function<Double, Double> rememberLogic, double forgetRate) {
        redWeights = newNumWeight(33);
        blueWeights = newNumWeight(16);

        orderedRed = new ArrayList<>(Arrays.asList(redWeights));
        orderedBlue = new ArrayList<>(Arrays.asList(blueWeights));

        this.rememberLogic = rememberLogic;
        this.forgetRate = forgetRate;
    }

    public LotteryCalculator() {
        this((weight) -> {
            weight += 1;
            weight *= weight;
            return weight;
        }, 0.568);
    }

    private NumWeight[] newNumWeight(int count) {
        NumWeight[] weights = new NumWeight[count];
        for (int i = 0; i < count; i++) {
            NumWeight nw = new NumWeight(i + 1, 0);
            weights[i] = nw;
        }
        return weights;
    }

    public LotteryInfo accept(LotteryInfo info) {
        forget(redWeights);
        forget(blueWeights);

        remember(info.getReds(), redWeights);
        remember(info.getBlues(), blueWeights);

        return selectLottery();
    }

    private LotteryInfo selectLottery() {
        int[] reds = selectNum(orderedRed, 6);
        int[] blues = selectNum(orderedBlue, 1);
        return new LotteryInfo(null, reds, blues);
    }

    private int[] selectNum(List<NumWeight> weights, int count) {
        weights.sort(Comparator.comparing(NumWeight::getWeight));

        int[] nums = new int[count];
        for (int i = 0; i < count; i++) {
            nums[i] = weights.get(i).num;
        }
        Arrays.sort(nums);
        return nums;
    }

    private void remember(int[] values, NumWeight[] weights) {
        for (int i = values.length - 1; i >= 0; i--) {
            NumWeight weightInfo = weights[values[i] - 1];
            weightInfo.weight = rememberLogic.apply(weightInfo.weight);
        }
    }

    private void forget(NumWeight[] weights) {
        for (int i = weights.length - 1; i >= 0; i--) {
            weights[i].weight *= forgetRate;
        }
    }

    @Getter
    @AllArgsConstructor
    private static class NumWeight {
        private int num;
        private double weight;

        @Override
        public String toString() {
            return MessageFormat.format("{0}:{1}", num, weight);
        }
    }
}
