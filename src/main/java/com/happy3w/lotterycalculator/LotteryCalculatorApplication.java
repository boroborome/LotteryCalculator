package com.happy3w.lotterycalculator;

import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;

@SpringBootApplication
public class LotteryCalculatorApplication {

	public static void main(String[] args) throws IOException {

		LotteryCalculator lotteryCalculator = new LotteryCalculator();
//		SpringApplication.run(LotteryCalculatorApplication.class, args);
		InputStream inputStream = LotteryCalculatorApplication.class.getResourceAsStream("/lottery-history.txt");
		BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

		Map<String, Integer> winCounts = new HashMap<>();
		LotteryInfo nextLottery = null;
		int lotteryCount = 0;
		for (String line = reader.readLine(); line != null; line = reader.readLine()) {
			LotteryInfo info = LotteryInfo.decode(line);
			lotteryCount++;

			LotteryInfo newLottery = lotteryCalculator.accept(info);
			System.out.print(MessageFormat.format("{0}:{1}",
					info.getCode(), info.getDesc()));
			if (nextLottery == null) {
				System.out.println();
			} else {
				String winPrice = nextLottery.winPrice(info);
				System.out.println(MessageFormat.format("--{0}--{1}",
						nextLottery.getDesc(), winPrice));

				int count = winCounts.getOrDefault(winPrice, 0);
				winCounts.put(winPrice, count + 1);
			}
			nextLottery = newLottery;
		}

		System.out.println(winCounts);
		int cost = lotteryCount * 2;
		int win = calculateMoney(winCounts);
		System.out.println(MessageFormat.format("Cost:{0}; Win:{1}", cost, win));
	}

	private static int calculateMoney(Map<String, Integer> winCounts) {
		int money = 0;
		for (Map.Entry<String, Integer> entry : winCounts.entrySet()) {
			money += (entry.getValue() * LotteryInfo.winMoney(entry.getKey()));
		}
		return money;
	}

}
