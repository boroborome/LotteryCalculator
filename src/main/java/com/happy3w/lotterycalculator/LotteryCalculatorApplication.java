package com.happy3w.lotterycalculator;

import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

@SpringBootApplication
public class LotteryCalculatorApplication {

	private static final List<RememberFunInfo> RememberFuns = Arrays.asList(
			new RememberFunInfo("power weight", (weight) -> {
				weight += 1;
				weight *= weight;
				return weight;
			}),
			new RememberFunInfo("plus 1", (weight) -> {
				weight += 1;
				return weight;
			})
	);

	private static Function<Double, Double> DefaultRememberFun = (weight) -> {
		weight += 1;
		weight *= weight;
		return weight;
	};

	public static void main(String[] args) throws IOException {
		List<LotteryInfo> lotteryInfos = loadAllLotteryInfos();

		TestScore bestScore = iterativeCalculateBestScore(lotteryInfos, 3);

		int cost = lotteryInfos.size() * 2;
		System.out.println(bestScore.getWinCounts());
		System.out.println(MessageFormat.format("RemFun:{0};ForgetRate:{1}; Cost:{2}; Win:{3}",
				bestScore.getRememberFunInfo().getName(),
				bestScore.getForgetRate(),
				cost,
				bestScore.getWinMoney()));
		System.out.println(bestScore.getNextLottery().getDesc());
	}

	private static final int StepCount = 100;

	private static TestScore iterativeCalculateBestScore(List<LotteryInfo> lotteryInfos, int times) {
		double start = 0;
		double end = 1;
		TestScore bestScore = null;
		for (int i = 0; i < times; i++) {
			TestScore curScore = calculateBestScore(lotteryInfos, start, end);
			if (bestScore == null || bestScore.getWinMoney() < curScore.getWinMoney()) {
				bestScore = curScore;
			}

			double step = (end - start) / StepCount;
			double newForgetRate = curScore.getForgetRate();
			start = newForgetRate - step;
			end = newForgetRate + step;
		}
		return bestScore;
	}

	private static TestScore calculateBestScore(List<LotteryInfo> lotteryInfos, double start, double end) {
		TestScore bestScore = null;
		double step = (end - start) / StepCount;
		for (double forgetRate = start + step; forgetRate < end; forgetRate += step) {
			for (RememberFunInfo funInfo : RememberFuns) {
				bestScore = calculateBestScore(funInfo, forgetRate, lotteryInfos, bestScore);
			}
		}
		return bestScore;
	}

	private static TestScore calculateBestScore(RememberFunInfo funInfo, double forgetRate, List<LotteryInfo> lotteryInfos, TestScore bestScore) {
		LotteryCalculator lotteryCalculator = new LotteryCalculator(funInfo.getRememberFun(), forgetRate);
		TestResult testResult = testCalculator(lotteryCalculator, lotteryInfos);
		Map<String, Integer> winCounts = testResult.getWinCounts();
		int winMoney = calculateMoney(winCounts);
		if (bestScore == null || bestScore.getWinMoney() < winMoney) {
			bestScore = TestScore.builder()
					.rememberFunInfo(funInfo)
					.forgetRate(forgetRate)
					.winCounts(winCounts)
					.winMoney(winMoney)
					.nextLottery(testResult.nextLottery)
					.build();
		}
		return bestScore;
	}

	private static TestResult testCalculator(LotteryCalculator lotteryCalculator, List<LotteryInfo> lotteryInfos) {
		Map<String, Integer> winCounts = new HashMap<>();
		LotteryInfo nextLottery = null;
		for (LotteryInfo info : lotteryInfos) {

			LotteryInfo newLottery = lotteryCalculator.accept(info);
			if (nextLottery != null) {
				String winPrice = nextLottery.winPrice(info);
				int count = winCounts.getOrDefault(winPrice, 0);
				winCounts.put(winPrice, count + 1);
			}
			nextLottery = newLottery;
		}
		return new TestResult(winCounts, nextLottery);
	}

	private static List<LotteryInfo> loadAllLotteryInfos() throws IOException {
		InputStream inputStream = LotteryCalculatorApplication.class.getResourceAsStream("/lottery-history.txt");
		BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

		List<LotteryInfo> infos = new ArrayList<>();
		for (String line = reader.readLine(); line != null; line = reader.readLine()) {
			LotteryInfo info = LotteryInfo.decode(line);
			infos.add(info);
		}
		return infos;
	}

	private static int calculateMoney(Map<String, Integer> winCounts) {
		int money = 0;
		for (Map.Entry<String, Integer> entry : winCounts.entrySet()) {
			money += (entry.getValue() * LotteryInfo.winMoney(entry.getKey()));
		}
		return money;
	}

}
