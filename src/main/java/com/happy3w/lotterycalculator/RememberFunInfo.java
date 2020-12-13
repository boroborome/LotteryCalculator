package com.happy3w.lotterycalculator;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.function.Function;

@Getter
@AllArgsConstructor
public class RememberFunInfo {
    private String name;
    private Function<Double, Double> rememberFun;
}
