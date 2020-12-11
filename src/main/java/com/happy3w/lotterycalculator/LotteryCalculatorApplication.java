package com.happy3w.lotterycalculator;

import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

@SpringBootApplication
public class LotteryCalculatorApplication {

	private static Function<Double, Double> DefaultRememberFun = (weight) -> {
		weight += 1;
		weight *= weight;
		return weight;
	};

	public static void main(String[] args) throws IOException {
		List<LotteryInfo> lotteryInfos = loadAllLotteryInfos();

		TestScore bestScore = null;

		for (double forgetRate = 0.01; forgetRate < 1; forgetRate += 0.01) {
			LotteryCalculator lotteryCalculator = new LotteryCalculator(DefaultRememberFun, forgetRate);
			Map<String, Integer> winCounts = testCalculator(lotteryCalculator, lotteryInfos);
			int winMoney = calculateMoney(winCounts);
			if (bestScore == null || bestScore.getWinMoney() < winMoney) {
				bestScore = new TestScore(forgetRate, winCounts, winMoney);
			}
		}
		int cost = lotteryInfos.size() * 2;
		System.out.println(bestScore.getWinCounts());
		System.out.println(MessageFormat.format("ForgetRate:{0}; Cost:{1}; Win:{2}",
				bestScore.getForgetRate(),
				cost,
				bestScore.getWinMoney()));
	}

	private static Map<String, Integer> testCalculator(LotteryCalculator lotteryCalculator, List<LotteryInfo> lotteryInfos) {
		Map<String, Integer> winCounts = new HashMap<>();
		LotteryInfo nextLottery = null;
		for (LotteryInfo info : lotteryInfos) {

			LotteryInfo newLottery = lotteryCalculator.accept(info);
//			System.out.print(MessageFormat.format("{0}:{1}",
//					info.getCode(), info.getDesc()));
			if (nextLottery == null) {
				System.out.println();
			} else {
				String winPrice = nextLottery.winPrice(info);
//				System.out.println(MessageFormat.format("--{0}--{1}",
//						nextLottery.getDesc(), winPrice));

				int count = winCounts.getOrDefault(winPrice, 0);
				winCounts.put(winPrice, count + 1);
			}
			nextLottery = newLottery;
		}
		return winCounts;
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
